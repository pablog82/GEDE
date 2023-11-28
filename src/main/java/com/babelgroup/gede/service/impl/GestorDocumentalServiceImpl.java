package com.babelgroup.gede.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.babelgroup.gede.dto.DatosAltaDocumentoRequest;
import com.babelgroup.gede.dto.DatosAltaFirmantesRequest;
import com.babelgroup.gede.dto.DocumentoResponse;
import com.babelgroup.gede.dto.FirmaResponse;
import com.babelgroup.gede.dto.RespuestaGenerica;
import com.babelgroup.gede.dto.TicketRequest;
import com.babelgroup.gede.dto.TicketResponse;
import com.babelgroup.gede.dto.ValoresMetadato;
import com.babelgroup.gede.exception.GeneralException;
import com.babelgroup.gede.model.Registro;
import com.babelgroup.gede.service.FirmaService;
import com.babelgroup.gede.service.GestorDocumentalService;
import com.babelgroup.gede.service.RegistroService;
import com.babelgroup.gede.util.EncodeDecode;

import lombok.extern.log4j.Log4j2;

/**
 * The Class GestorDocumentalServiceImpl.
 */
@Service

/** The Constant log. */

/** The Constant log. */
@Log4j2
public class GestorDocumentalServiceImpl implements GestorDocumentalService {

	/** The Constant PASO_4_PROCESANDO_FICHERO_FICHEROS_EN_LA_CARPETA. */
	private static final String PASO_4_PROCESANDO_FICHERO_FICHEROS_EN_LA_CARPETA = "PASO 4 - Procesando fichero ficheros en la carpeta: {0}";

	/** The Constant DOCUMENTOS_DE_EXPEDIENTES_ENCONTRADOS. */
	private static final String DOCUMENTOS_DE_EXPEDIENTES_ENCONTRADOS = "-- Documentos de expedientes encontrados: {0}";

	/** The Constant PASO_2_LISTAR_FICHEROS_EN_LA_CARPETA. */
	private static final String PASO_2_LISTAR_FICHEROS_EN_LA_CARPETA = "PASO 2 - Listar ficheros en la carpeta: {0}";

	/** The Constant CARPETAS_DE_EXPEDIENTES_ENCONTRADAS. */
	private static final String CARPETAS_DE_EXPEDIENTES_ENCONTRADAS = "--- Carpetas de expedientes encontradas: {0}";

	/** The Constant PASO_1_LISTAR_CARPETAS_DE_EXPEDIENTES. */
	private static final String PASO_1_LISTAR_CARPETAS_DE_EXPEDIENTES = "PASO 1 - Listar carpetas de expedientes";

	/** The Constant INICIO_DEL_PROCESO_DE_DOCUMENTOS. */
	private static final String INICIO_DEL_PROCESO_DE_DOCUMENTOS = "-- INICIO DEL PROCESO DE DOCUMENTOS ---";

	/** The dir expedientes. */
	@Value("${dir.expedientes}")
	private String dirExpedientes;

	/** The gede usuario. */
	@Value("${gede.usuario}")
	private String gedeUsuario;

	/** The gede password. */
	@Value("${gede.password}")
	private String gedePassword;

	/** The gede organismo. */
	@Value("${gede.organismo}")
	private String gedeOrganismo;

	/** The gede api ticket. */
	@Value("${gede.api.ticket}")
	private String gedeApiTicket;

	/** The gede api ticket validar. */
	@Value("${gede.api.ticket.validar}")
	private String gedeApiTicketValidar;

	/** The gede api binarios. */
	@Value("${gede.api.binarios}")
	private String gedeApiBinarios;

	/** The gede api binarios. */
	@Value("${gede.api.documentos.crear}")
	private String gedeApiDocumentosCrear;

	/** The gede api binarios. */
	@Value("${gede.api.documentos.agreagar.firmantes}")
	private String gedeApiDocumentosAgregarFirmantes;

	/** The gede api binarios. */
	@Value("${gede.api.documentos.cerrar}")
	private String gedeApiDocumentosCerrar;

	/** The firma service. */
	@Autowired
	private FirmaService firmaService;

	/** The registro service. */
	@Autowired
	private RegistroService registroService;

	/** The rest template. */
	@Autowired
	private RestTemplate restTemplate;

	/** The ticket response. */
	private TicketResponse ticketResponse;

	/** The intentos llamada API. */
	private int intentosLlamadaAPI = 0;

