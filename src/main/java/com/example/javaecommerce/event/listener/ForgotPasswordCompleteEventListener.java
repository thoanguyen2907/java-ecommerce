package com.example.javaecommerce.event.listener;

import com.example.javaecommerce.event.ForgotPasswordCompleteEvent;
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


@Component
@RequiredArgsConstructor
public class ForgotPasswordCompleteEventListener implements ApplicationListener<ForgotPasswordCompleteEvent> {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    private final EmailService emailService;

    private final UserService userService;

    @Override
    public void onApplicationEvent(ForgotPasswordCompleteEvent event) {
        UserEntity user = event.getUser();
        String urlLink = event.getApplicationUrl();

        // Compose the email
        String to = user.getEmail();
        String subject = "Email Forgot password";
        String body = "Please click the following link to change the password your email: " + urlLink;
        // Send the email
        emailService.sendEmail(to, subject, body);

        logger.info("Forgot password email email sent to: {}", to);
    }
}
