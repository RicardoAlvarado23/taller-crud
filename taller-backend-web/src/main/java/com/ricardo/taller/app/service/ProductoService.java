package com.ricardo.taller.app.service;

import java.util.List;
import java.util.Optional;

import com.ricardo.taller.app.entity.Producto;
import com.ricardo.taller.app.exceptions.BusinessException;
import com.ricardo.taller.app.exceptions.RequiredException;

/**
 * Interfaz del servicio producto
 * @author ricardo
 * 
 */
public interface ProductoService {

	Producto guardar(Producto producto) throws BusinessException, RequiredException;
	List<Producto> listarTodos();
	Optional<Producto> obtenerPorCodigo(String codigo);
	void eliminar(String codigo);
	
}
