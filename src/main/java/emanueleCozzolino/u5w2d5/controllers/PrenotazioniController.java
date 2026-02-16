package emanueleCozzolino.u5w2d5.controllers;

import emanueleCozzolino.u5w2d5.entities.Prenotazione;
import emanueleCozzolino.u5w2d5.exceptions.ValidationException;
import emanueleCozzolino.u5w2d5.payloads.PrenotazioneDTO;
import emanueleCozzolino.u5w2d5.services.PrenotazioniService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/prenotazioni")
public class PrenotazioniController {
	private final PrenotazioniService prenotazioniService;

	@Autowired
	public PrenotazioniController(PrenotazioniService prenotazioniService) {
		this.prenotazioniService = prenotazioniService;
	}

	// POST http://localhost:3001/prenotazioni
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Prenotazione createPrenotazione(@RequestBody @Validated PrenotazioneDTO payload, BindingResult validationResult) {
		if (validationResult.hasErrors()) {
			List<String> errorsList = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
			throw new ValidationException(errorsList);
		}
		return this.prenotazioniService.save(payload);
	}

	// GET http://localhost:3001/prenotazioni
	@GetMapping
	public Page<Prenotazione> findAll(@RequestParam(defaultValue = "0") int page,
									  @RequestParam(defaultValue = "10") int size,
									  @RequestParam(defaultValue = "dataRichiesta") String orderBy,
									  @RequestParam(defaultValue = "asc") String sortCriteria) {
		return this.prenotazioniService.findAll(page, size, orderBy, sortCriteria);
	}

	// GET http://localhost:3001/prenotazioni/{prenotazioneId}
	@GetMapping("/{prenotazioneId}")
	public Prenotazione findById(@PathVariable UUID prenotazioneId) {
		return this.prenotazioniService.findById(prenotazioneId);
	}

	// DELETE http://localhost:3001/prenotazioni/{prenotazioneId}
	@DeleteMapping("/{prenotazioneId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void findByIdAndDelete(@PathVariable UUID prenotazioneId) {
		this.prenotazioniService.findByIdAndDelete(prenotazioneId);
	}
}
