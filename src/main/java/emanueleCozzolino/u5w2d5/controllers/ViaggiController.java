package emanueleCozzolino.u5w2d5.controllers;

import emanueleCozzolino.u5w2d5.entities.Viaggio;
import emanueleCozzolino.u5w2d5.enums.StatoViaggio;
import emanueleCozzolino.u5w2d5.exceptions.ValidationException;
import emanueleCozzolino.u5w2d5.payloads.ViaggioDTO;
import emanueleCozzolino.u5w2d5.services.ViaggiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/viaggi")
public class ViaggiController {
	private final ViaggiService viaggiService;

	@Autowired
	public ViaggiController(ViaggiService viaggiService) {
		this.viaggiService = viaggiService;
	}

	// POST http://localhost:3001/viaggi
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Viaggio createViaggio(@RequestBody @Validated ViaggioDTO payload, BindingResult validationResult) {
		if (validationResult.hasErrors()) {
			List<String> errorsList = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
			throw new ValidationException(errorsList);
		}
		return this.viaggiService.save(payload);
	}

	// GET http://localhost:3001/viaggi
	@GetMapping
	public Page<Viaggio> findAll(@RequestParam(defaultValue = "0") int page,
								 @RequestParam(defaultValue = "10") int size,
								 @RequestParam(defaultValue = "destinazione") String orderBy,
								 @RequestParam(defaultValue = "asc") String sortCriteria) {
		return this.viaggiService.findAll(page, size, orderBy, sortCriteria);
	}

	// GET http://localhost:3001/viaggi/{viaggioId}
	@GetMapping("/{viaggioId}")
	public Viaggio findById(@PathVariable UUID viaggioId) {
		return this.viaggiService.findById(viaggioId);
	}

	// PUT http://localhost:3001/viaggi/{viaggioId}
	@PutMapping("/{viaggioId}")
	public Viaggio findByIdAndUpdate(@PathVariable UUID viaggioId, @RequestBody @Validated ViaggioDTO payload, BindingResult validationResult) {
		if (validationResult.hasErrors()) {
			List<String> errorsList = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
			throw new ValidationException(errorsList);
		}
		return this.viaggiService.findByIdAndUpdate(viaggioId, payload);
	}

	// DELETE http://localhost:3001/viaggi/{viaggioId}
	@DeleteMapping("/{viaggioId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void findByIdAndDelete(@PathVariable UUID viaggioId) {
		this.viaggiService.findByIdAndDelete(viaggioId);
	}

	// PATCH http://localhost:3001/viaggi/{viaggioId}/stato
	@PatchMapping("/{viaggioId}/stato")
	public Viaggio findByIdAndUpdateStato(@PathVariable UUID viaggioId, @RequestParam StatoViaggio stato) {
		return this.viaggiService.findByIdAndUpdateStato(viaggioId, stato);
	}
}
