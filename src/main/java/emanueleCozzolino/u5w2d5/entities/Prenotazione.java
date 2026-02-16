package emanueleCozzolino.u5w2d5.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "prenotazioni")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Prenotazione {
	@Id
	@GeneratedValue
	@Setter(AccessLevel.NONE)
	private UUID id;

	@ManyToOne
	@JoinColumn(name = "viaggio_id")
	private Viaggio viaggio;

	@ManyToOne
	@JoinColumn(name = "dipendente_id")
	private Dipendente dipendente;

	private LocalDate dataRichiesta;
	private String note;

	public Prenotazione(Viaggio viaggio, Dipendente dipendente, String note) {
		this.viaggio = viaggio;
		this.dipendente = dipendente;
		this.dataRichiesta = LocalDate.now();
		this.note = note;
	}
}
