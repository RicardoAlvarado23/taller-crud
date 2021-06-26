package com.ricardo.taller.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ricardo.taller.app.entity.Producto;

/**
 * Interfaz repositorio de Producto
 * @author ricardo
 * 
 *
 */
public interface ProductoRepository extends JpaRepository<Producto, String> {
	Optional<Producto> findByNombreIgnoreCase(String nombre);
}
