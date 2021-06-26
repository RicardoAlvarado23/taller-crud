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

import com.ricardo.taller.app.entity.Producto;
import com.ricardo.taller.app.exceptions.BusinessException;
import com.ricardo.taller.app.exceptions.RequiredException;
import com.ricardo.taller.app.service.ProductoService;
import com.ricardo.taller.app.util.UtilConstants;
import com.ricardo.taller.app.util.UtilFunctions;

/**
 * Controlador REST de Productos
 * @author ricardo
 *
 */
@RestController
@RequestMapping("/producto")
public class ProductoController {

	@Autowired
	private ProductoService productoService;
	
	private Logger logger = LoggerFactory.getLogger(ProductoController.class);
	
	@GetMapping()
	public ResponseEntity<?> listarTodos() {
		Map<String, Object> response = UtilFunctions.createMessageOk();
		response.put(UtilConstants.CURSOR, productoService.listarTodos());
		return ResponseEntity.ok().body(response);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> obtenerPorId(@PathVariable String id) {
		Map<String, Object> response = UtilFunctions.createMessageOk();
		Optional<Producto> oProducto = productoService.obtenerPorCodigo(id);
		if (!oProducto.isPresent()) {
			response = UtilFunctions.createMessageError("No existe el producto");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		} else {
			response.put(UtilConstants.PRODUCTO, oProducto.get());
			return ResponseEntity.ok(response);
		}
	}
	
	@PostMapping()
	public ResponseEntity<?> guardar(@RequestBody Producto producto) {
		Map<String, Object> response = UtilFunctions.createMessageOk();
		Producto productoGuardado;
		try {
			productoGuardado = productoService.guardar(producto);
			response.put(UtilConstants.PRODUCTO, productoGuardado);
		} catch (BusinessException e) {
			logger.error("Error en guardar usuario: " + e.getMessage());
			response = UtilFunctions.createMessageError(e.getMessage());
		} catch (RequiredException e) {
			logger.error("Error en guardar usuario: " + e.getMessage());
			response = UtilFunctions.createMessageError(e.getMessage());
		}
		return ResponseEntity.ok(response);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> actualizar(@RequestBody Producto producto, @PathVariable String id) {
		Map<String, Object> response = UtilFunctions.createMessageOk();
		Optional<Producto> oProducto = productoService.obtenerPorCodigo(id);
		if (!oProducto.isPresent()) {
			response = UtilFunctions.createMessageError("No existe el producto");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		Producto productoBD = oProducto.get();
		productoBD.setNombre(producto.getNombre());
		productoBD.setPrecio(producto.getPrecio());
		try {
			response.put(UtilConstants.PRODUCTO, productoService.guardar(productoBD));
		} catch (BusinessException e) {
			logger.error("Error en actualizar usuario: " + e.getMessage());
			response = UtilFunctions.createMessageError(e.getMessage());
		} catch (RequiredException e) {
			logger.error("Error en actualizar usuario: " + e.getMessage());
			response = UtilFunctions.createMessageError(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminar(@PathVariable String id) {
		Map<String, Object> response = UtilFunctions.createMessageOk();
		Optional<Producto> oProducto = productoService.obtenerPorCodigo(id);
		if (!oProducto.isPresent()) {
			response = UtilFunctions.createMessageError("No existe el producto");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		Producto productoBD = oProducto.get();
		productoService.eliminar(productoBD.getCodigoProducto());
		response.put(UtilConstants.PRODUCTO, productoBD);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
}
