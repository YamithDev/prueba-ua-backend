package com.springboot.backend.apirest.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.backend.apirest.models.entity.Contacto;
import com.springboot.backend.apirest.models.services.ContactoService;

@RestController
@CrossOrigin(origins = { "*" })
@RequestMapping("/rest/v1/")
public class ContactosRest {

	@Autowired
	private ContactoService contactoService;

	@GetMapping("/contactos/list")
	public List<Contacto> listarContactos() {
		return contactoService.findAllContactos();
	}
	
	@GetMapping("/contacto/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		
		Contacto contacto = null;
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			contacto = contactoService.findById(id);
		} catch (DataAccessException e) {
			response.put("mensaje","Error al realizar la consulta");
			response.put("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(contacto==null) {
			response.put("mensaje","El contacto con el ID:".concat(id.toString().concat(" no existe")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Contacto>(contacto, HttpStatus.OK);
	}
	
	@PostMapping("/contacto/new")
	public ResponseEntity<?> create(@Valid @RequestBody Contacto contacto, BindingResult result) {
		
		@SuppressWarnings("unused")
		Contacto contactoNew = null;
		
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			
			List<String> errors = result.getFieldErrors()
				.stream()
				.map( err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
				.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		try {
			contactoNew = contactoService.save(contacto);
		}catch( DataAccessException e) {
			response.put("mensaje","Error al realizar la inserción");
			response.put("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje","El contacto ha sido creado con éxito!");
		response.put("contacto", contacto);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/contacto/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Contacto contacto,BindingResult result, @PathVariable Long id) {
		
		Contacto contactoActual = contactoService.findById(id);
		Contacto contactoUpdated = null;
		
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			
			List<String> errors = result.getFieldErrors()
				.stream()
				.map( err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
				.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(contactoActual==null) {
			response.put("mensaje","Eror: no se puede editar, el cliente con el ID: ".concat(id.toString().concat(". No existe")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		try {
			
			contactoActual.setApellidos(contacto.getApellidos());
			contactoActual.setNombres(contacto.getNombres());
			contactoActual.setFechaNac(contacto.getFechaNac());
			contactoActual.setCedula(contacto.getCedula());
			contactoActual.setTelefono(contacto.getTelefono());
			contactoActual.setEdad(contacto.getEdad());
			contactoActual.setDireccion(contacto.getDireccion());
			
			contactoUpdated = contactoService.save(contactoActual);
			
		}catch( DataAccessException e) {
			
			response.put("mensaje","Error al actualizar el contacto");
			response.put("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		
		response.put("mensaje","El contacto ha sido actualizado con éxito!");
		response.put("contacto", contactoUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/contacto/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		
		Map<String, Object> response = new HashMap<>();
		
		Contacto contactoActual = contactoService.findById(id);
		try{
						
			contactoService.delete(id);
			
		}catch( DataAccessException e) {
			
			response.put("mensaje","Error al Eliminar el contacto");
			response.put("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);		
		}
		
		response.put("mensaje", "El contacto "+ contactoActual.getNombres() +" ha sido eliminado con éxito!");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		
	}
}
