package emanueleCozzolino.u5w2d5.services;

import emanueleCozzolino.u5w2d5.entities.Viaggio;
import emanueleCozzolino.u5w2d5.enums.StatoViaggio;
import emanueleCozzolino.u5w2d5.exceptions.NotFoundException;
import emanueleCozzolino.u5w2d5.payloads.ViaggioDTO;
import emanueleCozzolino.u5w2d5.repositories.ViaggiRepository;
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
public class ViaggiService {
	private final ViaggiRepository viaggiRepository;

	@Autowired
	public ViaggiService(ViaggiRepository viaggiRepository) {
		this.viaggiRepository = viaggiRepository;
	}

	public Viaggio save(ViaggioDTO payload) {
		//Creo il nuovo viaggio
		Viaggio newViaggio = new Viaggio(payload.destinazione(), payload.data());
		//Salvo
		Viaggio savedViaggio = this.viaggiRepository.save(newViaggio);
		log.info("Il viaggio con id " + savedViaggio.getId() + " e' stato salvato correttamente!");
		//Ritorno il viaggio salvato
		return savedViaggio;
	}

	public Page<Viaggio> findAll(int page, int size, String orderBy, String sortCriteria) {
		if (size > 100 || size < 0) size = 10;
		if (page < 0) page = 0;
		Pageable pageable = PageRequest.of(page, size, sortCriteria.equals("desc") ? Sort.by(orderBy).descending() : Sort.by(orderBy));
		return this.viaggiRepository.findAll(pageable);
	}

	public Viaggio findById(UUID viaggioId) {
		return this.viaggiRepository.findById(viaggioId).orElseThrow(() -> new NotFoundException(viaggioId));
	}

	public Viaggio findByIdAndUpdate(UUID viaggioId, ViaggioDTO payload) {
		//Cerco il viaggio nel db
		Viaggio found = this.findById(viaggioId);
		//Modifico il viaggio trovato
		found.setDestinazione(payload.destinazione());
		found.setData(payload.data());
		//Salvo
		Viaggio modifiedViaggio = this.viaggiRepository.save(found);
		log.info("Il viaggio con id " + modifiedViaggio.getId() + " e' stato modificato correttamente");
		// Ritorno il viaggio modificato
		return modifiedViaggio;
	}

	public void findByIdAndDelete(UUID viaggioId) {
		Viaggio found = this.findById(viaggioId);
		this.viaggiRepository.delete(found);
	}

	public Viaggio findByIdAndUpdateStato(UUID viaggioId, StatoViaggio stato) {
		//Cerco il viaggio nel db
		Viaggio found = this.findById(viaggioId);
		// Modifico lo stato
		found.setStato(stato);
		//Salvo
		Viaggio modifiedViaggio = this.viaggiRepository.save(found);
		log.info("Lo stato del viaggio con id " + modifiedViaggio.getId() + " e' stato aggiornato a " + stato);
		//Ritorno il viaggio modificato
		return modifiedViaggio;
	}
}
