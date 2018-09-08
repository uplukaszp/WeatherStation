package pl.uplukaszp.config.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfigurationSource;

import pl.uplukaszp.config.security.filters.JWTAuthenticationFilter;
import pl.uplukaszp.config.security.filters.JWTAuthorizationFilter;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
	private CorsConfigurationSource corssource;
	private JWTAuthenticationFilter authentitacionFilter;
	private JWTAuthorizationFilter authorizationFilter;

	public WebSecurity(CorsConfigurationSource corssource, JWTAuthenticationFilter authentitacionFilter,
			JWTAuthorizationFilter authorizationFilter) {
		this.corssource = corssource;
		this.authentitacionFilter = authentitacionFilter;
		this.authorizationFilter=authorizationFilter;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().cors().configurationSource(corssource).and().authorizeRequests()
				.antMatchers(HttpMethod.POST, "/user").permitAll().antMatchers("/login").permitAll().anyRequest()
				.authenticated().and().addFilter(authentitacionFilter).addFilter(authorizationFilter)
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	
}