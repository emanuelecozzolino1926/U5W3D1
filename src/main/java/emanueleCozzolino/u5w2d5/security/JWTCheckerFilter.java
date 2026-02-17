package emanueleCozzolino.u5w2d5.security;

import emanueleCozzolino.u5w2d5.entities.Dipendente;
import emanueleCozzolino.u5w2d5.exceptions.UnauthorizedException;
import emanueleCozzolino.u5w2d5.services.DipendentiService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class JWTCheckerFilter extends OncePerRequestFilter {

	private final JWTTools jwtTools;
	private final DipendentiService dipendentiService;

	@Autowired
	public JWTCheckerFilter(JWTTools jwtTools, DipendentiService dipendentiService) {
		this.jwtTools = jwtTools;
		this.dipendentiService = dipendentiService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		// 1. Verifico che l'Authorization header esista e sia nel formato "Bearer <token>"
		String authHeader = request.getHeader("Authorization");
		if (authHeader == null || !authHeader.startsWith("Bearer "))
			throw new UnauthorizedException("Inserire il token nell'Authorization header nel formato corretto!");

		// 2. Estraggo il token dall'header
		String accessToken = authHeader.replace("Bearer ", "");

		// 3. Verifico la validita' del token (firma + scadenza)
		jwtTools.verifyToken(accessToken);

		// 4. Estraggo l'id dell'utente dal token e cerco l'utente nel DB
		UUID dipendenteId = jwtTools.extractIdFromToken(accessToken);
		Dipendente authenticatedDipendente = this.dipendentiService.findById(dipendenteId);

		// 5. Setto l'autenticazione nel SecurityContext
		Authentication authentication = new UsernamePasswordAuthenticationToken(authenticatedDipendente, null, authenticatedDipendente.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// 6. Proseguo con la filter chain
		filterChain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return new AntPathMatcher().match("/auth/**", request.getServletPath());
	}
}
