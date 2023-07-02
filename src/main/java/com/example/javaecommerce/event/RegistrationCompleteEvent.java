package com.example.javaecommerce.event;

import com.example.javaecommerce.model.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Setter
@Getter
public class RegistrationCompleteEvent extends ApplicationEvent {

    private UserEntity user;
    private String applicationUrl;
    private String token;

    public RegistrationCompleteEvent(final UserEntity user, final String applicationUrl, final String token) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
        this.token = token;
    }
}
