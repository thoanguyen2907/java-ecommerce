package com.example.javaecommerce.repository;

import com.example.javaecommerce.model.entity.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>, PagingAndSortingRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUsername(String username);
    Boolean existsByUsername(String name);

    Boolean existsByEmail(String email);
}
