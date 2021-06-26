package com.ricardo.taller.app.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ricardo.taller.app.enums.StatusRest;

public class UtilFunctions {
	

	/**
	 * Obtener fecha hora actual.
	 * 
	 * @return the timestamp
	 */
	public static Timestamp obtenerFechaHoraActual() {
		java.util.Date date = new java.util.Date();
		Timestamp timeStampTransaction = new Timestamp(date.getTime());
		return timeStampTransaction;
	}

	/**
	 * Completar numero con ceros.
	 * 
	 * @param len   the len
	 * @param value the value
	 * @return the string
	 */
	public static String completarNumeroConCeros(Integer len, Integer value) {
		return String.format("%0" + len + "d", value);
	}

	/**
	 * Obtener formato fecha.
	 * 
	 * @param Fecha the fecha
	 * @return the string
	 */
	public static String obtenerFormatoFecha(Timestamp Fecha) {
		String fecha = null;
		try {
			fecha = new SimpleDateFormat("dd/MM/yyyy").format(Fecha);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fecha;
	}

	/**
	 * Encriptar A md 5.
	 * 
	 * @param input the input
	 * @return the string
	 */
	public static String encriptarAMd5(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(input.getBytes("UTF-8"));
			BigInteger number = new BigInteger(1, messageDigest);
			String hashtext = number.toString(16);
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext;
		} catch (UnsupportedEncodingException e) {
			return null;
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

	/**
	 * Checks if is number.
	 * 
	 * @param cadena the cadena
	 * @return true, if is number
	 */
	public static boolean isNumber(String cadena) {
		boolean isDecimal = false;
		if (cadena == null || cadena.isEmpty()) {
			return false;
		}
		int i = 0;
		if (cadena.charAt(0) == '-') {
			if (cadena.length() > 1) {
				i++;
			} else {
				return false;
			}
		}
		for (; i < cadena.length(); i++) {
			if (!Character.isDigit(cadena.charAt(i))) {
				if (!isDecimal && (cadena.charAt(i) != '.' || cadena.charAt(i) != ',')) {
					isDecimal = true;
					continue;
				}
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @param objeto
	 * @return
	 */
	public static String verifyString(Object objeto) {
		if (objeto == null) {
			return StringUtils.EMPTY;
		}
		return objeto.toString();
	}


	/**
	 * Convertir Map a Json
	 * 
	 * @param map
	 * @return
	 */
	public static String mapToJson(Map map) {
		try {
			String mapAsJson = new ObjectMapper().writeValueAsString(map);
			return mapAsJson;
		} catch (JsonProcessingException ex) {
		}
		return StringUtils.EMPTY;
	}

	/**
	 * Contertir JSON String a Map
	 * 
	 * @param json
	 * @return
	 */
	public static Map<String, Object> jsonToMap(String json) {
		ObjectMapper mapper = new ObjectMapper();
		TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
		};
		Map<String, Object> map = null;
		try {
			map = mapper.readValue(json, typeRef);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

	public static boolean validateIsNotNullAndNotEmpty(Object o) {
		return (validateIsNotNull(o) && validateIsNotEmpty(o));
	}

	public static boolean validateIsNullAndEmpty(Object o) {
		return (validateIsNull(o) && validateIsEmpty(o));
	}

	public static boolean validateIsNullOrEmpty(Object o) {
		return (validateIsNull(o) || validateIsEmpty(o));
	}

	public static boolean validateIsNotNull(Object o) {
		return !validateIsNull(o);
	}

	public static boolean validateIsNull(Object o) {
		return o == null;
	}

	public static boolean validateIsEmpty(Object o) {
		String d = o.toString();
		return d.trim().length() == 0;
	}

	public static boolean validateIsNotEmpty(Object o) {
		return !validateIsEmpty(o);
	}

	public static boolean validateIsNotNullAndPositive(Integer num) {
		return (validateIsNotNull(num) && validateIsPositiveNumber(num));
	}

	public static boolean validateIsNotNullAndPositive(Long num) {
		return (validateIsNotNull(num) && validateIsPositiveNumber(num));
	}

	public static boolean validateIsNotNullAndPositive(BigDecimal num) {
		return (validateIsNotNull(num) && validateIsPositiveNumber(num));
	}

	public static boolean validateIsPositiveNumber(Integer num) {
		return num > 0;
	}

	public static boolean validateIsPositiveNumber(Long num) {
		return num > 0;
	}

	public static boolean validateIsPositiveNumber(BigDecimal num) {
		return BigDecimal.ZERO.compareTo(num) < 0;
	}

	
	/**
	 * Crea un mensaje con un estado determinado por el usuario (status, message)
	 * 
	 * @param errorCode
	 * @param message
	 * @return
	 */
	public static Map<String, Object> createMessage(String errorCode, String message) {
		Map<String, Object> respuesta = new HashMap<String, Object>();
		respuesta.put(UtilConstants.SUCCESS,
				errorCode.equals(StatusRest.EXITO.getCode()) );
		respuesta.put(UtilConstants.STATUS, errorCode);
		respuesta.put(UtilConstants.MESSAGE, message);
		return respuesta;
	}

	/**
	 * Crea un mensaje de Error
	 * 
	 * @param message
	 * @return
	 */
	public static Map<String, Object> createMessageError(String message) {
		Map<String, Object> respuesta = new HashMap<String, Object>();
		respuesta.put(UtilConstants.SUCCESS, false);
		respuesta.put(UtilConstants.STATUS, StatusRest.ERROR.getCode());
		respuesta.put(UtilConstants.MESSAGE, message);
		return respuesta;
	}

	/**
	 * Crea un mensaje satisfactorio
	 * 
	 * @param message
	 * @return
	 */
	public static Map<String, Object> createMessageOk(String message) {
		Map<String, Object> respuesta = new HashMap<String, Object>();
		respuesta.put(UtilConstants.SUCCESS, true);
		respuesta.put(UtilConstants.STATUS, StatusRest.EXITO.getCode());
		respuesta.put(UtilConstants.MESSAGE, message);
		return respuesta;
	}

	/**
	 * Mensaje Satisfactorio
	 * 
	 * @return
	 */
	public static Map<String, Object> createMessageOk() {
		return createMessageOk("Proceso satisfactorio.");
	}

	/**
	 * Crea mensaje de un Result
	 * 
	 * @param result
	 * @return
	 */
	public static Map<String, Object> createMessage(Map<String, Object> result) {
		String status = result.get(UtilConstants.STATUS).toString();
		String message = result.get(UtilConstants.MESSAGE).toString();
		Map<String, Object> response = createMessage(status, message);
		return response;
	}
	
	
}
