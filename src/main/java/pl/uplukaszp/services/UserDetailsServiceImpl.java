package pl.uplukaszp.services;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import pl.uplukaszp.config.beans.TokenUtility;
import pl.uplukaszp.domain.ApiKey;
import pl.uplukaszp.domain.UserData;
import pl.uplukaszp.dto.UserDataDTO;
import pl.uplukaszp.repo.UserRepository;
import pl.uplukaszp.util.ValidationErrorParser;

/**
 * Loads user data, necessary during the authentication process
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserRepository applicationUserRepository;

	@Autowired
	private ApiKeyServiceImpl apiService;

	public UserDetailsServiceImpl(UserRepository applicationUserRepository) {
		this.applicationUserRepository = applicationUserRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserData applicationUser = applicationUserRepository.findByEmail(username);
		if (applicationUser == null) {
			throw new UsernameNotFoundException(username);
		}
		return new User(applicationUser.getEmail(), applicationUser.getPassword(), new ArrayList<>());
	}

	public UserData findByEmail(String name) {
		return applicationUserRepository.findByEmail(name);
	}

	public ResponseEntity<Map<String, String>> save(UserDataDTO user) {
		if (applicationUserRepository.findByEmail(user.getEmail()) == null) {
			UserData u = new UserData();
			u.setEmail(user.getEmail());
			u.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			u = applicationUserRepository.save(u);
			generateApiKey(u);
			return new ResponseEntity<Map<String, String>>(HttpStatus.OK);
		}
		return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(ValidationErrorParser.parseError("email", "User already exist"));
	}

	private void generateApiKey(UserData user) {
		ApiKey key = new ApiKey();
		key.setOwner(user);
		key.setAccessKey(TokenUtility.createToken(user.getEmail(), null));
		apiService.save(key);
	}

	public ResponseEntity<Map<String, String>> changePassword(String name, String password) {
		UserData u = applicationUserRepository.findByEmail(name);
		u.setPassword(bCryptPasswordEncoder.encode(password));
		applicationUserRepository.save(u);
		return new ResponseEntity<Map<String, String>>(HttpStatus.OK);
	}

	public void delete(String user) {
		applicationUserRepository.delete(applicationUserRepository.findByEmail(user));

	}
}