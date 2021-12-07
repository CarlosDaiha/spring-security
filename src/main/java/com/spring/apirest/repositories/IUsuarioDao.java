package com.spring.apirest.repositories;

import org.springframework.data.repository.CrudRepository;

import com.spring.apirest.entities.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario, Long> {
	
	/** Primera versión para buscar un usuario por su username */
	public Usuario findByUsername(String username);
	
}
