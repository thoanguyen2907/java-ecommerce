package com.example.javaecommerce.event.listener;

import com.example.javaecommerce.event.ForgotPasswordCompleteEvent;
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
public class ForgotPasswordCompleteEventListener implements ApplicationListener<ForgotPasswordCompleteEvent> {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    private final EmailService emailService;

    @Override
    public void onApplicationEvent(final ForgotPasswordCompleteEvent event) {
        UserEntity user = event.getUser();
        String urlLink = event.getApplicationUrl();
        // get user email
        String to = user.getEmail();
        //send type of email , user email and url link including token
        emailService.sendEmail(to, EmailType.FORGOT_PASSWORD, urlLink);
        logger.info("Forgot password email email sent to: {}", to);
    }
}
