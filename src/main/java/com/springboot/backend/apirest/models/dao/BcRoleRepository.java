package com.springboot.backend.apirest.models.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.springboot.backend.apirest.models.entity.Role;


public interface BcRoleRepository extends CrudRepository<Role, Long>{
	
	public Role findByNombre(String nombre);
	
	public Optional<Role> findById(Long id);
	
}
