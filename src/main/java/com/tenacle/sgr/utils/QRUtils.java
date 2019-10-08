/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tenacle.sgr.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;

/**
 *
 * @author samuel
 */
public class QRUtils {

    public static void createQR(String ticketID) {
        try {
            File file = new File("/var/www/qr/" + ticketID + ".png");
            file.createNewFile();
            QRCode.from("" + ticketID).to(ImageType.PNG).writeTo(new FileOutputStream(file));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(QRUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(QRUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
