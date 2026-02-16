package emanueleCozzolino.u5w2d5.controllers;

import emanueleCozzolino.u5w2d5.entities.Dipendente;
import emanueleCozzolino.u5w2d5.exceptions.ValidationException;
import emanueleCozzolino.u5w2d5.payloads.DipendenteDTO;
import emanueleCozzolino.u5w2d5.services.DipendentiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/dipendenti")
public class DipendentiController {
	private final DipendentiService dipendentiService;

	@Autowired
	public DipendentiController(DipendentiService dipendentiService) {
		this.dipendentiService = dipendentiService;
	}

	// POST http://localhost:3001/dipendenti
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Dipendente createDipendente(@RequestBody @Validated DipendenteDTO payload, BindingResult validationResult) {
		if (validationResult.hasErrors()) {
			List<String> errorsList = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
			throw new ValidationException(errorsList);
		}
		return this.dipendentiService.save(payload);
	}

	// GET http://localhost:3001/dipendenti
	@GetMapping
	public Page<Dipendente> findAll(@RequestParam(defaultValue = "0") int page,
									@RequestParam(defaultValue = "10") int size,
									@RequestParam(defaultValue = "cognome") String orderBy,
									@RequestParam(defaultValue = "asc") String sortCriteria) {
		return this.dipendentiService.findAll(page, size, orderBy, sortCriteria);
	}

	// GET http://localhost:3001/dipendenti/{dipendenteId}
	@GetMapping("/{dipendenteId}")
	public Dipendente findById(@PathVariable UUID dipendenteId) {
		return this.dipendentiService.findById(dipendenteId);
	}

	// PUT http://localhost:3001/dipendenti/{dipendenteId}
	@PutMapping("/{dipendenteId}")
	public Dipendente findByIdAndUpdate(@PathVariable UUID dipendenteId, @RequestBody @Validated DipendenteDTO payload, BindingResult validationResult) {
		if (validationResult.hasErrors()) {
			List<String> errorsList = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
			throw new ValidationException(errorsList);
		}
		return this.dipendentiService.findByIdAndUpdate(dipendenteId, payload);
	}

	// DELETE http://localhost:3001/dipendenti/{dipendenteId}
	@DeleteMapping("/{dipendenteId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void findByIdAndDelete(@PathVariable UUID dipendenteId) {
		this.dipendentiService.findByIdAndDelete(dipendenteId);
	}

	// PATCH http://localhost:3001/dipendenti/{dipendenteId}/immagineProfilo
	@PatchMapping("/{dipendenteId}/immagineProfilo")
	public String uploadImmagineProfilo(@RequestParam("immagineProfilo") MultipartFile file, @PathVariable UUID dipendenteId) {
		return this.dipendentiService.uploadImmagineProfilo(dipendenteId, file);
	}
}
