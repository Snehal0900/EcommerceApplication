package com.example.ecommerce.service;

import com.example.ecommerce.entity.Roles;
import com.example.ecommerce.repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RolesImpl implements RolesService {

    @Autowired
    private RolesRepository rolesRepository;

    // Create a new role
    public Roles createRole(Roles roles) {
        return rolesRepository.save(roles);
    }

    // Update an existing role
    public Roles updateRole(Roles roles) {
        return rolesRepository.save(roles);
    }
}

