package com.example.javaecommerce.utils;

import com.example.javaecommerce.model.EmailType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    public EmailService(final JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(final String to, final String subject, final String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        javaMailSender.send(message);
    }

    public void sendEmail(final String to, final EmailType emailType, final String url) {
        // based on email type, get the body with correct url token
        String body = getEmailBody(emailType, url);
        // based on email type, get the correct subject
        String subject = getEmailSubject(emailType);
        sendEmail(to, subject, body);
    }

    private String getEmailSubject(final EmailType emailType) {
        String subject = "";
        switch (emailType) {
            case FORGOT_PASSWORD:
                subject = "FORGOT_PASSWORD";
                break;
            case VERIFY_EMAIL:
                subject = "VERIFY_EMAIL";
                break;
        }
        return subject;
    }

    private String getEmailBody(final EmailType emailType, final String url) {
        String body = "";
        switch (emailType) {
            case FORGOT_PASSWORD:
                // Logic to get the email body for forgot password
                body = "Dear User,\n\n"
                        + "You have requested to reset your password. Please click on the following link to proceed:\n"
                        + url + "\n\n"
                        + "If you didn't request this, you can safely ignore this email.\n\n"
                        + "Regards,\n"
                        + "Your Application";
                break;
            case VERIFY_EMAIL:
                // Logic to get the email body for email verification
                body = "Dear User,\n\n"
                        + "This is the link to verify your registered email, please click the link to proceed:\n"
                        + url + "\n\n"
                        + "If you didn't register by this email, please report us.\n\n"
                        + "Regards,\n"
                        + "Your Application";
                break;
        }

        return body;
    }
}
