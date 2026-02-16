package emanueleCozzolino.u5w2d5.exceptions;

import java.util.UUID;

public class NotFoundException extends RuntimeException {
	public NotFoundException(UUID id) {
		super("La risorsa con id " + id + " non e' stata trovata");
	}
}
