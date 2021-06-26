package com.ricardo.taller.app.service;

import java.util.List;
import java.util.Optional;

import com.ricardo.taller.app.entity.Usuario;
import com.ricardo.taller.app.exceptions.BusinessException;
import com.ricardo.taller.app.exceptions.RequiredException;

/**
 *  Interfaz del servicio Usuario
 * @author ricardo
 *
 */
public interface UsuarioService {

	Usuario guardar(Usuario usuario) throws BusinessException, RequiredException;
	List<Usuario> listarTodos();
	Optional<Usuario> obtenerPorCodigo(String codigo);
	Optional<Usuario> obtenerPorUsuario(String usuario);
	void eliminar(String codigo);
	
}
