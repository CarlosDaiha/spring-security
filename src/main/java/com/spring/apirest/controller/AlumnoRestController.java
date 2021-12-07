package com.spring.apirest.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.apirest.entities.Alumno;
import com.spring.apirest.services.AlumnoService;

@RestController
@RequestMapping("/api")
public class AlumnoRestController {

	@Autowired
	private AlumnoService alumnoService;

	@GetMapping("/alumnos")
	public List<Alumno> readAll() {
		return alumnoService.findAll();
	}

	@GetMapping("/alumnos/{id}")
	public ResponseEntity<?> read(@PathVariable Long id){
		Alumno alumno;
		HashMap<String,Object> response = new HashMap<>();

		try {
			alumno = alumnoService.findById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en base de datos.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<HashMap<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if(alumno == null) {
			response.put("mensaje", "El alumno ID: ".concat(id.toString().concat(" no existe en la base de datos.")));
			return new ResponseEntity<HashMap<String,Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Alumno>(alumno, HttpStatus.OK);
	}

	@PostMapping("/alumnos")
	public ResponseEntity<?> create(@RequestBody Alumno alumno) {
		HashMap<String,Object> response = new HashMap<>();

		try {
			alumnoService.save(alumno);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al insertar en base de datos.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<HashMap<String,Object>>(response, HttpStatus.BAD_REQUEST);
		}

		response.put("mensaje", "Usuario creado con éxito.");
		response.put("alumno", alumno);
		return new ResponseEntity<HashMap<String,Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping("/alumnos/{id}")
	public ResponseEntity<?> update(@RequestBody Alumno alumno, @PathVariable Long id) {
		HashMap<String,Object> response = new HashMap<>();

		Alumno alumnoUpdated = alumnoService.findById(id);
		if (alumnoUpdated == null) {
			response.put("mensaje", "El alumno ID: ".concat(id.toString().concat(" no existe en la base de datos.")));
			return new ResponseEntity<HashMap<String,Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {
			alumnoUpdated.setNombre(alumno.getNombre());
			alumnoUpdated.setApellido(alumno.getApellido());
			alumnoUpdated.setEmail(alumno.getEmail());
			alumnoUpdated.setTelefono(alumno.getTelefono());
			
			alumnoService.save(alumnoUpdated);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar en base de datos.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<HashMap<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El alumno se ha actualizado con éxito.");
		response.put("alumno", alumnoUpdated);
		return new ResponseEntity<HashMap<String,Object>>(response, HttpStatus.OK);
	}

	@DeleteMapping("/alumnos/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		HashMap<String,Object> response = new HashMap<>();

		if (alumnoService.findById(id) == null) {
			response.put("mensaje", "El alumno ID: ".concat(id.toString().concat(" no existe en la base de datos.")));
			return new ResponseEntity<HashMap<String,Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {
			alumnoService.deleteById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar alumno en base de datos.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<HashMap<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El alumno se ha eliminado con éxito.");
		return new ResponseEntity<HashMap<String,Object>>(response, HttpStatus.NO_CONTENT);
	}

}
