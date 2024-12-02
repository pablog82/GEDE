package com.babelgroup.gede.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.*;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.babelgroup.gede.dto.DatosAltaDocumentoRequest;
import com.babelgroup.gede.dto.DatosAltaExpedienteRequest;
import com.babelgroup.gede.dto.DocumentoResponse;
import com.babelgroup.gede.dto.ExpedienteResponse;
import com.babelgroup.gede.dto.Metadato;
import com.babelgroup.gede.dto.RespuestaGenerica;
import com.babelgroup.gede.dto.TicketRequest;
import com.babelgroup.gede.dto.TicketResponse;
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
@Log4j2
public class GestorDocumentalServiceImpl implements GestorDocumentalService {

	private static final String DOCUMENTOS_DE_EXPEDIENTES_ENCONTRADOS = "-- Documentos de expedientes encontrados: {0}";

	private static final String PASO_1_LISTAR_CARPETAS_DE_EXPEDIENTES = "PASO 1 - Listar carpetas de expedientes";

	private static final String PASO_2_LISTAR_FICHEROS_EN_LA_CARPETA = "PASO 2 - Listar ficheros en la carpeta: {0}";

	private static final String PASO_3_PROCESANDO_FICHERO_FICHEROS_EN_LA_CARPETA = "PASO 3 - Procesando ficheros en la carpeta: {0} -  Fichero: {1}";

	private static final String CARPETAS_DE_EXPEDIENTES_ENCONTRADAS = "--- Carpetas de expedientes encontradas: {0}";

	private static final String INICIO_DEL_PROCESO_DE_DOCUMENTOS = "-- INICIO DEL PROCESO DE DOCUMENTOS ---";

	@Value("${dir.expedientes}")
	private String dirExpedientes;

	@Value("${gede.usuario}")
	private String gedeUsuario;

	@Value("${gede.password}")
	private String gedePassword;

	@Value("${gede.organismo}")
	private String gedeOrganismo;

	@Value("${gede.api.ticket}")
	private String gedeApiTicket;

	@Value("${gede.api.ticket.validar}")
	private String gedeApiTicketValidar;

	@Value("${gede.api.binarios}")
	private String gedeApiBinarios;

	@Value("${gede.api.documentos.crear}")
	private String gedeApiDocumentosCrear;

	@Value("${gede.api.expedientes.crear}")
	private String gedeApiExpedientesCrear;

	@Value("${gede.alta.documento.deposito}")
	private String deposito;

	@Value("${gede.alta.documento.tipoDocumental}")
	private String tipoDocumentalDocumento;

	@Value("${gede.alta.expediente.tipoDocumental}")
	private String tipoDocumentalExpediente;

	@Value("${gede.alta.documento.formato}")
	private String formato;

	@Value("${gede.alta.documento.accesibilidadDocumento}")
	private String accesibilidadDocumento;

	@Value("${gede.alta.documento.estadoElaboracion}")
	private String estadoElaboracion;

	@Value("${gede.alta.documento.origen}")
	private String origen;

	@Value("${gede.alta.documento.organo}")
	private String organo;

	@Value("${gede.alta.documento.unidadProductora}")
	private String unidadProductora;

	@Value("${gede.alta.documento.tipoDocumento}")
	private String tipoDocumento;

	@Value("${gede.alta.documento.serie}")
	private String serieDocumenal;

	@Value("${gede.api.documentos.crear.nombre}")
	private String nombreDocumentoHistorico;

	@Value("${gede.api.expedientes.crear.nombre}")
	private String nombreExpedienteHistorico;

	@Value("${gede.api.expedientes.crear.numero}")
	private String numeroExpedienteHistorico;

	@Autowired
	private FirmaService firmaService;
	
	@Autowired
	private RegistroService registroService;

	@Lazy
	@Autowired
	private RestTemplate restTemplate;

	private TicketResponse ticketResponse;

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

