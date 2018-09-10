package pl.uplukaszp.config.beans;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import pl.uplukaszp.domain.UserData;
import pl.uplukaszp.repo.UserRepository;

@Component
public class MyAuthManager implements AuthenticationManager {

	UserRepository repo;
	BCryptPasswordEncoder encoder;

	public MyAuthManager(UserRepository repo, BCryptPasswordEncoder encoder) {
		this.repo = repo;
		this.encoder = encoder;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UserData user = repo.findByEmail((String) authentication.getPrincipal());
		if (user == null)
			throw new DisabledException("Acount does not exist");
		if (authentication.getCredentials() != null)
			if (!encoder.matches((String) authentication.getCredentials(), user.getPassword())) {
				throw new BadCredentialsException("Password incorrect");
			}
		return authentication;
	}

}
