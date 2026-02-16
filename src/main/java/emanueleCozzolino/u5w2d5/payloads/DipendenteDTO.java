package emanueleCozzolino.u5w2d5.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record DipendenteDTO(
		@NotBlank(message = "Lo username e' un campo obbligatorio")
		@Size(min = 3, max = 30, message = "Lo username deve essere tra i 3 e i 30 caratteri")
		String username,
		@NotBlank(message = "Il nome e' un campo obbligatorio")
		@Size(min = 2, max = 30, message = "Il nome deve essere tra i 2 e i 30 caratteri")
		String nome,
		@NotBlank(message = "Il cognome e' un campo obbligatorio")
		@Size(min = 2, max = 30, message = "Il cognome deve essere tra i 2 e i 30 caratteri")
		String cognome,
		@NotBlank(message = "L'email e' obbligatoria")
		@Email(message = "L'indirizzo email inserito non e' nel formato corretto!")
		String email,
		@NotBlank(message = "La password e' obbligatoria")
		@Size(min = 4, message = "La password deve avere almeno 4 caratteri")
		@Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{4,}$",
				message = "La password deve contenere una maiuscola, una minuscola e un numero")
		String password) {
}
