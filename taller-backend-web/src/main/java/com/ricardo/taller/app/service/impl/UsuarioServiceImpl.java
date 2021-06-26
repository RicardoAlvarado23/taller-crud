package com.ricardo.taller.app.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ricardo.taller.app.entity.Usuario;
import com.ricardo.taller.app.enums.TablaEnum;
import com.ricardo.taller.app.exceptions.BusinessException;
import com.ricardo.taller.app.exceptions.RequiredException;
import com.ricardo.taller.app.repository.UsuarioRepository;
import com.ricardo.taller.app.service.GeneradorCodigoService;
import com.ricardo.taller.app.service.UsuarioService;
import com.ricardo.taller.app.util.UtilFunctions;

/**
 * Implementacion del servicio de usuario
 * @author ricardo
 *
 */
@Service
public class UsuarioServiceImpl implements UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private GeneradorCodigoService generadorCodigoService;
	
	private TablaEnum NOMBRE_TABLA = TablaEnum.USUARIO;

	@Override
	@Transactional
	public Usuario guardar(Usuario usuario) throws BusinessException, RequiredException {
		validarUsuarioRegistroBasico(usuario);
		if (StringUtils.isEmpty(usuario.getCodigoUsuario())) {
			String secuencia = StringUtils.EMPTY;
			secuencia = generadorCodigoService.generarSecuenciaTabla(NOMBRE_TABLA);
			usuario.setCodigoUsuario(secuencia);
		}
		return usuarioRepository.save(usuario);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Usuario> listarTodos() {
		return usuarioRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Usuario> obtenerPorCodigo(String codigo) {
		return usuarioRepository.findById(codigo);
	}

	@Override
	@Transactional
	public void eliminar(String codigo) {
		usuarioRepository.deleteById(codigo);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Usuario> obtenerPorUsuario(String usuario) {
		return usuarioRepository.findByUserIgnoreCase(usuario);
	}
	
	@Transactional(readOnly = true)
	private void validarUsuarioRegistroBasico(Usuario usuario)
			throws RequiredException, BusinessException {
		if (!UtilFunctions.validateIsNotNullAndNotEmpty(usuario.getNombre())) {
			throw new RequiredException("Nombre es requerido");
		}
		
		if (!UtilFunctions.validateIsNotNullAndNotEmpty(usuario.getUser())) {
			throw new RequiredException("Usuario es requerido");
		}
		
		if (!UtilFunctions.validateIsNotNullAndNotEmpty(usuario.getPassword())) {
			throw new RequiredException("Clave es requerida");
		}
		
		
		Optional<Usuario> oUsuario = null;
		oUsuario = usuarioRepository.findByNombreIgnoreCase(usuario.getNombre());

		if (oUsuario.isPresent() && oUsuario.get().getCodigoUsuario() != usuario.getCodigoUsuario()) {
			throw new BusinessException("Ya existe un usuario con en el mismo nombre");
		}
		
		oUsuario = usuarioRepository.findByUserIgnoreCase(usuario.getUser());

		if (oUsuario.isPresent() && oUsuario.get().getCodigoUsuario() != usuario.getCodigoUsuario()) {
			throw new BusinessException("Ya existe un usuario con en el mismo username");
		}


	}

}
