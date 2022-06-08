package com.springboot.backend.apirest.models.services;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.springboot.backend.apirest.models.dao.BcRoleRepository;
import com.springboot.backend.apirest.models.dao.IUsuarioDao;
import com.springboot.backend.apirest.models.entity.Role;
import com.springboot.backend.apirest.models.entity.Usuario;

@Service
public class UsuarioService implements IUsuarioService, UserDetailsService {

	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder;
	}

	private final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

	
	@Autowired
	private BcRoleRepository roleDao;

	@Autowired
	private IUsuarioDao usuarioDao;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Usuario usuario = usuarioDao.findByUsername(username);

		if (usuario == null) {
			logger.error("Error en el Login: no existe el usuario '" + username + "' En el Sistema!");
			throw new UsernameNotFoundException(
					"Error en el Login: no existe el usuario '" + username + "' En el Sistema!");
		}

		List<GrantedAuthority> authorities = usuario.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getNombre()))
				.peek(authority -> logger.info("Role :" + authority.getAuthority())).collect(Collectors.toList());

		return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, true,
				authorities);
	}

	@Override
	@Transactional(readOnly = true)
	public Usuario findByUsername(String username) {
		return usuarioDao.findByUsername(username);
	}

	public List<Usuario> findAllUsers() {
		return usuarioDao.findAllUsers();
	}

	public Usuario crearNuevoUsuario(Usuario usuario) {		
		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		
		Usuario userNew = new Usuario();
		
		userNew.setEnabled(usuario.getEnabled());
		userNew.setUsername(usuario.getUsername());
		userNew.setPassword(passwordEncoder().encode(usuario.getPassword()));
		
		Role userRole = roleDao.findByNombre("ROLE_ADMIN");
		
		userNew.setRoles(Arrays.asList(userRole));
		
		String json;
		try {
			json = ow.writeValueAsString(userNew);
			logger.info("usuario creado: " + json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return usuarioDao.save(userNew);
	}

}
