package com.spring.apirest.services;

import java.util.List;

import com.spring.apirest.entities.Alumno;

public interface AlumnoService {
	
	public List<Alumno> findAll();
	
	public Alumno findById(Long id);
	
	public Alumno save(Alumno alumno);
	
	public void deleteById(Long id);
	
}
