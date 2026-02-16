package emanueleCozzolino.u5w2d5.repositories;

import emanueleCozzolino.u5w2d5.entities.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface PrenotazioniRepository extends JpaRepository<Prenotazione, UUID> {
	boolean existsByDipendenteIdAndViaggioData(UUID dipendenteId, LocalDate data);
}
