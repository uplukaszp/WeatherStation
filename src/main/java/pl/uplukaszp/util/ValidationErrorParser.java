package pl.uplukaszp.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

public class ValidationErrorParser {

	public static Map<String, String> parseErrors(Errors errors) {
		Map<String, String> m = new HashMap<>();
		for (ObjectError objectError : errors.getAllErrors()) {
			FieldError def = (FieldError) objectError;
			m.put(def.getField(), objectError.getDefaultMessage());
		}
		return m;

	}

	public static Map<String, String> parseError(String field,String error) {
		Map<String, String> m = new HashMap<>();
		m.put(field, error);
		return m;
	}
}
