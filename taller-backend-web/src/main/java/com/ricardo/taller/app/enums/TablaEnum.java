package com.ricardo.taller.app.enums;

/**
 * Definicion de tablas que permitira obtener un codigo unico por cada una de ellas
 * @author ricardo
 *
 */
public enum TablaEnum {
	USUARIO("usuario"),
	PRODUCTO("producto"),
	SUCURSAL("sucursal");
	
	private String code;
	
	private TablaEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
}
