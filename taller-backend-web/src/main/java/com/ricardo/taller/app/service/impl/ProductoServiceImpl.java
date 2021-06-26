package com.ricardo.taller.app.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ricardo.taller.app.entity.Producto;
import com.ricardo.taller.app.enums.TablaEnum;
import com.ricardo.taller.app.exceptions.BusinessException;
import com.ricardo.taller.app.exceptions.RequiredException;
import com.ricardo.taller.app.repository.ProductoRepository;
import com.ricardo.taller.app.service.GeneradorCodigoService;
import com.ricardo.taller.app.service.ProductoService;
import com.ricardo.taller.app.util.UtilFunctions;

/**
 * Implementacion de la interfaz de servicio Producto
 * @author ricardo
 *
 */
@Service
public class ProductoServiceImpl implements ProductoService {
	
	private Logger logger = LoggerFactory.getLogger(ProductoServiceImpl.class);
	
	@Autowired
	private ProductoRepository productoRepository;
	
	@Autowired
	private GeneradorCodigoService generadorCodigoService;
	
	private TablaEnum NOMBRE_TABLA = TablaEnum.PRODUCTO;

	@Override
	@Transactional
	public Producto guardar(Producto producto) throws BusinessException, RequiredException {
		validarProductoRegistroBasico(producto);
		if (StringUtils.isEmpty(producto.getCodigoProducto())) {
			String secuencia = StringUtils.EMPTY;
			secuencia = generadorCodigoService.generarSecuenciaTabla(NOMBRE_TABLA);
			producto.setCodigoProducto(secuencia);
		}
		return productoRepository.save(producto);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Producto> listarTodos() {
		return productoRepository.findAll(Sort.by(Direction.ASC, "nombre"));
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Producto> obtenerPorCodigo(String codigo) {
		return productoRepository.findById(codigo);
	}

	@Override
	@Transactional
	public void eliminar(String codigo) {
		productoRepository.deleteById(codigo);
	}
	
	@Transactional(readOnly = true)
	private void validarProductoRegistroBasico(Producto producto)
			throws RequiredException, BusinessException {
		if (!UtilFunctions.validateIsNotNullAndNotEmpty(producto.getNombre())) {
			throw new RequiredException("Nombre es requerido");
		}
		
		if (producto.getPrecio().doubleValue() <= 0.0) {
			throw new BusinessException("El precio debe ser mayor a cero");
		}
		
		Optional<Producto> oProducto = null;
		oProducto = productoRepository.findByNombreIgnoreCase(producto.getNombre());

		if (oProducto.isPresent() && oProducto.get().getCodigoProducto() != producto.getCodigoProducto()) {
			throw new BusinessException("Ya existe un Producto con en el mismo nombre");
		}

	}

}
