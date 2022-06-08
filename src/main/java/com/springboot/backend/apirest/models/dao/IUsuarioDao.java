package com.springboot.backend.apirest.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.springboot.backend.apirest.models.entity.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario, Long> {

	public Usuario findByUsername(String username);

	@Query("select u from Usuario u where u.username=?1")
	public Usuario findByUsername2(String username);

	@Query("select u from Usuario u")
	public List<Usuario> findAllUsers();

	@Query(value = "select * from usuarios where fk_ma_entidad=:idEntidad", nativeQuery = true)
	public List<Usuario> findByIdEntidad(@Param("idEntidad") Integer idEntidad);

}
