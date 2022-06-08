package com.springboot.backend.apirest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.backend.apirest.models.entity.Usuario;
import com.springboot.backend.apirest.models.services.UsuarioService;

@Controller
@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/rest/v1/")
public class BcUsuariosRest {

	@Autowired
	private UsuarioService userService;

	@GetMapping("/users/list")
	public List<Usuario> findAllUsers() {
		return userService.findAllUsers();

	}

}
