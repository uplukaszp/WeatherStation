package pl.uplukaszp.config.security.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import pl.uplukaszp.config.beans.MyAuthManager;
import pl.uplukaszp.config.beans.TokenUtility;

/** Used when authorization is based on tokens */
@Component
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	@Value("${settings.header}")
	private String HEADER_STRING;
	@Value("${settings.token_prefix}")
	private String TOKEN_PREFIX;

	public JWTAuthorizationFilter(MyAuthManager authManager) {
		super(authManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		String header = req.getHeader(HEADER_STRING);

		if (header == null || !header.startsWith(TOKEN_PREFIX)) {
			chain.doFilter(req, res);
			return;
		}
		UsernamePasswordAuthenticationToken tokenAuthentication = new UsernamePasswordAuthenticationToken(
				getUserName(req), null, null);
		Authentication authentication = getAuthenticationManager().authenticate(tokenAuthentication);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		chain.doFilter(req, res);
	}

	private String getUserName(HttpServletRequest request) {
		String token = request.getHeader(HEADER_STRING);
		if (token != null) {
			token = token.replace(TOKEN_PREFIX + " ", "");
			return TokenUtility.getUserFromToken(token);
		}
		return null;
	}

}
