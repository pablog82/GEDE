package com.babelgroup.gede.service.impl;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.babelgroup.gede.service.FirmaService;
import com.babelgroup.gede.util.EncodeDecode;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.security.ExternalSignatureContainer;
import com.itextpdf.text.pdf.security.MakeSignature;

@Service
public class EmptySignature {
	
	@Autowired
	private FirmaService firmaService;
	
    public void firmar(File documento) throws Exception {
        //String src = "C:\\DevTools\\gede\\documento.pdf";
//        String between = "C:\\DevTools\\gede\\documento_bt.pdf";
//        String dest = "C:\\DevTools\\gede\\documento_firmado.pdf";
        String fieldName = "sign";
        
        String src = documento.getAbsolutePath().toUpperCase();
        String between= src.replace(".PDF", UUID.randomUUID().toString() + "_BT.PDF");
        String dest= src.replace(".PDF", UUID.randomUUID().toString() + "_DEST.PDF");

        emptySignature(src, between, fieldName);
        
		String justificanteBase64 = EncodeDecode.encode(FileUtils.readFileToByteArray(new File(between)));

		String firmarJustificante = firmaService.firmarJustificante(justificanteBase64);

        //byte[] bytesFirma = FileUtils.readFileToByteArray(firma);
        byte[] signatureBytes = Base64.decodeBase64(firmarJustificante.getBytes());

        createSignature(between, dest, fieldName, signatureBytes);
    }

    private void emptySignature(String src, String dest, String fieldname)
            throws IOException, DocumentException, GeneralSecurityException {
        PdfReader reader = new PdfReader(src);
        FileOutputStream os = new FileOutputStream(dest);
        PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0');

        Calendar cal = GregorianCalendar.getInstance();
        cal.add(Calendar.MINUTE, 10);

        PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
        appearance.setVisibleSignature(new Rectangle(36, 748, 144, 780), 1, fieldname);
//		appearance.setReason("Nice");
//		appearance.setLocation("Delhi");
//		appearance.setSignDate(cal);

        ExternalSignatureContainer external = new CustomExternalSignatureContainer(new byte[] {});
        MakeSignature.signExternalContainer(appearance, external, 8192);

        os.close();
        reader.close();
    }

    private static void createSignature(String src, String dest, String fieldname, byte[] signature)
            throws IOException, DocumentException, GeneralSecurityException {


        PdfReader reader = new PdfReader(src);
        FileOutputStream os = new FileOutputStream(dest);
		ExternalSignatureContainer external = new CustomExternalSignatureContainer(signature);
        MakeSignature.signDeferred(reader, fieldname, os, external);

        reader.close();
        os.close();
    }



    public static class CustomExternalSignatureContainer implements ExternalSignatureContainer {

        protected byte[] sig;

        public CustomExternalSignatureContainer(byte[] sig) {
            this.sig = sig;
        }

        public byte[] sign(InputStream is) {
            return sig;
        }

        public void modifySigningDictionary(PdfDictionary signDic) {
        }

    }
}