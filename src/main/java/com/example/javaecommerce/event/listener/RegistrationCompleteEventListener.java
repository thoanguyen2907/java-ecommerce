package com.example.javaecommerce.event.listener;

import com.example.javaecommerce.event.RegistrationCompleteEvent;
import com.example.javaecommerce.model.entity.UserEntity;
import com.example.javaecommerce.security.jwt.AuthEntryPointJwt;
import com.example.javaecommerce.services.UserService;
import com.example.javaecommerce.utils.EmailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    private final EmailService emailService;

    private final UserService userService;

    @Override
    public void onApplicationEvent(final RegistrationCompleteEvent event) {
        // Create verification token for the user with the link
        UserEntity user = event.getUser();
        String token = String.valueOf(UUID.randomUUID());
        userService.saveVerificationTokenForUser(token, user);
        //send mail to the user
        String url = event.getApplicationUrl() + "/api/auth/verifyRegistration?token=" + token;
        logger.info("Click the link to verify email after registration : {} " + url);

        // Compose the email
        String to = user.getEmail();
        String subject = "Email Verification";
        String body = "Please click the following link to verify your email: " + url;

        // Send the email
        emailService.sendEmail(to, subject, body);

        logger.info("Verification email sent to: {}", to);

    }
}
