package emanueleCozzolino.u5w2d5.security;

import emanueleCozzolino.u5w2d5.entities.Dipendente;
import emanueleCozzolino.u5w2d5.exceptions.UnauthorizedException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class JWTTools {
	@Value("${jwt.secret}")
	private String secret;

	public String generateToken(Dipendente dipendente) {
		return Jwts.builder()
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // 7 giorni
				.subject(String.valueOf(dipendente.getId()))
				.signWith(Keys.hmacShaKeyFor(secret.getBytes()))
				.compact();
	}

	public void verifyToken(String token) {
		try {
			Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parse(token);
		} catch (Exception ex) {
			throw new UnauthorizedException("Problemi col token! Effettua di nuovo il login!");
		}
	}

	public UUID extractIdFromToken(String token) {
		return UUID.fromString(Jwts.parser()
				.verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.getSubject());
	}
}
