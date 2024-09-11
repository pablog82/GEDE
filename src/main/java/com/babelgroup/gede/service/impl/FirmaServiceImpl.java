package com.babelgroup.gede.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.common.ConfigurationConstants;
import org.apache.xml.security.exceptions.Base64DecodingException;
import org.apache.xml.security.utils.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.babelgroup.gede.exception.GeneralException;
import com.babelgroup.gede.service.FirmaService;
import com.babelgroup.gede.util.EncodeDecode;
import com.babelgroup.gede.util.afirma.ClientPasswordCallback;
import com.babelgroup.gede.util.afirma.DSSSignature;
import com.babelgroup.gede.util.afirma.DSSSignatureService;

import lombok.extern.log4j.Log4j2;

/**
 * The Class FirmaServiceImple.
 */
@SuppressWarnings("deprecation")
@Service("FirmaServiceImpl")

/** The Constant log. */
@Log4j2
public class FirmaServiceImpl implements FirmaService {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(FirmaServiceImpl.class);

	/** The afirma ws endpoint. */
	@Value("${afirma.ws.endpoint}")
	private String afirmaWsEndpoint;

	/** The afirma username. */
	@Value("${afirma.ws.username}")
	private String afirmaUsername;

	/** The afirma password. */
	@Value("${afirma.ws.password}")
	private String afirmaPassword;

	/** The afirma key name. */
	@Value("${afirma.ws.keyname}")
	private String afirmaKeyName;

	/** The afirma claimed identity. */
	@Value("${afirma.ws.claimedidentity}")
	private String afirmaClaimedIdentity;

	/** The afirma hashalgorithm pdf. */
	@Value("${afirma.ws.hashalgorithmpdf}")
	private String afirmaHashalgorithmPdf;

	/** The afirma signature form pdf. */
	@Value("${afirma.ws.signatureformpdf}")
	private String afirmaSignatureFormPdf;

	/** The afirma signature type pdf. */
	@Value("${afirma.ws.signaturetypepdf}")
	private String afirmaSignatureTypePdf;