	/**
	 * Subir documentos.
	 */
	@Override
	public void subirDocumentos() {

		log.info(INICIO_DEL_PROCESO_DE_DOCUMENTOS);

		List<File> listadoDirectoriosExpedientes = null;

		try {
			// 1. Listar carpetas
			log.info(PASO_1_LISTAR_CARPETAS_DE_EXPEDIENTES);
			listadoDirectoriosExpedientes = listDirectories(dirExpedientes);

			if (listadoDirectoriosExpedientes != null && !listadoDirectoriosExpedientes.isEmpty()) {

				log.info(MessageFormat.format(CARPETAS_DE_EXPEDIENTES_ENCONTRADAS,
						listadoDirectoriosExpedientes.toString()));

				// 2. Listar fichero por carpetas
				for (File directorioExpediente : listadoDirectoriosExpedientes) {

					log.info(MessageFormat.format(PASO_2_LISTAR_FICHEROS_EN_LA_CARPETA, directorioExpediente));

					String expediente = directorioExpediente.getName();

					try {

						List<File> listadoFicherosExpediente = listFiles(directorioExpediente.getAbsolutePath());
						if (listadoFicherosExpediente != null && !listadoFicherosExpediente.isEmpty()) {

							log.info(MessageFormat.format(DOCUMENTOS_DE_EXPEDIENTES_ENCONTRADOS,
									listadoFicherosExpediente.toString()));

							// 4. Procesar fichero
							for (File ficheroExpediente : listadoFicherosExpediente) {

								log.info(MessageFormat.format(PASO_4_PROCESANDO_FICHERO_FICHEROS_EN_LA_CARPETA,
										directorioExpediente));

								String documento = ficheroExpediente.getName();

								// TODO 4.1. Comprobar si está procesado

								List<Registro> registros = registroService.findByExpedienteAndDocumento(expediente,
										documento);

								if (registros.isEmpty()) {

									// TODO 4.2. Almacenar binario

									intentosLlamadaAPI = 0;

									RespuestaGenerica respuestaAlmacenarBinario;
									try {
										respuestaAlmacenarBinario = almacenarBinario(ficheroExpediente);
									} catch (GeneralException e) {
										Registro registro = new Registro(expediente, documento, null,
												"Error al almacenar el binario: " + e.getMessage(), "ERROR");
										registroService.insert(registro);
										throw new GeneralException(e);
									}

									intentosLlamadaAPI = 0;

									// TODO 4.3. Crear documento

									DocumentoResponse responseCrearDocumento;
									try {
										responseCrearDocumento = crearDocumento(
												respuestaAlmacenarBinario.getIdentificador());
									} catch (GeneralException e) {
										Registro registro = new Registro(expediente, documento, null,
												"Error al crear el documento: " + e.getMessage(), "ERROR");
										registroService.insert(registro);
										throw new GeneralException(e);
									}

									// TODO 4.4. Firmar

									String justificanteBase64 = EncodeDecode
											.encode(Files.readAllBytes(ficheroExpediente.toPath()));

//									String firmarJustificante = firmaService.firmarJustificante(justificanteBase64);

//									FileUtils.writeByteArrayToFile(
//											new File(ficheroExpediente.getAbsolutePath().toUpperCase().replace(".PDF",
//													UUID.randomUUID().toString() + "_FIRMADO.PDF")),
//											EncodeDecode.decode(firmarJustificante));

									// TODO 4.5 Almacenar firmantes

									intentosLlamadaAPI = 0;

									try {
										FirmaResponse firmaResponse = agregarFirmantes(documento, null);
									} catch (GeneralException e) {
										Registro registro = new Registro(expediente, documento, null,
												"Error al almacenar los firmantes el documento: " + e.getMessage(), "ERROR");
										registroService.insert(registro);
										throw new GeneralException(e);
									}

									// TODO 4.6 Cerrar documento
									intentosLlamadaAPI = 0;

									try {
										cerrarDocumento(responseCrearDocumento.getIdentificador());
									} catch (GeneralException e) {
										Registro registro = new Registro(expediente, documento, null,
												"Error al cerrar el documento: " + e.getMessage(), "ERROR");
										registroService.insert(registro);
										throw new GeneralException(e);
									}

									// TODO 4.6 Registar en base de datos
									Registro registro = new Registro(expediente, documento,
											responseCrearDocumento.getIdentificador(), "Documento almacenado", "OK");
									registroService.insert(registro);
								} else {
									log.info("Ya se ha procesado el documento");
								}

							}
						} else {
							log.info("--- No se han encontrado documentos que procesar");
						}

					} catch (IOException e) {
						throw new GeneralException(
								"Error al listar los ficheros de la carperta " + directorioExpediente, e);
					}
				}
			} else {
				log.info("--- No se han encontrado carpetas que procesar");
			}

		} catch (IOException e) {
			log.info("--- Error al listar los expedientes: " + e.getMessage());
		}
		catch (GeneralException e) {
			log.info(e);
		}

		log.info("-- FIN DEL PROCESO DE DOCUMENTOS ---");
	}

	/**
	 * Obtener ticket.
	 *
	 * @throws GeneralException the general exception
	 */
	private void obtenerTicket() throws GeneralException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<TicketRequest> requestEntity = new HttpEntity<>(
				new TicketRequest(gedeUsuario, gedeOrganismo, gedePassword), headers);

