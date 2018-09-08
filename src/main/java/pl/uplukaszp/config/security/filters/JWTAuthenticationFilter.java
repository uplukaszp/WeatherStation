package pl.uplukaszp.config.security.filters;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import pl.uplukaszp.config.beans.MyAuthManager;
import pl.uplukaszp.config.beans.TokenUtility;
import pl.uplukaszp.domain.UserData;

@Component
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

	@Value("${settings.expiration_time}")
	private long EXPIRATION_TIME;

	@Value("${settings.header}")
	private String HEADER_STRING;
	@Value("${settings.token_prefix}")
	private String TOKEN_PREFIX;

	public JWTAuthenticationFilter(MyAuthManager authenticationManager) {
		this.authenticationManager = authenticationManager;
		super.setAuthenticationManager(authenticationManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {

			UserData creds = new ObjectMapper().readValue(request.getInputStream(), UserData.class);
			return authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication auth) throws IOException, ServletException {

		String token = TokenUtility.createToken(auth.getName(), EXPIRATION_TIME);
		response.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + token);
		System.out.println("successfulAuth()+ " + token + " exp time " + EXPIRATION_TIME);
	}

}