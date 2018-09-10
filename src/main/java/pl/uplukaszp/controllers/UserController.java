package pl.uplukaszp.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import pl.uplukaszp.config.beans.TokenUtility;
import pl.uplukaszp.domain.ApiKey;
import pl.uplukaszp.domain.UserData;
import pl.uplukaszp.dto.PasswordDTO;
import pl.uplukaszp.dto.UserDataDTO;
import pl.uplukaszp.repo.ApiKeyRepository;
import pl.uplukaszp.repo.UserRepository;
import pl.uplukaszp.util.ValidationErrorParser;

@RestController
public class UserController {

	@Autowired
	private UserRepository repo;
	
	@Autowired
	private ApiKeyRepository apiRepo;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	private void generateApiKey(UserData user)
	{
		ApiKey key=new ApiKey();
		key.setOwner(user);
		key.setAccessKey(TokenUtility.createToken(user.getEmail(), null));
		apiRepo.save(key);
	}
	@PostMapping("/user")
	public ResponseEntity<Map<String,String>> signUp(@Valid @RequestBody UserDataDTO user, Errors errors) {
		if (errors.hasErrors()) {
			return ResponseEntity.badRequest().body(ValidationErrorParser.parseErrors(errors));
		}
		if (repo.findByEmail(user.getEmail()) == null) {
			UserData u = new UserData();
			u.setEmail(user.getEmail());
			u.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			u=repo.save(u);
			generateApiKey(u);
			return new ResponseEntity<Map<String, String>>(HttpStatus.OK);
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ValidationErrorParser.parseError("email","User already exist"));
	}
	@PutMapping("/user")
	public ResponseEntity<Map<String,String>> changeUserPassword(Authentication auth,@RequestBody @Valid PasswordDTO password,Errors errors)
	{
		UserData u=repo.findByEmail(auth.getName());
		if (errors.hasErrors()) {
			return ResponseEntity.badRequest().body(ValidationErrorParser.parseErrors(errors));
		}
		u.setPassword(bCryptPasswordEncoder.encode(password.getPassword()));
		repo.save(u);
		return new ResponseEntity<Map<String, String>>(HttpStatus.OK);	
	}
	@DeleteMapping("/user")
	public ResponseEntity<Map<String,String>> changeUserPassword(Authentication auth){
		UserData u=repo.findByEmail(auth.getName());
		repo.delete(u);
		//TODO remove all user data ( sensors, measurments, etc.)
		return new ResponseEntity<Map<String, String>>(HttpStatus.OK);	

	}

}