		ResponseEntity<TicketResponse> response = restTemplate.exchange(gedeApiTicket, HttpMethod.POST, requestEntity,
				new ParameterizedTypeReference<TicketResponse>() {
				});

		if (HttpStatus.CREATED.equals(response.getStatusCode())) {
			this.ticketResponse = response.getBody();
//			log.info(ticketResponse);
		} else if (intentosLlamadaAPI < 3) {
			intentosLlamadaAPI++;
			log.info("obtenerTicket() - Error al llamar a la API - Reintento " + intentosLlamadaAPI);
		} else {
			throw new GeneralException("obtenerTicket() - Error al llamar a la API - Superado número de reintentos: "
					+ response.getStatusCode().toString());
		}
		{
			log.info(response);
		}
	}

	/**
	 * Validar ticket.
	 *
	 * @return true, if successful
	 */
	private boolean validarTicket() {

//		if (this.ticketResponse != null && StringUtils.isNoneEmpty(this.ticketResponse.getTicket())) {
//
//			HttpHeaders headers = new HttpHeaders();
//			headers.set("Authorization", this.ticketResponse.getTicket());
//			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//
//			HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
//
//			ResponseEntity<TicketResponse> response = restTemplate.exchange(gedeApiTicketValidar, HttpMethod.GET, requestEntity,
//					new ParameterizedTypeReference<TicketResponse>() {
//					});
//
//			if (HttpStatus.OK.equals(response.getStatusCode())) {
//				return true;
//			}
//		}

		// si la fecha de caducidad del ticket que tenemos es pasado ya no sirve el
		// ticker
		if (this.ticketResponse.getCaducidad().after(new Date())) {
			return false;
		}

		return true;
	}

	/**
	 * Gets the tipos documentales.
	 *
	 * @param contentType the content type
	 * @return the tipos documentales
	 * @throws GeneralException the general exception
	 */
	// TODO llamada de prueba para usar el ticket (BORRAR)
