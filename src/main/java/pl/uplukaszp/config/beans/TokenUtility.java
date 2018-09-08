package pl.uplukaszp.config.beans;

import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenUtility {

	private static final SignatureAlgorithm algorithm = SignatureAlgorithm.HS512;

	private static String secret;

	@Value("${settings.secret_key}")
	public void setSecret(String secret) {
		TokenUtility.secret = secret;
	}

	public static String createToken(String username, Long expirationTime) {

		Date expirationDate = (expirationTime == null) ? null : new Date(System.currentTimeMillis() + expirationTime);

		String token = Jwts.builder().setSubject(username).setExpiration(expirationDate)
				.signWith(algorithm, Base64.getEncoder().encodeToString(secret.getBytes())).compact();
		return token;
	}

	public static String getUserFromToken(String token) {
		String user = Jwts.parser().setSigningKey(Base64.getEncoder().encodeToString(secret.getBytes()))
				.parseClaimsJws(token).getBody().getSubject();
		return user;
	}
}
