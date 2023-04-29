package com.example.javaecommerce.repository;

import com.example.javaecommerce.model.entity.UserEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);

    Boolean existsByUsername(String name);

    Boolean existsByEmail(String email);

    Page<UserEntity> findByUsername(String username, Pageable pageable);
}
