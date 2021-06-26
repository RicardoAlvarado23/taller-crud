package com.ricardo.taller.app.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entidad de Generador de Codigo que permitira generar una secuencia unica para las demas tablas
 * 
 * @author ricardo
 * 
 */
@Entity
@Table(name = "generador_codigo", schema = "crud")
public class GeneradorCodigo implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Long id;
	
	@Column(unique = true, nullable = false)
	private String tabla;
	
	private Integer secuencia;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTabla() {
		return tabla;
	}

	public void setTabla(String tabla) {
		this.tabla = tabla;
	}

	public Integer getSecuencia() {
		return secuencia;
	}

	public void setSecuencia(Integer secuencia) {
		this.secuencia = secuencia;
	}

	@Override
	public String toString() {
		return "GeneradorCodigo [id=" + id + ", tabla=" + tabla + ", secuencia=" + secuencia + "]";
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
}
