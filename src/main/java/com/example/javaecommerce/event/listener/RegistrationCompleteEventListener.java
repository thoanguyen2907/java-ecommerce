package com.example.javaecommerce.event.listener;

import com.example.javaecommerce.event.RegistrationCompleteEvent;
import com.example.javaecommerce.model.EmailType;
import com.example.javaecommerce.model.entity.UserEntity;
import com.example.javaecommerce.security.jwt.AuthEntryPointJwt;

import com.example.javaecommerce.utils.EmailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    private final EmailService emailService;

    @Override
    public void onApplicationEvent(final RegistrationCompleteEvent event) {
        // create verification token for the user with the link
        UserEntity user = event.getUser();
        String token = event.getToken();
        // send mail to the user
        String url = event.getApplicationUrl() + "/api/auth/verifyRegistration?token=" + token;
        logger.info("Click the link to verify email after registration : {} " + url);
        // get user email
        String to = user.getEmail();
        // Send the email with email type, user email, url token
        emailService.sendEmail(to, EmailType.VERIFY_EMAIL, url);

        logger.info("Verification email sent to: {}", to);
    }
}
