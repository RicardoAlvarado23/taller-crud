package com.ricardo.taller.app.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Entidad de sucursal
 * @author ricardo
 * 
 */
@Entity
@Table(name = "sucursal", schema = "crud")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Sucursal implements Serializable {

	@Id
	@Column(unique = true, nullable = false, name = "cod_sucursal", length = 2)
	private String codigoSucursal;
	
	@Column(nullable = false, length = 50)
	private String nombre;

	public String getCodigoSucursal() {
		return codigoSucursal;
	}

	public void setCodigoSucursal(String codigoSucursal) {
		this.codigoSucursal = codigoSucursal;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigoSucursal == null) ? 0 : codigoSucursal.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sucursal other = (Sucursal) obj;
		if (codigoSucursal == null) {
			if (other.codigoSucursal != null)
				return false;
		} else if (!codigoSucursal.equals(other.codigoSucursal))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Sucursal [codigoSucursal=" + codigoSucursal + ", nombre=" + nombre + "]";
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
}
