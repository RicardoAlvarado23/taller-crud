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

import com.ricardo.taller.app.entity.Sucursal;
import com.ricardo.taller.app.exceptions.BusinessException;
import com.ricardo.taller.app.exceptions.RequiredException;
import com.ricardo.taller.app.service.SucursalService;
import com.ricardo.taller.app.util.UtilConstants;
import com.ricardo.taller.app.util.UtilFunctions;

/**
 * Controlador REST de Sucursal
 * @author ricardo
 *
 */
@RestController
@RequestMapping("/sucursal")
public class SucursalController {

	private Logger logger = LoggerFactory.getLogger(SucursalController.class);
	
	@Autowired
	private SucursalService sucursalService;
	
	@GetMapping()
	public ResponseEntity<?> listarTodos() {
		Map<String, Object> response = UtilFunctions.createMessageOk();
		response.put(UtilConstants.CURSOR, sucursalService.listarTodos());
		return ResponseEntity.ok().body(response);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> obtenerPorId(@PathVariable String id) {
		Map<String, Object> response = UtilFunctions.createMessageOk();
		Optional<Sucursal> oSucursal = sucursalService.obtenerPorCodigo(id);
		if (!oSucursal.isPresent()) {
			response = UtilFunctions.createMessageError("No existe la sucursal");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		} else {
			response.put(UtilConstants.SUCURSAL, oSucursal.get());
			return ResponseEntity.ok(response);
		}
	}
	
	@PostMapping()
	public ResponseEntity<?> guardar(@RequestBody Sucursal sucursal) {
		Map<String, Object> response = UtilFunctions.createMessageOk();
		Sucursal sucursalBD;
		try {
			sucursalBD = sucursalService.guardar(sucursal);
			response.put(UtilConstants.SUCURSAL, sucursalBD);
		} catch (BusinessException e) {
			logger.error("Error en guardar sucursal: " + e.getMessage());
			response = UtilFunctions.createMessageError(e.getMessage());
		} catch (RequiredException e) {
			logger.error("Error en guardar sucursal: " + e.getMessage());
			response = UtilFunctions.createMessageError(e.getMessage() );
		}
		
		return ResponseEntity.ok(response);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> actualizar(@RequestBody Sucursal sucursal, @PathVariable String id) {
		Map<String, Object> response = UtilFunctions.createMessageOk();
		Optional<Sucursal> oSucursal = sucursalService.obtenerPorCodigo(id);
		if (!oSucursal.isPresent()) {
			response = UtilFunctions.createMessageError("No existe la sucursal");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		Sucursal sucursalBD = oSucursal.get();
		sucursalBD.setNombre(sucursal.getNombre());
		try {
			response.put(UtilConstants.SUCURSAL, sucursalService.guardar(sucursalBD));
		} catch (BusinessException e) {
			logger.error("Error en actualizar sucursal: " + e.getMessage());
			response = UtilFunctions.createMessageError(e.getMessage() );
		} catch (RequiredException e) {
			logger.error("Error en actualizar sucursal: " + e.getMessage());
			response = UtilFunctions.createMessageError(e.getMessage() );
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminar(@PathVariable String id) {
		Map<String, Object> response = UtilFunctions.createMessageOk();
		Optional<Sucursal> oSucursal = sucursalService.obtenerPorCodigo(id);
		if (!oSucursal.isPresent()) {
			response = UtilFunctions.createMessageError("No existe la sucursal");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		Sucursal sucursalBD  = oSucursal.get();
		sucursalService.eliminar(sucursalBD.getCodigoSucursal());
		response.put(UtilConstants.SUCURSAL, sucursalBD);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
}