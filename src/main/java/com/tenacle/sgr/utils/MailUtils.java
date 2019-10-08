/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tenacle.sgr.utils;

import com.tenacle.sgr.entities.Ticket;
import java.io.File;
import net.sargue.mailgun.Configuration;
import net.sargue.mailgun.Mail;

/**
 *
 * @author samuel
 */
public class MailUtils {

    static Configuration configuration = new Configuration()
            .domain("mailgun.org")
            .apiKey("key-a9509fe01ef47f2fc4c0ed8f838a1377")
            .from("Test account", "postmaster@sandbox3d348869074d41469d100955d865cfaa.mailgun.org");

    /**
     *  
     * Mail settings
     *
     *
     * Catch all email with your default email account
     *
     *
     * POP3 Host Address : mail.tenacle.co.ke SMTP Host Address:
     * mail.tenacle.co.ke Username: tenaclec Password: Au5@i835$
     */
    public static void sendMail(Ticket ticket, String html) {
        Mail.using(configuration)
                .to("samuel.wainaina.kimama@gmail.com")
                .subject("KR - Ticket Details for " + ticket.getCustomer().getFirstName() + " " + ticket.getCustomer().getSecondName())
                .text("Find attached, the ticket information for your trip")
                .multipart()
                .attachment(PDFUtils.createPDF(ticket, html))
                .build()
                .send();
    }
}
