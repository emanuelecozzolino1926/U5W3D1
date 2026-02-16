package emanueleCozzolino.u5w2d5.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ViaggioDTO(
		@NotBlank(message = "La destinazione e' un campo obbligatorio")
		@Size(min = 2, max = 100, message = "La destinazione deve essere tra i 2 e i 100 caratteri")
		String destinazione,
		@NotNull(message = "La data e' un campo obbligatorio")
		LocalDate data) {
}
