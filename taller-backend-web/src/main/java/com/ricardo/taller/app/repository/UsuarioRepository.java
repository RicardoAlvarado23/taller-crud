package com.ricardo.taller.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ricardo.taller.app.entity.Usuario;

/**
 * Interfaz repositorio de Usuario
 * @author ricardo
 * 
 *
 */
public interface UsuarioRepository extends JpaRepository<Usuario, String> {

	Optional<Usuario> findByUser(String user);
	Optional<Usuario> findByNombreIgnoreCase(String nombre);
	Optional<Usuario> findByUserIgnoreCase(String user);
}
