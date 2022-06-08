package com.springboot.backend.apirest.models.services;

import com.springboot.backend.apirest.models.entity.Contacto;

public interface IContactoService {
	
	public Contacto findById(Long id);
	
	public Contacto save(Contacto contacto);
	
	public void  delete(Long id);


}
