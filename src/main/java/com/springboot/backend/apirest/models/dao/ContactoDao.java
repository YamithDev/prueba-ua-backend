package com.springboot.backend.apirest.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.springboot.backend.apirest.models.entity.Contacto;

public interface ContactoDao extends JpaRepository<Contacto, Long>{
	
	@Query("select u from Contacto u")
	public List<Contacto> findAllContactos();

}
