package com.ricardo.taller.app.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ricardo.taller.app.entity.Sucursal;
import com.ricardo.taller.app.enums.TablaEnum;
import com.ricardo.taller.app.exceptions.BusinessException;
import com.ricardo.taller.app.exceptions.RequiredException;
import com.ricardo.taller.app.repository.SucursalRepository;
import com.ricardo.taller.app.service.GeneradorCodigoService;
import com.ricardo.taller.app.service.SucursalService;
import com.ricardo.taller.app.util.UtilFunctions;


/**
 * Implementacion del servicio de sucursal
 * @author ricardo
 *
 */
@Service(value = "sucursalService")
public class SucursalServiceImpl implements SucursalService {
	
	@Autowired
	private SucursalRepository sucursalRepository;
	
	@Autowired
	private GeneradorCodigoService generadorCodigoService;
	
	private TablaEnum NOMBRE_TABLA = TablaEnum.SUCURSAL;

	@Override
	@Transactional
	public Sucursal guardar(Sucursal sucursal) throws BusinessException, RequiredException {
		validarSucursalRegistroBasico(sucursal);
		String secuencia = StringUtils.EMPTY;
		if (StringUtils.isEmpty(sucursal.getCodigoSucursal())) {
			secuencia = generadorCodigoService.generarSecuenciaTabla(NOMBRE_TABLA);
			sucursal.setCodigoSucursal(secuencia);
		}
		return sucursalRepository.save(sucursal);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Sucursal> listarTodos() {
		return sucursalRepository.findAll(Sort.by(Direction.ASC, "nombre"));
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Sucursal> obtenerPorCodigo(String codigo) {
		return sucursalRepository.findById(codigo);
	}

	@Override
	@Transactional
	public void eliminar(String codigo) {
		sucursalRepository.deleteById(codigo);
	}
	
	@Transactional(readOnly = true)
	private void validarSucursalRegistroBasico(Sucursal sucursal)
			throws RequiredException, BusinessException {
		if (!UtilFunctions.validateIsNotNullAndNotEmpty(sucursal.getNombre())) {
			throw new RequiredException("Nombre es requerido");
		}
		
		Optional<Sucursal> oSucursal = null;
		oSucursal = sucursalRepository.findByNombreIgnoreCase(sucursal.getNombre());

		if (oSucursal.isPresent() && oSucursal.get().getCodigoSucursal() != sucursal.getCodigoSucursal()) {
			throw new BusinessException("Ya existe una sucursal con en el mismo nombre");
		}

	}
	 

}
