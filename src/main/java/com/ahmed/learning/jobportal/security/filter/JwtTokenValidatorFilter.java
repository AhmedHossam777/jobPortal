package com.ahmed.learning.jobportal.security.filter;

import com.ahmed.learning.jobportal.constants.ApplicationConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class JwtTokenValidatorFilter extends OncePerRequestFilter {
	private static final String BEARER_PREFIX = "Bearer ";

	private final List<String> publicPaths;
	private final SecretKey secretKey;
	private final AntPathMatcher pathMatcher = new AntPathMatcher();

	private final Logger logger = LoggerFactory.getLogger(JwtTokenValidatorFilter.class);

	public JwtTokenValidatorFilter(List<String> publicPaths, String secret) {
		this.publicPaths = publicPaths;
		this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
	                                FilterChain filterChain)
					throws ServletException, IOException {
		String authHeader = request.getHeader(ApplicationConstants.JWT_HEADER);

		// no bearer token here: leave the context anonymous and let the authorization
		// rules reject the request, so basic auth still has a chance to run
		if (null == authHeader || !authHeader.startsWith(BEARER_PREFIX)) {
			filterChain.doFilter(request, response);
			return;
		}

		String jwt = authHeader.substring(BEARER_PREFIX.length()).trim();
		try {
			Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(jwt).getPayload();

			String username = String.valueOf(claims.get("username"));
			String roles = String.valueOf(claims.get("roles"));
			logger.debug("Authenticated {} from JWT with roles {}", username, roles);

			Authentication authentication = new UsernamePasswordAuthenticationToken(username, null,
							AuthorityUtils.commaSeparatedStringToAuthorityList(roles));

			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (ExpiredJwtException e) {
			rejectRequest(response, "Token has expired");
			return;
		} catch (JwtException | IllegalArgumentException e) {
			logger.warn("Rejected JWT: {}", e.getMessage());
			rejectRequest(response, "Invalid token");
			return;
		}

		// this is like next() in express
		filterChain.doFilter(request, response);
	}

	private void rejectRequest(HttpServletResponse response, String message) throws IOException {
		// ExceptionTranslationFilter sits after this filter, so it cannot turn an
		// exception thrown here into a 401 - write the response ourselves
		SecurityContextHolder.clearContext();
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("text/plain;charset=UTF-8");
		response.getWriter().write(message);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getRequestURI();

		return publicPaths.stream().anyMatch(publicPath -> pathMatcher.match(publicPath, path));
	}
}
