package emanueleCozzolino.u5w2d5.services;

import emanueleCozzolino.u5w2d5.entities.Dipendente;
import emanueleCozzolino.u5w2d5.entities.Prenotazione;
import emanueleCozzolino.u5w2d5.entities.Viaggio;
import emanueleCozzolino.u5w2d5.exceptions.BadRequestException;
import emanueleCozzolino.u5w2d5.exceptions.NotFoundException;
import emanueleCozzolino.u5w2d5.payloads.PrenotazioneDTO;
import emanueleCozzolino.u5w2d5.repositories.PrenotazioniRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class PrenotazioniService {
	private final PrenotazioniRepository prenotazioniRepository;
	private final ViaggiService viaggiService;
	private final DipendentiService dipendentiService;

	@Autowired
	public PrenotazioniService(PrenotazioniRepository prenotazioniRepository, ViaggiService viaggiService, DipendentiService dipendentiService) {
		this.prenotazioniRepository = prenotazioniRepository;
		this.viaggiService = viaggiService;
		this.dipendentiService = dipendentiService;
	}

	public Prenotazione save(PrenotazioneDTO payload) {
		//Cerco il viaggio e il dipendente nel db
		Viaggio viaggio = this.viaggiService.findById(payload.viaggioId());
		Dipendente dipendente = this.dipendentiService.findById(payload.dipendenteId());

		//Controllo che il dipendente non abbia gia una prenotazione per la stessa data
		if (this.prenotazioniRepository.existsByDipendenteIdAndViaggioData(dipendente.getId(), viaggio.getData())) {
			throw new BadRequestException("Il dipendente " + dipendente.getNome() + " " + dipendente.getCognome() + " ha gia' una prenotazione per la data " + viaggio.getData());
		}
		//Creo la nuova prenotazione
		Prenotazione newPrenotazione = new Prenotazione(viaggio, dipendente, payload.note());
		//Salvo
		Prenotazione savedPrenotazione = this.prenotazioniRepository.save(newPrenotazione);
		log.info("La prenotazione con id " + savedPrenotazione.getId() + " e' stata salvata correttamente!");
		//Ritorno la prenotazione salvata
		return savedPrenotazione;
	}

	public Page<Prenotazione> findAll(int page, int size, String orderBy, String sortCriteria) {
		if (size > 100 || size < 0) size = 10;
		if (page < 0) page = 0;
		Pageable pageable = PageRequest.of(page, size, sortCriteria.equals("desc") ? Sort.by(orderBy).descending() : Sort.by(orderBy));
		return this.prenotazioniRepository.findAll(pageable);
	}

	public Prenotazione findById(UUID prenotazioneId) {
		return this.prenotazioniRepository.findById(prenotazioneId).orElseThrow(() -> new NotFoundException(prenotazioneId));
	}

	public void findByIdAndDelete(UUID prenotazioneId) {
		Prenotazione found = this.findById(prenotazioneId);
		this.prenotazioniRepository.delete(found);
	}
}
