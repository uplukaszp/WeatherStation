package pl.uplukaszp.config.beans;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityBeans {

	@Value("${settings.allowed_orgins}")
	private String[] allowedOrgins;

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/** Allow access to every resource only my web site */
	private CorsConfiguration blockNotMyDomain() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList(allowedOrgins));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "content-type"));
		configuration.setExposedHeaders(Arrays.asList("Authorization"));
		configuration.setAllowCredentials(true);
		return configuration;
	}

	/** Allows sending data from any client */
	private CorsConfiguration allowAllToUploadData() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("POST"));
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "content-type"));
		configuration.setExposedHeaders(Arrays.asList("Authorization"));
		return configuration;
	}

	@Bean
	@Primary
	CorsConfigurationSource corsConfigurationSource() {

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/measurement", allowAllToUploadData());
		source.registerCorsConfiguration("/**", blockNotMyDomain());
		return source;
	}

}
