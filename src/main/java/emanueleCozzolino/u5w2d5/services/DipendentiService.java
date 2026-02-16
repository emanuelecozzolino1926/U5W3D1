package emanueleCozzolino.u5w2d5.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import emanueleCozzolino.u5w2d5.entities.Dipendente;
import emanueleCozzolino.u5w2d5.exceptions.BadRequestException;
import emanueleCozzolino.u5w2d5.exceptions.NotFoundException;
import emanueleCozzolino.u5w2d5.payloads.DipendenteDTO;
import emanueleCozzolino.u5w2d5.repositories.DipendentiRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class DipendentiService {
	private final DipendentiRepository dipendentiRepository;
	private final Cloudinary cloudinaryUploader;

	@Autowired
	public DipendentiService(DipendentiRepository dipendentiRepository, Cloudinary cloudinaryUploader) {
		this.dipendentiRepository = dipendentiRepository;
		this.cloudinaryUploader = cloudinaryUploader;
	}

	public Dipendente save(DipendenteDTO payload) {
		//Verifico che l'email passata non sia gia in uso
		this.dipendentiRepository.findByEmail(payload.email()).ifPresent(dipendente -> {
			throw new BadRequestException("L'email " + dipendente.getEmail() + " e' gia' in uso!");
		});
		//Creo il nuovo dipendente con avatar auto-generato
		Dipendente newDipendente = new Dipendente(payload.username(), payload.nome(), payload.cognome(), payload.email(), payload.password());
		newDipendente.setImmagineProfilo("https://ui-avatars.com/api?name=" + payload.nome() + "+" + payload.cognome());
		//Salvo
		Dipendente savedDipendente = this.dipendentiRepository.save(newDipendente);
		log.info("Il dipendente con id " + savedDipendente.getId() + " e' stato salvato correttamente!");
		//Ritorno il dipendente salvato
		return savedDipendente;
	}

	public Page<Dipendente> findAll(int page, int size, String orderBy, String sortCriteria) {
		if (size > 100 || size < 0) size = 10;
		if (page < 0) page = 0;
		Pageable pageable = PageRequest.of(page, size, sortCriteria.equals("desc") ? Sort.by(orderBy).descending() : Sort.by(orderBy));
		return this.dipendentiRepository.findAll(pageable);
	}

	public Dipendente findById(UUID dipendenteId) {
		return this.dipendentiRepository.findById(dipendenteId).orElseThrow(() -> new NotFoundException(dipendenteId));
	}

	public Dipendente findByIdAndUpdate(UUID dipendenteId, DipendenteDTO payload) {
		//Cerco il dipendente nel db
		Dipendente found = this.findById(dipendenteId);
		//Controllo se l'email e cambiata e se e gia in uso
		if (!found.getEmail().equals(payload.email())) {
			this.dipendentiRepository.findByEmail(payload.email()).ifPresent(dipendente -> {
				throw new BadRequestException("L'email " + dipendente.getEmail() + " e' gia' in uso!");
			});
		}
		//Modifico il dipendente trovato
		found.setUsername(payload.username());
		found.setNome(payload.nome());
		found.setCognome(payload.cognome());
		found.setEmail(payload.email());
		found.setImmagineProfilo("https://ui-avatars.com/api?name=" + payload.nome() + "+" + payload.cognome());
		//Salvo
		Dipendente modifiedDipendente = this.dipendentiRepository.save(found);
		log.info("Il dipendente con id " + modifiedDipendente.getId() + " e' stato modificato correttamente");
		//Ritorno il dipendente modificato
		return modifiedDipendente;
	}

	public Dipendente findByEmail(String email) {
		return this.dipendentiRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Il dipendente con email " + email + " non e' stato trovato!"));
	}

	public void findByIdAndDelete(UUID dipendenteId) {
		Dipendente found = this.findById(dipendenteId);
		this.dipendentiRepository.delete(found);
	}

	public String uploadImmagineProfilo(UUID dipendenteId, MultipartFile file) {
		Dipendente found = this.findById(dipendenteId);
		try {
			Map result = this.cloudinaryUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
			String imageUrl = (String) result.get("secure_url");
			found.setImmagineProfilo(imageUrl);
			this.dipendentiRepository.save(found);
			return imageUrl;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
