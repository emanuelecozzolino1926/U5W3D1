package emanueleCozzolino.u5w2d5.services;

import emanueleCozzolino.u5w2d5.entities.Dipendente;
import emanueleCozzolino.u5w2d5.exceptions.UnauthorizedException;
import emanueleCozzolino.u5w2d5.payloads.LoginDTO;
import emanueleCozzolino.u5w2d5.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
	private final DipendentiService dipendentiService;
	private final JWTTools jwtTools;

	@Autowired
	public AuthService(DipendentiService dipendentiService, JWTTools jwtTools) {
		this.dipendentiService = dipendentiService;
		this.jwtTools = jwtTools;
	}

	public String checkCredentialsAndGenerateToken(LoginDTO body) {
		Dipendente found = this.dipendentiService.findByEmail(body.email());
		if (found.getPassword().equals(body.password())) {
			String accessToken = jwtTools.generateToken(found);
			return accessToken;
		} else {
			throw new UnauthorizedException("Credenziali errate!");
		}
	}
}
