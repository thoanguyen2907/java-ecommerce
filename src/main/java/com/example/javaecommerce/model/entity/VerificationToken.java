package com.example.javaecommerce.model.entity;

import com.example.javaecommerce.model.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Builder
@Table(name = "verification_token")
public class VerificationToken extends BaseEntity {
    private static final int EXPIRATION_TIME = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "expiration_time", nullable = false)
    private Date expirationTime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity user;

    public VerificationToken(final UserEntity user, final String token) {
        super();
        this.token = token;
        this.user = user;
        this.expirationTime = calculateExpirationDate(EXPIRATION_TIME);
    }

    public Date calculateExpirationDate(final int expirationTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, expirationTime);
        return new Date(calendar.getTime().getTime());
    }
}
