package edu.sru.thangiah.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Service class for sending emails.
 * Utilizes the {@link JavaMailSender} for email sending functionality.
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;
    
    /**
     * Sends an email to the specified recipient with the given subject and message.
     *
     * @param recipientEmail The email address of the recipient.
     * @param subject The subject of the email.
     * @param message The text message to be sent.
     */
    public void sendEmail(String recipientEmail, String subject, String message) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(recipientEmail);
        mail.setSubject(subject);
        mail.setText(message);

        javaMailSender.send(mail);
    }
}