//	private void getTiposDocumentales() throws GeneralException {
//
//		HttpHeaders headers = cabeceraConTicket(MediaType.APPLICATION_JSON);
//
//		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
//
//		ResponseEntity<String> response = restTemplate.exchange(
//				"https://pre-plataforma.sevilla.org/gede-rest/api/v1/tipoDocumentales?operacion=ESCRITURA&formato=PAPEL&tipoEntidad=EXPEDIENTE",
//				HttpMethod.GET, requestEntity, new ParameterizedTypeReference<String>() {
//				});
//
//		if (HttpStatus.OK.equals(response.getStatusCode())) {
//
//			log.info(response);
//		} else {
//			log.info(response);
//		}
//	}

	/**
	 * Cabecera con ticket.
	 *
	 * @param contentType the content type
	 * @return the http headers
	 * @throws GeneralException
	 */
	private HttpHeaders cabeceraConTicket(MediaType contentType) throws GeneralException {

		if (!validarTicket()) {
			obtenerTicket();
		}

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", this.ticketResponse.getTicket());
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setContentType(contentType);
		return headers;
	}

	/**
	 * Almacenar binario.
	 *
	 * @param fichero the fichero
	 * @return the respuesta generica
	 * @throws GeneralException the general exception
	 */
	private RespuestaGenerica almacenarBinario(File fichero) throws GeneralException {
		HttpHeaders headers = cabeceraConTicket(MediaType.APPLICATION_OCTET_STREAM);

		HttpEntity<byte[]> requestEntity;
		try {
			requestEntity = new HttpEntity<>(FileUtils.readFileToByteArray(fichero), headers);
			ResponseEntity<RespuestaGenerica> response = restTemplate.exchange(gedeApiBinarios, HttpMethod.POST,
					requestEntity, new ParameterizedTypeReference<RespuestaGenerica>() {
					});
			if (HttpStatus.CREATED.equals(response.getStatusCode())) {
				return response.getBody();
			} else if (HttpStatus.UNAUTHORIZED.equals(response.getStatusCode()) && intentosLlamadaAPI < 3) {
				intentosLlamadaAPI++;
				log.info("almacenarBinario() - Error al llamar a la API - Reintento " + intentosLlamadaAPI);
				return almacenarBinario(fichero);
			} else {
				throw new GeneralException(
						"almacenarBinario() - Error al llamar a la API - Superado número de reintentos: "
								+ response.getStatusCode().toString());
			}
		} catch (IOException e) {
			throw new GeneralException("almacenarBinario() - Error al leer el fichero a almacenar", e);
		}
	}

	/**
	 * Crear documento.
	 *
	 * @param idBinario the id binario
	 * @return the documento response
	 * @throws GeneralException the general exception
	 */
	private DocumentoResponse crearDocumento(String idBinario) throws GeneralException {
		HttpHeaders headers = cabeceraConTicket(MediaType.APPLICATION_JSON);
		HttpEntity<DatosAltaDocumentoRequest> requestEntity;
		DatosAltaDocumentoRequest datosAlta = new DatosAltaDocumentoRequest();
		requestEntity = new HttpEntity<>(datosAlta, headers);
		ResponseEntity<DocumentoResponse> response = restTemplate.exchange(gedeApiDocumentosCrear, HttpMethod.POST,
				requestEntity, new ParameterizedTypeReference<DocumentoResponse>() {
				});
		if (HttpStatus.CREATED.equals(response.getStatusCode())) {
			return response.getBody();
		} else if (HttpStatus.UNAUTHORIZED.equals(response.getStatusCode()) && intentosLlamadaAPI < 3) {
			intentosLlamadaAPI++;
			log.info("crearDocumento() - Error al llamar a la API - Reintento " + intentosLlamadaAPI);
			return crearDocumento(idBinario);
		} else {
			throw new GeneralException("crearDocumento() - Error al llamar a la API - Superado número de reintentos: "
					+ response.getStatusCode().toString());
		}
	}

	/**
	 * Cerrar documento.
	 *
	 * @param idDocumento the id documento
	 * @return the respuesta generica
	 * @throws GeneralException the general exception
	 */
	private RespuestaGenerica cerrarDocumento(String idDocumento) throws GeneralException {

		HttpHeaders headers = cabeceraConTicket(MediaType.APPLICATION_JSON);
		HttpEntity<DatosAltaDocumentoRequest> requestEntity;
		DatosAltaDocumentoRequest datosAlta = new DatosAltaDocumentoRequest();
		requestEntity = new HttpEntity<>(datosAlta, headers);
		ResponseEntity<RespuestaGenerica> response = restTemplate.exchange(gedeApiDocumentosCerrar, HttpMethod.POST,
				requestEntity, new ParameterizedTypeReference<RespuestaGenerica>() {
				});
		if (HttpStatus.CREATED.equals(response.getStatusCode())) {
			return response.getBody();
		} else if (HttpStatus.UNAUTHORIZED.equals(response.getStatusCode()) && intentosLlamadaAPI < 3) {
			intentosLlamadaAPI++;
			log.info("cerrarDocumento() - Error al llamar a la API - Reintento " + intentosLlamadaAPI);
			return cerrarDocumento(idDocumento);
		} else {
			throw new GeneralException("cerrarDocumento() - Error al llamar a la API - Superado número de reintentos: "
					+ response.getStatusCode().toString());
		}
	}

	/**
	 * Agregar firmantes.
	 *
	 * @param idDocumento the id documento
	 * @param metadatos   the metadatos
	 * @return the firma response
	 * @throws GeneralException the general exception
	 */
	private FirmaResponse agregarFirmantes(String idDocumento, List<ValoresMetadato> metadatos)
			throws GeneralException {

		HttpHeaders headers = cabeceraConTicket(MediaType.APPLICATION_JSON);
		HttpEntity<DatosAltaFirmantesRequest> requestEntity;
		DatosAltaFirmantesRequest datosAlta = new DatosAltaFirmantesRequest();
		requestEntity = new HttpEntity<>(datosAlta, headers);
		ResponseEntity<FirmaResponse> response = restTemplate.exchange(gedeApiDocumentosAgregarFirmantes,
				HttpMethod.POST, requestEntity, new ParameterizedTypeReference<FirmaResponse>() {
				});
		if (HttpStatus.CREATED.equals(response.getStatusCode())) {
			return response.getBody();
		} else if (HttpStatus.UNAUTHORIZED.equals(response.getStatusCode()) && intentosLlamadaAPI < 3) {
			intentosLlamadaAPI++;
			log.info("agregarFirmantes() - Error al llamar a la API - Reintento " + intentosLlamadaAPI);
			return agregarFirmantes(idDocumento, metadatos);
		} else {
			throw new GeneralException("agregarFirmantes() - Error al llamar a la API - Superado número de reintentos: "
					+ response.getStatusCode().toString());
		}
	}

	/**
	 * List directories.
	 *
	 * @param directory the directory
	 * @return the sets the
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private List<File> listDirectories(String directory) throws IOException {

		List<File> directories = new ArrayList<File>();
		File[] files = new File(directory).listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				directories.add(file);
			}
		}

		return directories;
	}

	/**
	 * List files.
	 *
	 * @param directory the directory
	 * @return the sets the
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private List<File> listFiles(String directory) throws IOException {

		List<File> directories = new ArrayList<File>();
		File[] files = new File(directory).listFiles();
		for (File file : files) {
			if (file.isFile()) {
				directories.add(file);
			}
		}

		return directories;
	}

}
