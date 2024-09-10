package com.babelgroup.gede.service.impl;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.security.ExternalSignatureContainer;
import com.itextpdf.text.pdf.security.MakeSignature;

public class EmptySignature {

    public static void emptySignature(String src, String dest, String fieldname)
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

    public static void createSignature(String src, String dest, String fieldname, byte[] signature)
            throws IOException, DocumentException, GeneralSecurityException {

        byte[] firma = FileUtils.readFileToByteArray(new File("C:\\DevTools\\gede\\firmabase64.txt"));

        byte[] signatureBytes = Base64.decodeBase64(firma);

        PdfReader reader = new PdfReader(src);
        FileOutputStream os = new FileOutputStream(dest);
        ExternalSignatureContainer external = new CustomExternalSignatureContainer(signatureBytes);
        MakeSignature.signDeferred(reader, fieldname, os, external);

        reader.close();
        os.close();
    }

    public static void main(String[] args) throws Exception {
        String src = "C:\\DevTools\\gede\\documento.pdf";
        String between = "C:\\DevTools\\gede\\documento_bt.pdf";
        String dest = "C:\\DevTools\\gede\\documento_firmado.pdf";
        String fieldName = "sign";

        emptySignature(src, between, fieldName);

        byte[] firma = FileUtils.readFileToByteArray(new File("C:\\DevTools\\gede\\firmabase64.txt"));
        byte[] signatureBytes = Base64.decodeBase64(firma);

        createSignature(between, dest, fieldName, signatureBytes);
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