package com.ricardo.taller.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ricardo.taller.app.entity.Sucursal;

/**
 * Interfaz repositorio de Sucusal
 * @author ricardo
 * 
 *
 */
public interface SucursalRepository extends JpaRepository<Sucursal, String> {
	Optional<Sucursal> findByNombreIgnoreCase(String nombre);
}
