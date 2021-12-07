package com.spring.apirest.services;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.apirest.entities.Usuario;
import com.spring.apirest.repositories.IUsuarioDao;

@Service
public class UsuarioService implements UserDetailsService {

	private Logger logger = LoggerFactory.getLogger(UsuarioService.class);

	@Autowired
	private IUsuarioDao usuarioDao;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioDao.findByUsername(username);

		if (usuario == null) {
			logger.error("Error en el login: No existe el usuario ".concat(username).concat(" en el sistema."));
			throw new UsernameNotFoundException("Error en el login: No existe el usuario ".concat(username).concat(" en el sistema."));
		}
		List<GrantedAuthority> authorities = usuario.getRoles()
				.stream()
				.map(rol -> new SimpleGrantedAuthority(rol.getNombre()))
				.peek(authority -> logger.info("Rol: ".concat(authority.getAuthority())))
				.collect(Collectors.toList());

		return new User(usuario.getUsername(), usuario.getPassword(), usuario.isEnabled(), true, true, true, authorities);
	}

}
