package com.ricardo.taller.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ricardo.taller.app.entity.GeneradorCodigo;

public interface GeneradorCodigoRepository extends JpaRepository<GeneradorCodigo, Long> {

	Optional<GeneradorCodigo> findByTabla(String tabla);
	
}
