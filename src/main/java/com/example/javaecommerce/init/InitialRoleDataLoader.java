package com.example.javaecommerce.init;

import com.example.javaecommerce.model.ERole;
import com.example.javaecommerce.model.entity.RoleEntity;
import com.example.javaecommerce.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;



@Component
public class InitialRoleDataLoader {

    @Autowired
    private RoleRepository roleRepository;

    @Bean
    public ApplicationRunner initializer() {
        List<ERole> roles = Arrays.asList(ERole.ROLE_ADMIN, ERole.ROLE_MODERATOR, ERole.ROLE_USER);
        return args -> roles.forEach(this::createRoleIfNotFound);
    }

    private Optional<RoleEntity> createRoleIfNotFound(ERole roleName) {
        Optional<RoleEntity> role = roleRepository.findByName(roleName);
        if (!role.isPresent()) {
            RoleEntity newRole = new RoleEntity();
            newRole.setName(roleName);
            roleRepository.save(newRole);
        }
        return role;
    }
}
