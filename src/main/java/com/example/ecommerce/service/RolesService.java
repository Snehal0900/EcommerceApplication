package com.example.ecommerce.service;

import java.util.List;

import com.example.ecommerce.entity.Roles;

public interface RolesService {

	public Roles createRole(Roles roles);
	
	public Roles updateRole(Roles roles);
	
	public List<Roles> getAllRoles();
}
