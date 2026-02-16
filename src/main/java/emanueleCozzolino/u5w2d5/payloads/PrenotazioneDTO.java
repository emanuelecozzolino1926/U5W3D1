package emanueleCozzolino.u5w2d5.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PrenotazioneDTO(
		@NotNull(message = "L'id del viaggio e' un campo obbligatorio")
		UUID viaggioId,
		@NotNull(message = "L'id del dipendente e' un campo obbligatorio")
		UUID dipendenteId,
		@NotBlank(message = "Le note sono un campo obbligatorio")
		String note) {
}
