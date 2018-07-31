package pl.uplukaszp.config.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import pl.uplukaszp.repo.UserRepository;

@Component
public class MyAuthManager implements AuthenticationManager{

	@Autowired
	UserRepository repo;
	

	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		System.err.println("authentication");
		System.err.println(authentication.getPrincipal());
		if(repo.findByEmail((String) authentication.getPrincipal())==null)throw new DisabledException("Acount does not exist"); 
		return authentication;
	}

}