	/**
	 * Firmar justificante.
	 *
	 * @param justificanteRecibido the justificante recibido
	 * @return the string
	 * @throws GeneralException the general exception
	 */
	@Override
	public String firmarJustificante(String justificanteRecibido) throws GeneralException {
		log.info("Inicio firmarJustificante con el sello de AESA");
		byte[] resultadoEX = null;
		String justificanteFirmado = "";

		DSSSignatureService ss;
		try {
			ss = new DSSSignatureService(new URL(afirmaWsEndpoint), DSSSignatureService.DSSSIGNATURESERVICEQNAMEQ);

			DSSSignature port = ss.getDSSAfirmaSign();

			// Realizamos la autenticación
			Client client = ClientProxy.getClient(port);
			Endpoint cxfEndpoint = client.getEndpoint();
			Map<String, Object> outProps = new HashMap<>();

			// Se deshabilita el parametro CHUNKED para que se mande automáticamente el
			// content-length
			HTTPConduit httpConduit = (HTTPConduit) client.getConduit();
			HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
			httpClientPolicy.setAllowChunking(false);
			httpConduit.setClient(httpClientPolicy);

			try {
				SSLContext ctx = SSLContext.getInstance("TLS");
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			TLSClientParameters params = httpConduit.getTlsClientParameters();

			if (params == null) {
				params = new TLSClientParameters();
				httpConduit.setTlsClientParameters(params);
			}

			params.setTrustManagers(new TrustManager[] { new X509TrustManager() {

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					// TODO Auto-generated method stub

				}

				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					// TODO Auto-generated method stub

				}
			} });

			params.setDisableCNCheck(true);

			outProps.put(ConfigurationConstants.ACTION, "UsernameToken Timestamp");
			outProps.put(ConfigurationConstants.PASSWORD_TYPE, "PasswordText");
			outProps.put(ConfigurationConstants.USER, afirmaUsername);
			outProps.put("password", afirmaPassword);
			outProps.put(ConfigurationConstants.TTL_TIMESTAMP, "300");
			outProps.put(ConfigurationConstants.PW_CALLBACK_REF,
					new ClientPasswordCallback(afirmaUsername, afirmaPassword));
			WSS4JOutInterceptor wssOut = new WSS4JOutInterceptor(outProps); // request
			cxfEndpoint.getOutInterceptors().add(wssOut);

            String signDssXML ="<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
					"<dss:SignRequest Profile=\"urn:afirma:dss:1.0:profile:XSS\" " +
					                 "xmlns:dss=\"urn:oasis:names:tc:dss:1.0:core:schema\" xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\" " + 
					                 "xmlns:afxp=\"urn:afirma:dss:1.0:profile:XSS:schema\" xmlns:ades=\"urn:oasis:names:tc:dss:1.0:profiles:AdES:schema#\" " +
					                 "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
					                 "xmlns:cmism=\"http://docs.oasis-open.org/ns/cmis/messaging/200908/\" " +
					                 "xmlns:sigpol=\"urn:oasis:names:tc:dss-x:1.0:profiles:SignaturePolicy:schema#\" " +
					                 "xmlns:xss=\"urn:oasis:names:tc:dss:1.0:profiles:XSS\">" +
					    "<dss:InputDocuments>"+
					        "<dss:Document>"+
					            "<dss:Base64Data>"+
					        justificanteRecibido +
					            "</dss:Base64Data>"+
					        "</dss:Document>"+
					    "</dss:InputDocuments >"+
					    "<dss:OptionalInputs>"+
					        "<dss:ClaimedIdentity>"+
					            "<dss:Name>"+
					            afirmaClaimedIdentity + 
					            "</dss:Name>"+
					        "</dss:ClaimedIdentity>"+
					        "<dss:KeySelector>"+
					            "<ds:KeyInfo>"+
					                "<ds:KeyName>"+
					                afirmaKeyName + 
					                "</ds:KeyName>"+
					            "</ds:KeyInfo>"+
					        "</dss:KeySelector>"+
					        "<dss:SignatureType>"+ 
					        afirmaSignatureTypePdf + 
					        "</dss:SignatureType>"+
					        "<ades:SignatureForm>" +
					        afirmaSignatureFormPdf +
					        "</ades:SignatureForm>"+
							 "<afxp:HashAlgorithm xmlns:afxp=\"urn:afirma:dss:1.0:profile:XSS:schema\">"
+ afirmaHashalgorithmPdf 
+ "</afxp:HashAlgorithm> "
+ "<afxp:AdditionalDocumentInfo xmlns:afxp='urn:afirma:dss:1.0:profile:XSS:schema'>"
+ "<afxp:DocumentName>docPDF.pdf</afxp:DocumentName>" 
+ "<afxp:DocumentType>pdf</afxp:DocumentType>"
+ "</afxp:AdditionalDocumentInfo>" + 
					        "<dss:IncludeEContent/>"+
					    "</dss:OptionalInputs>"+
					"</dss:SignRequest>";
			
//			java.lang.String signDssXML = 
//					"<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
//							"<dss:SignRequest Profile=\"urn:afirma:dss:1.0:profile:XSS\" " +
//							                 "xmlns:dss=\"urn:oasis:names:tc:dss:1.0:core:schema\" xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\" " + 
//							                 "xmlns:afxp=\"urn:afirma:dss:1.0:profile:XSS:schema\" xmlns:ades=\"urn:oasis:names:tc:dss:1.0:profiles:AdES:schema#\" " +
//							                 "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
//							                 "xmlns:cmism=\"http://docs.oasis-open.org/ns/cmis/messaging/200908/\" " +
//							                 "xmlns:sigpol=\"urn:oasis:names:tc:dss-x:1.0:profiles:SignaturePolicy:schema#\" " +
//							                 "xmlns:xss=\"urn:oasis:names:tc:dss:1.0:profiles:XSS\">" 
//					+ "<dss:InputDocuments xmlns:dss='urn:oasis:names:tc:dss:1.0:core:schema'>"
//						+ "<dss:Document xmlns:dss=\"urn:oasis:names:tc:dss:1.0:core:schema\"> "
//						+ "<dss:Base64Data xmlns:dss=\"urn:oasis:names:tc:dss:1.0:core:schema\">"
//								+ justificanteRecibido 
//							+ "</dss:Base64Data>" 
//						+ "</dss:Document> "
//					+ "</dss:InputDocuments> "
//					+ "<dss:OptionalInputs xmlns:dss=\"urn:oasis:names:tc:dss:1.0:core:schema\">"
//						+ "<dss:ClaimedIdentity xmlns:dss=\"urn:oasis:names:tc:dss:1.0:core:schema\">"
//							+ "<dss:Name xmlns:dss=\"urn:oasis:names:tc:dss:1.0:core:schema\">"
//								+ afirmaClaimedIdentity 
//							+ "</dss:Name>" + ""
//						+ "</dss:ClaimedIdentity> "
//						+ "<dss:KeySelector xmlns:dss=\"urn:oasis:names:tc:dss:1.0:core:schema\"> "
//								+ "<ds:KeyInfo xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\"> "
//									+ "<ds:KeyName>"
//									+ afirmaKeyName 
//									+ "</ds:KeyName> "
//								+ "</ds:KeyInfo>" 
//						+ "</dss:KeySelector> "
//						+ "<dss:SignatureType xmlns:dss=\"urn:oasis:names:tc:dss:1.0:core:schema\">"
//							+ afirmaSignatureTypePdf 
//							+ "</dss:SignatureType> "
//						+ "<ades:SignatureForm xmlns:ades=\"urn:oasis:names:tc:dss:1.0:profiles:AdES:schema#\">"
//							+ afirmaSignatureFormPdf 
//						+ "</ades:SignatureForm> "
//						+ "<afxp:HashAlgorithm xmlns:afxp=\"urn:afirma:dss:1.0:profile:XSS:schema\">"
//								+ afirmaHashalgorithmPdf 
//						+ "</afxp:HashAlgorithm> "
//						+ "<afxp:AdditionalDocumentInfo xmlns:afxp='urn:afirma:dss:1.0:profile:XSS:schema'>"
//							+ "<afxp:DocumentName>docPDF.pdf</afxp:DocumentName>" 
//							+ "<afxp:DocumentType>pdf</afxp:DocumentType>"
//						+ "</afxp:AdditionalDocumentInfo>" 
////						+ "<sigpol:GenerateUnderSignaturePolicy>"
////							+ "<sigpol:SignaturePolicyIdentifier>urn:oid:2.16.724.1.3.1.1.2.1.9</sigpol:SignaturePolicyIdentifier>"
////						+ "</sigpol:GenerateUnderSignaturePolicy>" 
//						+ "<dss:IncludeEContent/>"
//						+ "<afxp:IgnoreGracePeriod/>" 
//					+ " </dss:OptionalInputs> " 
//				+ " </dss:SignRequest>";

			// Se realiza la firma
			justificanteFirmado = port.sign(signDssXML);

//			String xmlRetornoUTF = reemplazarCaracteresISO(justificanteFirmado);

			DocumentBuilderFactory fabricaCreadorDocumento = DocumentBuilderFactory.newInstance();
			fabricaCreadorDocumento.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);

