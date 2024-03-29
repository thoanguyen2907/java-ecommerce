package com.example.javaecommerce.model.entity;

import com.example.javaecommerce.model.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Builder
@Table(name = "users")
public class UserEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "id", updatable = false, nullable = false)
    private Long id;
    private String email;
    private String password;

    private Boolean enabled = false;

    public UserEntity(final String email, final String password) {
        this.email = email;
        this.password = password;
    }


    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST, CascadeType.MERGE
    })
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonIgnore
    private Set<RoleEntity> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OrderEntity> orderEntity = new ArrayList<>();
}
