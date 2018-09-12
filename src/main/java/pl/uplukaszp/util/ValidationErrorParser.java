package pl.uplukaszp.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

public class ValidationErrorParser {

	/**
	 * Converts Errors object to map, where key is a name of field with wrong value,
	 * and value is a description of a problem related with this field
	 */
	public static Map<String, String> parseErrors(Errors errors) {
		Map<String, String> m = new HashMap<>();
		for (ObjectError objectError : errors.getAllErrors()) {
			FieldError error = (FieldError) objectError;
			m.put(error.getField(), objectError.getDefaultMessage());
		}
		return m;
	}

	/**
	 * Creates a map, where key is a name of field with wrong value, and value is a
	 * description of a problem related with this field
	 */
	public static Map<String, String> parseError(String field, String error) {
		Map<String, String> m = new HashMap<>();
		m.put(field, error);
		return m;
	}
}
