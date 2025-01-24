package com.example.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecommerce.entity.Roles;

public interface RolesRepository extends JpaRepository<Roles, Long> {
	Optional<Roles> findByName(String name);
}
