package com.ricardo.taller.app.controller;

import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ricardo.taller.app.entity.Usuario;
import com.ricardo.taller.app.service.UsuarioService;
import com.ricardo.taller.app.util.UtilConstants;
import com.ricardo.taller.app.util.UtilFunctions;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private UsuarioService usuarioService;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, Object> parametros) {
		Map<String, Object> response = UtilFunctions.createMessageOk();
		String usuario = UtilFunctions.verifyString(parametros.get(UtilConstants.USUARIO));
		String clave = UtilFunctions.verifyString(parametros.get(UtilConstants.CLAVE));
		
		if (StringUtils.isEmpty(usuario) || StringUtils.isEmpty(clave)) {
			response = UtilFunctions.createMessageError("Parametros invalidos o incorrectos");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}
		
		Optional<Usuario> oUsuario = usuarioService.obtenerPorUsuario(usuario);
		Usuario usuarioBD = null;
		if (!oUsuario.isPresent()) {
			// Usuario solo para pruebas
			if (usuario.equalsIgnoreCase("admin") && clave.equalsIgnoreCase("123456")) {
				usuarioBD = new Usuario();
				usuarioBD.setNombre(usuario);
				usuarioBD.setPassword(clave);
			} else {
				response = UtilFunctions.createMessageError("No existe el usuario");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}
		} else {
			usuarioBD = oUsuario.get();
			String passwordMD5 = StringUtils.left(UtilFunctions.encriptarAMd5(clave), 15);
			if (!passwordMD5.equals(usuarioBD.getPassword())) {
				response = UtilFunctions.createMessageError("Credenciales inválidad");
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
			}
		}
		response.put(UtilConstants.USUARIO, usuarioBD);
		return ResponseEntity.ok(response);
	}
	
}
