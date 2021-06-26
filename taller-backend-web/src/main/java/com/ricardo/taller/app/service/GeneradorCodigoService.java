package com.ricardo.taller.app.service;

import com.ricardo.taller.app.enums.TablaEnum;
import com.ricardo.taller.app.exceptions.BusinessException;

/**
 * 
 * @author ricardo
 *
 */
public interface GeneradorCodigoService {

	public String  generarSecuenciaTabla(TablaEnum tabla) throws BusinessException;
	
}
