/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tenacle.sgr.utils.exec;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import org.xhtmlrenderer.pdf.ITextRenderer;

public class CreatePdf {

    public static void main(String[] args) {
        String fileName = UUID.randomUUID().toString();
        FileOutputStream os;
        String html = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body style=\"background-color:powderblue;\">\n" +
                "\n" +
                "<h1>This is a heading</h1>\n" +
                "<p>This is a paragraph.</p>\n" +
                "\n" +
                "</body>\n" +
                "</html>";
        try {
            final File outputFile = File.createTempFile(fileName, ".pdf");
            os = new FileOutputStream(outputFile);
            generatePdf(html, os, outputFile.toString());

            ByteArrayOutputStream bas = new ByteArrayOutputStream();
            bas = convertPDFToByteArrayOutputStream(outputFile.toString());
            bas.writeTo(os);
            os.flush();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generatePdf(String html, FileOutputStream baos, String fileName) throws Exception {
        try {

            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(baos, false);
            renderer.finishPDF();
            System.out.println("PDF created successfully");

            ByteArrayOutputStream bas = new ByteArrayOutputStream();
            bas.writeTo(baos);
            baos.flush();

        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) { /*ignore*/ }
            }
        }
    }

    private static ByteArrayOutputStream convertPDFToByteArrayOutputStream(String fileName) {

        InputStream inputStream = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {

            inputStream = new FileInputStream(fileName);
            byte[] buffer = new byte[1024];
            baos = new ByteArrayOutputStream();

            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return baos;
    }
}