			if (CollectionUtils.isNotEmpty(listadoDirectoriosExpedientes)) {
				log.info(MessageFormat.format(CARPETAS_DE_EXPEDIENTES_ENCONTRADAS,
						listadoDirectoriosExpedientes.toString()));
				// 2. Listar fichero por carpetas
				for (File directorioExpediente : listadoDirectoriosExpedientes) {
					listarFicherosExpedientes(directorioExpediente);
				}
			} else {
				log.info("--- No se han encontrado carpetas que procesar");
			}

		} catch (IOException e) {
			log.info("--- Error al listar los expedientes: " + e.getMessage());
		} catch (GeneralException e) {
			log.info(e);
		}

		log.info("-- FIN DEL PROCESO DE DOCUMENTOS ---");
	}

	/**
	 * Listar ficheros expedientes.
	 *
	 * @param directorioExpediente the directorio expediente
	 * @throws GeneralException the general exception
	 */
	private void listarFicherosExpedientes(File directorioExpediente) throws GeneralException {
		log.info(MessageFormat.format(PASO_2_LISTAR_FICHEROS_EN_LA_CARPETA, directorioExpediente));

		String expediente = directorioExpediente.getName();

		try {
			List<File> listadoFicherosExpediente = listFiles(directorioExpediente.getAbsolutePath());
			if (CollectionUtils.isNotEmpty(listadoFicherosExpediente)) {

				log.info(MessageFormat.format(DOCUMENTOS_DE_EXPEDIENTES_ENCONTRADOS,
						listadoFicherosExpediente.toString()));

				// 3. Procesar fichero
				for (File ficheroExpediente : listadoFicherosExpediente) {

					log.info(MessageFormat.format(PASO_3_PROCESANDO_FICHERO_FICHEROS_EN_LA_CARPETA,
							directorioExpediente, ficheroExpediente));

					String nombreDocumento = ficheroExpediente.getName();

					// 3.1. Comprobar si está procesado

					List<Registro> registros = registroService.findByExpedienteAndDocumento(expediente,
							nombreDocumento);

					if (registros.isEmpty()) {

						// Obetener el numero de expediente
						String nombreFichero = ficheroExpediente.getName();

						String[] split = nombreFichero.split("-");

						String numeroExpediente = split[1];

						// Comprobar si no se creo ya

						List<Registro> registrosExpediente = registroService.findAllByExpediente(numeroExpediente);

						String identificadorExpediente = null;

						String nombreNaturalExpediente = MessageFormat.format(nombreExpedienteHistorico,
								numeroExpediente);

						if (registrosExpediente.isEmpty()) {
							// 3.1.1 Crear expediente
							intentosLlamadaAPI = 0;
							ExpedienteResponse expedienteResponse = crearExpediente(numeroExpediente, numeroExpediente,
									numeroExpediente, numeroExpediente);

							identificadorExpediente = expedienteResponse.getIdentificador();
						} else {
							identificadorExpediente = registrosExpediente.get(0).getIdentificadorExpediente();
						}

						// 3.2 Enviar el documento a @firma y almacenar binario

						intentosLlamadaAPI = 0;

//						RespuestaGenerica respuestaAlmacenarBinario;
//						try {
//							String documentoBase64 = EncodeDecode
//									.encode(Files.readAllBytes(ficheroExpediente.toPath()));
//
//							String firmarJustificante = firmaService.firmarJustificante(documentoBase64);
//
//							File fileFirmaDocumento = File.createTempFile("signef-", ".PDF");
//
//							FileUtils.writeByteArrayToFile(fileFirmaDocumento, EncodeDecode.decode(firmarJustificante));
//
//							// 3.3 Almacenar el fichero firmado
//							respuestaAlmacenarBinario = almacenarBinario(fileFirmaDocumento);
//
//						} catch (GeneralException e) {
//							Registro registro = new Registro(expediente, identificadorExpediente, nombreDocumento,
//									"error", "Error al almacenar el binario: " + e.getMessage(), "ERROR");
//							registroService.insert(registro);
//							throw new GeneralException(e);
//						}
						
						RespuestaGenerica respuestaAlmacenarBinario;
						try {

							// 3.3 Almacenar el fichero firmado
							respuestaAlmacenarBinario = almacenarBinario(ficheroExpediente);

						} catch (GeneralException e) {
							Registro registro = new Registro(expediente, identificadorExpediente, nombreDocumento,
									"error", "Error al almacenar el binario: " + e.getMessage(), "ERROR");
							registroService.insert(registro);
							throw new GeneralException(e);
						}

						intentosLlamadaAPI = 0;

						// 4. Crear documento en GEDE

						DocumentoResponse responseCrearDocumento;
						try {

							Integer contadorDocumento = split.length > 2 && (StringUtils.isNotEmpty(split[2].substring(0, split[2].indexOf("."))))
									? Integer.parseInt(split[2].substring(0, split[2].indexOf(".")))
									: 1;

							responseCrearDocumento = crearDocumento(nombreDocumento,
									respuestaAlmacenarBinario.getIdentificador(), identificadorExpediente,
									numeroExpediente, contadorDocumento);
						} catch (GeneralException e) {
							Registro registro = new Registro(expediente, identificadorExpediente, nombreDocumento, "0",
									"Error al crear el documento: " + e.getMessage(), "ERROR");
							registroService.insert(registro);
							throw new GeneralException(e);
						}

						// 5 Registar en base de datos
						Registro registro = new Registro(expediente, identificadorExpediente, nombreDocumento,
								responseCrearDocumento.getIdentificador(), "Documento almacenado",
								"OK - [firmado: OK]");
						registroService.insert(registro);
					} else {
						log.info("Ya se ha procesado el documento");
					}

				}
			} else {
				log.info("--- No se han encontrado documentos que procesar");
			}

		} catch (IOException e) {
			throw new GeneralException("Error al listar los ficheros de la carperta " + directorioExpediente, e);
		} 
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
			log.info("obtenerTicket() - Ticket obtenido:  {}", Objects.requireNonNull(this.ticketResponse).getTicket());
		} else if (intentosLlamadaAPI < 3) {
			intentosLlamadaAPI++;
			log.info("obtenerTicket() - Error al llamar a la API - Reintento {}", intentosLlamadaAPI);
		} else {
			throw new GeneralException("obtenerTicket() - Error al llamar a la API - Superado número de reintentos: "
					+ response.getStatusCode().toString());
		}
	}

	/**
	 * Validar ticket.
	 *
	 * @return true, if successful
	 */
	private boolean validarTicket() {
		// si la fecha de caducidad del ticket que tenemos es pasado ya no sirve el
		// Ticket
		if (this.ticketResponse == null) {
			return false;
		}
		return !this.ticketResponse.getCaducidad().after(new Date());
	}

	/**
	 * Cabecera con ticket.
	 *
	 * @param contentType the content type
	 * @return the http headers
	 * @throws GeneralException the general exception
	 */
	private HttpHeaders cabeceraConTicket(MediaType contentType) throws GeneralException {
		if (!validarTicket()) {
			obtenerTicket();
		}
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", this.ticketResponse.getTicket());
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
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
				RespuestaGenerica r = response.getBody();
				log.info("almacenarBinario() - Binario almacenado:  {}", Objects.requireNonNull(r).getIdentificador());
				return r;
			} else if (HttpStatus.UNAUTHORIZED.equals(response.getStatusCode()) && intentosLlamadaAPI < 3) {
				intentosLlamadaAPI++;
				log.info("almacenarBinario() - Error al llamar a la API - Reintento {}", intentosLlamadaAPI);
				return almacenarBinario(fichero);
			} else {
				throw new GeneralException(
						"almacenarBinario() - Error al llamar a la API - Superado número de reintentos: "
								+ response.getStatusCode().toString());
			}
		} catch (IOException | RestClientException e) {
			throw new GeneralException("almacenarBinario() - Error al leer el fichero a almacenar", e);
		}
	}

	private ExpedienteResponse crearExpediente(String nombreExpediente, String idExpediente, String mes, String anyo)
			throws GeneralException {
		HttpHeaders headers = cabeceraConTicket(MediaType.APPLICATION_JSON);
		HttpEntity<DatosAltaExpedienteRequest> requestEntity;

		DatosAltaExpedienteRequest datosAlta = generarDatosAltaExpediente(nombreExpediente, idExpediente, mes, anyo);

		requestEntity = new HttpEntity<>(datosAlta, headers);

		try {
			ResponseEntity<ExpedienteResponse> response = restTemplate.exchange(gedeApiExpedientesCrear,
					HttpMethod.POST, requestEntity, new ParameterizedTypeReference<ExpedienteResponse>() {
					});
			if (HttpStatus.CREATED.equals(response.getStatusCode())) {

				ExpedienteResponse r = response.getBody();
				log.info("crearExpediente() - Expediente creado  id: {}", Objects.requireNonNull(r).getIdentificador());
				return r;
			} else if (HttpStatus.UNAUTHORIZED.equals(response.getStatusCode()) && intentosLlamadaAPI < 3) {
				intentosLlamadaAPI++;
				log.info("crearExpediente() - Error al llamar a la API - Reintento {}", intentosLlamadaAPI);
				return crearExpediente(nombreExpediente, idExpediente, mes, anyo);
			} else {
				throw new GeneralException(
						"crearExpediente() - Error al llamar a la API - Superado número de reintentos: "
								+ response.getStatusCode().toString());
			}
		} catch (RestClientException e) {
			throw new GeneralException("crearExpediente() - Error al almacenar el expediente: " + e.getMessage(), e);
		}
	}

	/**
	 * Crear documento.
	 *
	 * @param nombreDocumento the nombre documento
	 * @param idBinario       the id binario
	 * @return the documento response
	 * @throws GeneralException the general exception
	 */
	private DocumentoResponse crearDocumento(String nombreDocumento, String idBinario, String identificadorExpediente,
			String numeroExpediente, Integer contadorDocumento) throws GeneralException {
		HttpHeaders headers = cabeceraConTicket(MediaType.APPLICATION_JSON);
		HttpEntity<DatosAltaDocumentoRequest> requestEntity;

		String nombreNaturalDocumento = MessageFormat.format(nombreDocumentoHistorico, numeroExpediente,
				contadorDocumento);

		DatosAltaDocumentoRequest datosAlta = generarDatosAltaDocumento(nombreDocumento, idBinario,
				identificadorExpediente, nombreNaturalDocumento);

		requestEntity = new HttpEntity<>(datosAlta, headers);

		try {
			ResponseEntity<DocumentoResponse> response = restTemplate.exchange(gedeApiDocumentosCrear, HttpMethod.POST,
					requestEntity, new ParameterizedTypeReference<DocumentoResponse>() {
					});
			if (HttpStatus.CREATED.equals(response.getStatusCode())) {

				DocumentoResponse r = response.getBody();
				log.info("crearDocumento() - Documento creado  id: {}", Objects.requireNonNull(r).getIdentificador());
				return r;
			} else if (HttpStatus.UNAUTHORIZED.equals(response.getStatusCode()) && intentosLlamadaAPI < 3) {
				intentosLlamadaAPI++;
				log.info("crearDocumento() - Error al llamar a la API - Reintento {}", intentosLlamadaAPI);
				return crearDocumento(nombreDocumento, idBinario, identificadorExpediente, numeroExpediente,
						contadorDocumento);
			} else {
				throw new GeneralException(
						"crearDocumento() - Error al llamar a la API - Superado número de reintentos: "
								+ response.getStatusCode().toString());
			}
		} catch (RestClientException e) {
			throw new GeneralException("crearDocumento() - Error al almacenar el documento: " + e.getMessage(), e);
		}
	}

	private DatosAltaExpedienteRequest generarDatosAltaExpediente(String nombreExpediente, String idExpediente,
			String mes, String anyo) {
		DatosAltaExpedienteRequest datosAlta = new DatosAltaExpedienteRequest();
		datosAlta.setDeposito(deposito);
		datosAlta.setTipoDocumental(tipoDocumentalExpediente);
		datosAlta.setBorrador(false);

		// Metadatos
		List<Metadato> metadatos = new ArrayList<Metadato>();

		String numeroExpediente = MessageFormat.format(numeroExpedienteHistorico, idExpediente);

		metadatos.add(new Metadato("nombreNaturalExpediente", new String[] { nombreExpediente }));
		metadatos.add(new Metadato("formatoExpediente", new String[] { formato }));
		metadatos.add(new Metadato("idoc:field_22_9", new String[] { organo }));
		metadatos.add(new Metadato("idoc:field_22_4", new String[] { gedeOrganismo }));
		metadatos.add(new Metadato("idExpediente", new String[] { numeroExpediente }));
		metadatos.add(new Metadato("idoc:field_22_13", new String[] { numeroExpediente }));
		metadatos.add(new Metadato("serieExpediente", new String[] { serieDocumenal }));
		metadatos.add(new Metadato("codigoExpediente", new String[] { numeroExpediente }));

		datosAlta.setMetadatos(metadatos);
		return datosAlta;
	}

	/**
	 * Generar datos alta.
	 *
	 * @param nombreDocumento the nombre documento
	 * @param idBinario       the id binario
	 * @return the datos alta documento request
	 */
	private DatosAltaDocumentoRequest generarDatosAltaDocumento(String nombreDocumento, String idBinario,
			String identificadorExpediente, String nombreNaturalDocumento) {
		DatosAltaDocumentoRequest datosAlta = new DatosAltaDocumentoRequest();
		datosAlta.setDeposito(deposito);
		datosAlta.setIdentificadorExpediente(identificadorExpediente);
		datosAlta.setIdentificadorBinario(idBinario);
		datosAlta.setFormatoBinario("PDF");
		datosAlta.setTipoDocumental(tipoDocumentalDocumento);
		datosAlta.setBorrador(false);
		datosAlta.setTrabajo(true);

		// Metadatos
		List<Metadato> metadatos = new ArrayList<Metadato>();
		metadatos.add(new Metadato("serieDocumento", new String[] { serieDocumenal }));
		metadatos.add(new Metadato("nombreFichero", new String[] { nombreDocumento }));
		metadatos.add(new Metadato("idoc:field_23_1", new String[] { tipoDocumento }));
		metadatos.add(new Metadato("idiomaDocumento", new String[] { "es" }));
		metadatos.add(new Metadato("fechaAltaDocumento", new String[] { Instant.now().toString() }));
		metadatos.add(new Metadato("idoc:field_23_13", new String[] { unidadProductora }));
		metadatos.add(new Metadato("idoc:field_23_14", new String[] { "PDF" }));
		metadatos.add(new Metadato("idoc:field_23_3", new String[] { organo }));
		metadatos.add(new Metadato("idoc:field_23_4", new String[] { origen }));
		metadatos.add(new Metadato("idoc:field_23_6", new String[] { estadoElaboracion }));
		metadatos.add(new Metadato("accesibilidadDocumento", new String[] { accesibilidadDocumento }));
		metadatos.add(new Metadato("formatoDocumento", new String[] { formato }));
		metadatos.add(new Metadato("nombreNaturalDocumento", new String[] { nombreNaturalDocumento }));
		metadatos.add(new Metadato("idoc:field_23_8",
				new String[] { "http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e" }));

		datosAlta.setMetadatos(metadatos);
		return datosAlta;
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
		File diretoryFile = new File(directory);
		if (diretoryFile.isDirectory()) {
			directories.add(diretoryFile);
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
		for (File file : Objects.requireNonNull(files)) {
			if (file.isFile()) {
				directories.add(file);
			}
		}
		return directories;
	}
}
