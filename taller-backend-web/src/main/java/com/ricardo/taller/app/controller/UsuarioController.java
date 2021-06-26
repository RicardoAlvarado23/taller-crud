package com.ricardo.taller.app.controller;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.ricardo.taller.app.entity.Usuario;
import com.ricardo.taller.app.exceptions.BusinessException;
import com.ricardo.taller.app.exceptions.RequiredException;
import com.ricardo.taller.app.service.UsuarioService;
import com.ricardo.taller.app.util.UtilConstants;
import com.ricardo.taller.app.util.UtilFunctions;

/**
 * Controlador REST de Usuarios
 * @author ricardo
 *
 */
@RestController
@RequestMapping("/usuario")
public class UsuarioController {
	
	private Logger logger = LoggerFactory.getLogger(UsuarioController.class);

	@Autowired
	private UsuarioService usuarioService;
	
	@GetMapping()
	public ResponseEntity<?> listarTodos() {
		Map<String, Object> response = UtilFunctions.createMessageOk();
		response.put(UtilConstants.CURSOR, usuarioService.listarTodos());
		return ResponseEntity.ok().body(response);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> obtenerPorId(@PathVariable String id) {
		Map<String, Object> response = UtilFunctions.createMessageOk();
		Optional<Usuario> oUsuario = usuarioService.obtenerPorCodigo(id);
		if (!oUsuario.isPresent()) {
			response = UtilFunctions.createMessageError("No existe el usuario");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		} else {
			response.put(UtilConstants.USUARIO, oUsuario.get());
			return ResponseEntity.ok(response);
		}
	}
	
	@PostMapping()
	public ResponseEntity<?> guardar(@RequestBody Usuario usuario) {
		Map<String, Object> response = UtilFunctions.createMessageOk();
		try {
			response.put(UtilConstants.USUARIO, usuarioService.guardar(usuario));
		} catch (BusinessException e) {
			logger.error("Error en guardar usuario: " + e.getMessage());
			response = UtilFunctions.createMessageError( e.getMessage());
		} catch (RequiredException e) {
			logger.error("Error en guardar usuario: " + e.getMessage());
			response = UtilFunctions.createMessageError( e.getMessage());
		}
		return ResponseEntity.ok(response);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> actualizar(@RequestBody Usuario usuario, @PathVariable String id) {
		Map<String, Object> response = UtilFunctions.createMessageOk();
		Optional<Usuario> oUsuario = usuarioService.obtenerPorCodigo(id);
		if (!oUsuario.isPresent()) {
			response = UtilFunctions.createMessageError("No existe el usuario");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		Usuario usuarioBD = oUsuario.get();
		usuarioBD.setNombre(usuario.getNombre());
		usuarioBD.setPassword(usuario.getPassword());
		usuarioBD.setSucursal(usuario.getSucursal());
		try {
			response.put(UtilConstants.USUARIO, usuarioService.guardar(usuarioBD));
		} catch (BusinessException e) {
			logger.error("Error en actaulizar usuario: " + e.getMessage());
			response = UtilFunctions.createMessageError(e.getMessage() );
		} catch (RequiredException e) {
			logger.error("Error en actaulizar usuario: " + e.getMessage());
			response = UtilFunctions.createMessageError( e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminar(@PathVariable String id) {
		Map<String, Object> response = UtilFunctions.createMessageOk();
		Optional<Usuario> oUsuario = usuarioService.obtenerPorCodigo(id);
		if (!oUsuario.isPresent()) {
			response = UtilFunctions.createMessageError("No existe el usuario");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		Usuario usuarioBD  = oUsuario.get();
		usuarioService.eliminar(usuarioBD.getCodigoUsuario());
		response.put(UtilConstants.USUARIO, usuarioBD);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
}