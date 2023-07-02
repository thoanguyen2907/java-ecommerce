package com.example.javaecommerce.model.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetModel {
    private String email;
    private String oldPassword;
    private String newPassword;
}
