package com.example.javaecommerce.service.impl;

import com.example.javaecommerce.converter.Converter;
import com.example.javaecommerce.model.entity.*;
import com.example.javaecommerce.model.request.RoleRequest;
import com.example.javaecommerce.model.response.RoleResponse;
import com.example.javaecommerce.repository.RoleRepository;
import com.example.javaecommerce.service.RoleService;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private  final RoleRepository roleRepository;
    @Override
    public List<RoleResponse> getAllRoles() {
        List<RoleEntity> roleEntities = roleRepository.findAll();
        return Converter.toList(roleEntities, RoleResponse.class);
    }

    @Override
    public RoleResponse addRole(RoleRequest role) {
        RoleEntity roleEntity = Converter.toModel(role, RoleEntity.class);
        roleRepository.save(roleEntity);
        return Converter.toModel(roleEntity, RoleResponse.class);
    }

    @Override
    public RoleResponse getRoleById(Long roleID) {
        RoleEntity roleEntity = roleRepository.findById(roleID).orElseThrow(() -> new IllegalStateException(
                "role with id" + " does not exist"
        ));
        return Converter.toModel(roleEntity, RoleResponse.class);
    }

    @Override
    public void deleteRole(Long roleID) throws Exception {
        roleRepository.deleteById(roleID);
    }

    @Override
    public RoleResponse updateRole(RoleRequest roleRequest, Long id) {
        return null;
    }
}
