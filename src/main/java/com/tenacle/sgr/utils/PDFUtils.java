/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tenacle.sgr.utils;

import com.tenacle.sgr.entities.Ticket;
import com.tenacle.sgr.pdf.Client;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author samuel
 */
public class PDFUtils {

//    static Client client = new Client("swainaina", "c1ea5e8a9cf2d944e31b8d6bc5035f30");
    static Client client = new Client("qubitlimited", "855cc296c90c112554de3394d685f879");

    public static File createPDF(Ticket ticket, String html) {
        File pdf = new File("/var/www/pdf/" + ticket.getId() + ".pdf");
        if (!pdf.exists()) {
            try {
                client.convertHtml(html, new FileOutputStream(pdf));
                return pdf;
            } catch (FileNotFoundException ex) {
                Logger.getLogger(PDFUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return pdf;
    }
}
