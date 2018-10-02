package pl.uplukaszp.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import pl.uplukaszp.dto.PasswordDTO;
import pl.uplukaszp.dto.UserDataDTO;
import pl.uplukaszp.services.UserDetailsServiceImpl;
import pl.uplukaszp.util.ValidationErrorParser;

@RestController
public class UserController {

	@Autowired
	private UserDetailsServiceImpl userService;

	/**
	 * Registers new user
	 * 
	 * @return @see ValidationErrorParser#parseErrors(Errors)
	 */
	@PostMapping("/user")
	public ResponseEntity<Map<String, String>> signUp(@Valid @RequestBody UserDataDTO user, Errors errors) {
		if (errors.hasErrors()) {
			return ResponseEntity.badRequest().body(ValidationErrorParser.parseErrors(errors));
		}
		return userService.save(user);
	}

	/**
	 * Changes the user's password
	 * 
	 * @return @see ValidationErrorParser#parseErrors(Errors)
	 */
	@PutMapping("/user")
	public ResponseEntity<Map<String, String>> changeUserPassword(Authentication auth,
			@RequestBody @Valid PasswordDTO password, Errors errors) {
		if (errors.hasErrors()) {
			return ResponseEntity.badRequest().body(ValidationErrorParser.parseErrors(errors));
		}
		return userService.changePassword(auth.getName(), password.getPassword());

	}

	/**
	 * Removes the user's account
	 * 
	 * @return @see ValidationErrorParser#parseErrors(Errors)
	 */
	@DeleteMapping("/user")
	public HttpStatus deleteUser(Authentication auth) {
		userService.delete(auth.getName());
		return HttpStatus.OK;
	}

}
