package com.babelgroup.gede.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
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

import com.babelgroup.gede.dto.DocumentoResponse;
import com.babelgroup.gede.dto.FirmaResponse;
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

	/** The dir expedientes. */
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

	/** The firma service. */
	@Autowired
	private FirmaService firmaService;

	/** The registro service. */
	@Autowired
	private RegistroService registroService;

	/** The rest template. */
	@Autowired
	private RestTemplate restTemplate;

	private TicketResponse ticketResponse;

	/**
	 * Subir documentos.
	 *
	 * @throws GeneralException the general exception
	 */
	@Override
	public void subirDocumentos() throws GeneralException {
		// TODO 1. Leer carpetas

		List<File> listadoDirectoriosExpedientes = null;

		try {
			log.info("PASO 1 - Listar carpetas de expedientes");
			listadoDirectoriosExpedientes = listDirectories(dirExpedientes);
//			listadoDirectoriosExpedientes.forEach(System.out::println);
			log.info("-- Carpetas de expedientes encontradas: " + listadoDirectoriosExpedientes.toString());
		} catch (IOException e) {
			throw new GeneralException("Error al listar los expedientes", e);
		}

		// TODO 2. Leer fichero por carpetas
		if (listadoDirectoriosExpedientes != null && !listadoDirectoriosExpedientes.isEmpty()) {
			log.info("PASO 2 - Listar ficheros en cada carpeta");

			for (File directorioExpediente : listadoDirectoriosExpedientes) {

				String expediente = directorioExpediente.getName();

				try {
					log.info("PASO 3 - Listar ficheros en carpeta de expedientes: " + directorioExpediente);
					List<File> listadoFicherosExpediente = listFiles(directorioExpediente.getAbsolutePath());
//					listadoFicherosExpediente.forEach(System.out::println);
					if (listadoFicherosExpediente != null && !listadoFicherosExpediente.isEmpty()) {
						log.info("-- Documentos de expedientes encontrados: " + listadoFicherosExpediente.toString());
						for (File ficheroExpediente : listadoFicherosExpediente) {

							String documento = ficheroExpediente.getName();

							// TODO 3. Comporbar si está procesado

							List<Registro> registros = registroService.findByExpedienteAndDocumento(expediente,
									documento);
//							registros.forEach(System.out::println);

							// TODO Obtener el ticker
							// obtenerTicket();

//							getTiposDocumentales();

							if (registros.isEmpty()) {

								// TODO 4. Almacenar binario

								// RespuestaGenerica respuestaAlmacenarBinario =
								// almacenarBinario(ficheroExpediente);

								// TODO 5. Crear documento

								crearDocumento(ficheroExpediente, expediente);

								// TODO 6. Firmar

								String justificanteBase64 = EncodeDecode
										.encode(Files.readAllBytes(ficheroExpediente.toPath()));

//								String firmarJustificante = firmaService.firmarJustificante(justificanteBase64);

//								FileUtils.writeByteArrayToFile(
//										new File(ficheroExpediente.getAbsolutePath().toUpperCase().replace(".PDF",
//												UUID.randomUUID().toString() + "_FIRMADO.PDF")),
//										EncodeDecode.decode(firmarJustificante));

								// TODO 7 Cerrar documento

								cerrarDocumento(ficheroExpediente, expediente);

								// TODO 8 Registar en base de datos
								Registro registro = new Registro(expediente, documento, null, null, null);
								registroService.insert(registro);
							} else {
								log.info("Ya se ha procesado el documento");
							}

						}
					}

				} catch (IOException e) {
					throw new GeneralException("Error al listar los ficheros de la carperta " + directorioExpediente,
							e);
				}
			}
		}

	}

	private void obtenerTicket() {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<TicketRequest> requestEntity = new HttpEntity<>(
				new TicketRequest(gedeUsuario, gedeOrganismo, gedePassword), headers);

		ResponseEntity<TicketResponse> response = restTemplate.exchange(gedeApiTicket, HttpMethod.POST, requestEntity,
				new ParameterizedTypeReference<TicketResponse>() {
				});

		if (HttpStatus.CREATED.equals(response.getStatusCode())) {
			this.ticketResponse = response.getBody();
			log.info(ticketResponse);
		} else {
			log.info(response);
		}
	}

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
		return false;
	}

	// llamada de prueba para usar el ticket (BORRAR)
	private void getTiposDocumentales() {

		HttpHeaders headers = cabeceraConTicket();

		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

		ResponseEntity<String> response = restTemplate.exchange(
				"https://pre-plataforma.sevilla.org/gede-rest/api/v1/tipoDocumentales?operacion=ESCRITURA&formato=PAPEL&tipoEntidad=EXPEDIENTE",
				HttpMethod.GET, requestEntity, new ParameterizedTypeReference<String>() {
				});

		if (HttpStatus.OK.equals(response.getStatusCode())) {

			log.info(response);
		} else {
			log.info(response);
		}
	}

	private HttpHeaders cabeceraConTicket() {

		if (!validarTicket()) {
			obtenerTicket();
		}

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", this.ticketResponse.getTicket());
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}

	/**
	 * Almacenar binario.
	 *
	 * @param fichero    the fichero
	 * @param expediente the expediente
	 * @return the respuesta generica
	 */
	private RespuestaGenerica almacenarBinario(File fichero) {
		// HttpHeaders headers = cabeceraConTicket();

		if (!validarTicket()) {
			obtenerTicket();
		}

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", this.ticketResponse.getTicket());
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

		HttpEntity<byte[]> requestEntity;
		try {
			requestEntity = new HttpEntity<>(FileUtils.readFileToByteArray(fichero), headers);
			ResponseEntity<RespuestaGenerica> response = restTemplate.exchange(gedeApiBinarios, HttpMethod.POST,
					requestEntity, new ParameterizedTypeReference<RespuestaGenerica>() {
					});

			if (HttpStatus.CREATED.equals(response.getStatusCode())) {
				RespuestaGenerica generica = response.getBody();
				log.info(generica);
			} else {
				// TODO
				log.info(response);
			}
		} catch (IOException e) {
			// TODO
		}

		return null;
	}

	/**
	 * Crear documento.
	 *
	 * @param fichero    the fichero
	 * @param expediente the expediente
	 * @return the documento response
	 */
	private DocumentoResponse crearDocumento(File fichero, String expediente) {
//		HttpEntity<DatosAltaDocumentoRequest> requestEntity = new HttpEntity<>(certificacionSedeDTO, headers);
//
//		ResponseEntity<RespuestaAuditoriaDTO> response = restTemplate.exchange(coreEndpointAceptarORechazarNotificacion,
//				HttpMethod.POST, requestEntity, new ParameterizedTypeReference<RespuestaAuditoriaDTO>() {
//				});
		return null;
	}

	private RespuestaGenerica cerrarDocumento(File fichero, String expediente) {
//		HttpEntity<CertificacionSedeDTO> requestEntity = new HttpEntity<>(certificacionSedeDTO, headers);
//
//		ResponseEntity<RespuestaAuditoriaDTO> response = restTemplate.exchange(coreEndpointAceptarORechazarNotificacion,
//				HttpMethod.POST, requestEntity, new ParameterizedTypeReference<RespuestaAuditoriaDTO>() {
//				});

		return null;
	}

	private FirmaResponse agregarFirmantes(File fichero, String expediente) {
//		HttpEntity<DatosAltaFirmantesRequest> requestEntity = new HttpEntity<>(certificacionSedeDTO, headers);
//
//		ResponseEntity<RespuestaAuditoriaDTO> response = restTemplate.exchange(coreEndpointAceptarORechazarNotificacion,
//				HttpMethod.POST, requestEntity, new ParameterizedTypeReference<RespuestaAuditoriaDTO>() {
//				});

		return null;
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
