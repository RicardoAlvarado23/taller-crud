package com.ricardo.taller.app.service;

import java.util.List;
import java.util.Optional;

import com.ricardo.taller.app.entity.Sucursal;
import com.ricardo.taller.app.exceptions.BusinessException;
import com.ricardo.taller.app.exceptions.RequiredException;

/**
 * Interfaz del servicio Sucursal
 * @author ricardo
 * 
 */
public interface SucursalService {

	Sucursal guardar(Sucursal sucursal) throws BusinessException, RequiredException;
	List<Sucursal> listarTodos();
	Optional<Sucursal> obtenerPorCodigo(String codigo);
	void eliminar(String codigo);
	
}
