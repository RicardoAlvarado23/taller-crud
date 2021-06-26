package com.ricardo.taller.app.service.impl;

import java.util.Optional;

import javax.persistence.LockModeType;
import javax.persistence.PersistenceException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ricardo.taller.app.entity.GeneradorCodigo;
import com.ricardo.taller.app.enums.TablaEnum;
import com.ricardo.taller.app.exceptions.BusinessException;
import com.ricardo.taller.app.repository.GeneradorCodigoRepository;
import com.ricardo.taller.app.service.GeneradorCodigoService;
import com.ricardo.taller.app.util.UtilConstants;

/**
 * Servicio que pemitira obtener un codigo de secuencia para cada tabla
 * @author ricardo
 *
 */
@Service
public class GeneradorSecuenciaImpl implements GeneradorCodigoService {
	
	@Autowired
	private GeneradorCodigoRepository generadorCodigoRepository;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	public String generarSecuenciaTabla(TablaEnum tabla) throws BusinessException {
		String secuencia = StringUtils.EMPTY;
		try {
			Optional<GeneradorCodigo> oGeneradorCodigo = generadorCodigoRepository.findByTabla(tabla.getCode());
			GeneradorCodigo generadorCodigo = null;
			if (oGeneradorCodigo.isPresent()) {
				generadorCodigo = oGeneradorCodigo.get();
				generadorCodigo.setSecuencia(generadorCodigo.getSecuencia() + 1);
			} else {
				generadorCodigo = new GeneradorCodigo();
				generadorCodigo.setTabla(tabla.getCode());
				generadorCodigo.setSecuencia(1);
			}
			secuencia = StringUtils.leftPad(generadorCodigo.getSecuencia()+"", UtilConstants.NUMBER_TWO.intValue(), UtilConstants.STRING_NUMBER_ZERO );
			generadorCodigoRepository.saveAndFlush(generadorCodigo);
		} catch (PersistenceException e) {
			throw new BusinessException("No se pudo obtener secuencia generada para la tabla: " + tabla.getCode());
		}
		return secuencia;
	}

}