			DocumentBuilder creadorDocumento;

			creadorDocumento = fabricaCreadorDocumento.newDocumentBuilder();

			Document documento = creadorDocumento.parse(new ByteArrayInputStream(justificanteFirmado.getBytes()));
			Element raiz = documento.getDocumentElement();
			NodeList listaContenido2 = raiz.getElementsByTagName("dss:Base64Signature");

			for (int j = 0; j < listaContenido2.getLength(); j++) {
				Node dato = listaContenido2.item(j);
				String valor = dato.getFirstChild().getTextContent();
				resultadoEX = decodificarBase64(valor);
			}
			return EncodeDecode.encode(resultadoEX);
		} catch (SOAPFaultException | SAXException | IOException | ParserConfigurationException e) {
			throw new GeneralException();
		}

	}
	
	

	/**
	 * Decodificar base 64.
	 *
	 * @param contenido the contenido
	 * @return the byte[]
	 */
	public static byte[] decodificarBase64(String contenido) {
		byte[] contenidoDecodificado = null;
		try {
			contenidoDecodificado = Base64.decode(contenido);
		} catch (Base64DecodingException e) {
			LOGGER.error(e.getMessage());
		}
		return contenidoDecodificado;
	}

	/**
	 * Reemplazar caracteres ISO.
	 *
	 * @param input the input
	 * @return the string
	 */
	private static String reemplazarCaracteresISO(String input) {
		// Cadena de caracteres original a sustituir.
		String original = "áàäéèëíìïóòöúùüñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";
		// Cadena de caracteres ASCII que reemplazarán los originales.
		String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
		String output = input;
		for (int i = 0; i < original.length(); i++) {
			// Reemplazamos los caracteres especiales.
			output = output.replace(original.charAt(i), ascii.charAt(i));
		} // for i
		return output;
	}
	
	//https://stackoverflow.com/questions/47505696/itext-pdf-deferred-signing-results-in-pdf-with-invalid-signature

}
