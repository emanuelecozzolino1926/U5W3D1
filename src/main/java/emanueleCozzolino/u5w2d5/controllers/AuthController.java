package emanueleCozzolino.u5w2d5.controllers;

import emanueleCozzolino.u5w2d5.entities.Dipendente;
import emanueleCozzolino.u5w2d5.exceptions.ValidationException;
import emanueleCozzolino.u5w2d5.payloads.DipendenteDTO;
import emanueleCozzolino.u5w2d5.payloads.LoginDTO;
import emanueleCozzolino.u5w2d5.payloads.LoginResponseDTO;
import emanueleCozzolino.u5w2d5.services.AuthService;
import emanueleCozzolino.u5w2d5.services.DipendentiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
	private final AuthService authService;
	private final DipendentiService dipendentiService;

	@Autowired
	public AuthController(AuthService authService, DipendentiService dipendentiService) {
		this.authService = authService;
		this.dipendentiService = dipendentiService;
	}

	// POST http://localhost:3001/auth/login
	@PostMapping("/login")
	public LoginResponseDTO login(@RequestBody LoginDTO body) {
		return new LoginResponseDTO(this.authService.checkCredentialsAndGenerateToken(body));
	}

	// POST http://localhost:3001/auth/register
	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public Dipendente register(@RequestBody @Validated DipendenteDTO payload, BindingResult validationResult) {
		if (validationResult.hasErrors()) {
			List<String> errorsList = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
			throw new ValidationException(errorsList);
		}
		return this.dipendentiService.save(payload);
	}
}
