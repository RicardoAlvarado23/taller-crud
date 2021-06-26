package com.ricardo.taller.app.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * Entidad de producto
 * @author ricardo
 */
@Entity
@Table(name = "producto", schema = "crud")
public class Producto implements Serializable {


	@Id
	@Column(unique = true, nullable = false, name = "cod_producto", length = 2)
	private String codigoProducto;
	
	@Column(nullable = false, length = 50)
	private String nombre;
	
	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal precio;

	public String getCodigoProducto() {
		return codigoProducto;
	}

	public void setCodigoProducto(String codigoProducto) {
		this.codigoProducto = codigoProducto;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public BigDecimal getPrecio() {
		return precio;
	}

	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigoProducto == null) ? 0 : codigoProducto.hashCode());
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
		Producto other = (Producto) obj;
		if (codigoProducto == null) {
			if (other.codigoProducto != null)
				return false;
		} else if (!codigoProducto.equals(other.codigoProducto))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Producto [codigoProducto=" + codigoProducto + ", nombre=" + nombre + ", precio=" + precio + "]";
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
}
