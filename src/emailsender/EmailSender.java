/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emailsender;

import java.util.Properties;
import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author kmacharia<https://github.com/kmacharia>
 */

/**
 *Add javax.mail.jar to your project.
 * You can download it from https://java.net/downloads/javamail/javax.mail.jar
 */

public class EmailSender {
    //Define email configurations
    private static final String SMTP_HOST_NAME = "mail.domain.com";
    private static final String SMTP_AUTH_USER = "sender@domain.com";
    private static final String SMTP_AUTH_PASSWORD = "password";

    public static void main(String args[]) throws Exception {
        EmailSender emailSender = new EmailSender();
        String[] recipientsList = {"receiver@domain.com"};
        String emailSubject = "Subject Header";
        String emailContent = "This is the body of the email";
        emailSender.sendEmail(recipientsList, emailSubject, emailContent, SMTP_AUTH_USER);
        System.out.println("Email(s) sent successfully");
    }

    public void sendEmail(String[] recipientsList, String emailSubject,String emailContent, String senderAddress)
            throws MessagingException, AuthenticationFailedException {
        
        //Set the host smtp address
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST_NAME);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        Authenticator authenticator = new CredentialsAuthenticator();
        Session session = Session.getDefaultInstance(props, authenticator);

        session.setDebug(false);

        //Create Email
        Message emailMessage = new MimeMessage(session);
        InternetAddress senderInternetAddress = new InternetAddress(senderAddress);
        emailMessage.setFrom(senderInternetAddress);

        InternetAddress[] recipientInternetAddress = new InternetAddress[recipientsList.length];
        for (int i = 0; i < recipientsList.length; i++) {
            recipientInternetAddress[i] = new InternetAddress(recipientsList[i]);
        }
        
        emailMessage.setRecipients(Message.RecipientType.TO, recipientInternetAddress);
        emailMessage.setSubject(emailSubject);
        emailMessage.setContent(emailContent, "text/plain");
        Transport.send(emailMessage);
    }

    private class CredentialsAuthenticator extends javax.mail.Authenticator {

        public PasswordAuthentication getPasswordAuthentication() {
            String username = SMTP_AUTH_USER;
            String password = SMTP_AUTH_PASSWORD;
            return new PasswordAuthentication(username, password);
        }
    }
}
