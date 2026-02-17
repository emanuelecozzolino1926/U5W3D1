package emanueleCozzolino.u5w2d5.services;

import emanueleCozzolino.u5w2d5.entities.Dipendente;
import emanueleCozzolino.u5w2d5.exceptions.UnauthorizedException;
import emanueleCozzolino.u5w2d5.payloads.LoginDTO;
import emanueleCozzolino.u5w2d5.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
	private final DipendentiService dipendentiService;
	private final JWTTools jwtTools;
	private final PasswordEncoder bcrypt;

	@Autowired
	public AuthService(DipendentiService dipendentiService, JWTTools jwtTools, PasswordEncoder bcrypt) {
		this.dipendentiService = dipendentiService;
		this.jwtTools = jwtTools;
		this.bcrypt = bcrypt;
	}

	public String checkCredentialsAndGenerateToken(LoginDTO body) {
		// 1. Cerco il dipendente per email
		Dipendente found = this.dipendentiService.findByEmail(body.email());

		// 2. Confronto le password con BCrypt
		if (bcrypt.matches(body.password(), found.getPassword())) {
			String accessToken = jwtTools.generateToken(found);
			return accessToken;
		} else {
			throw new UnauthorizedException("Credenziali errate!");
		}
	}
}
