package pl.uplukaszp.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import pl.uplukaszp.domain.UserData;
import pl.uplukaszp.repo.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
    private UserRepository applicationUserRepository;

    public UserDetailsServiceImpl(UserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserData applicationUser = applicationUserRepository.findByEmail(username);
        System.err.println(applicationUser);
        if (applicationUser == null) {
            throw new UsernameNotFoundException(username);
        }
        
        return new User(applicationUser.getEmail(), applicationUser.getPassword(), new ArrayList<>());
    }
}