package com.spring.apirest.repositories;

import org.springframework.data.repository.CrudRepository;

import com.spring.apirest.entities.Alumno;

public interface AlumnoDao extends CrudRepository<Alumno, Long> {

}
