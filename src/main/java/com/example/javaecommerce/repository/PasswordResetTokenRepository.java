package com.example.javaecommerce.repository;

import com.example.javaecommerce.model.entity.PasswordResetToken;
import com.example.javaecommerce.model.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);
}