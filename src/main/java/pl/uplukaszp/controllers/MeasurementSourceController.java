package pl.uplukaszp.controllers;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import pl.uplukaszp.domain.Location;
import pl.uplukaszp.domain.MeasurementSource;
import pl.uplukaszp.domain.projections.MeasurementSourceWithoutOwner;
import pl.uplukaszp.dto.MeasurementSourceDTO;
import pl.uplukaszp.repo.LocationRepository;
import pl.uplukaszp.repo.MeasurementSourceRepository;
import pl.uplukaszp.repo.UserRepository;
import pl.uplukaszp.util.ValidationErrorParser;

@RestController
public class MeasurementSourceController {

	@Autowired
	MeasurementSourceRepository repo;

	@Autowired
	UserRepository userRepo;
	
	@Autowired 
	LocationRepository locationRepo;
	
	@PostMapping("/measurementSource")
	public ResponseEntity<Map<String, String>> createSource(Authentication auth,
			@RequestBody @Valid MeasurementSourceDTO sourceDTO, Errors errors) {
		if (errors.hasErrors()) {
			return ResponseEntity.badRequest().body(ValidationErrorParser.parseErrors(errors));
		}
		MeasurementSource source = new MeasurementSource();
		Location loc = new Location();
		loc.setLatitude(Double.valueOf(sourceDTO.getLatitude()));
		loc.setLongitude(Double.valueOf(sourceDTO.getLongitude()));
		loc=locationRepo.save(loc);
		source.setName(sourceDTO.getName());
		source.setLocation(loc);
		source.setPublicly(sourceDTO.isPublicly());
		source.setOwner(userRepo.findByEmail(auth.getName()));
		repo.save(source);
		return ResponseEntity.ok(null);
	}

	@GetMapping("/measurementSource")
	public ResponseEntity<List<MeasurementSourceWithoutOwner>> getSources(Authentication auth) {
		List<MeasurementSourceWithoutOwner> sources=repo.findByOwnerEmail(auth.getName());
		return new ResponseEntity<List<MeasurementSourceWithoutOwner>>(sources,HttpStatus.OK);
	}

	@DeleteMapping("/measurementSource")
	public ResponseEntity<Map<String, String>> removeSource() {
		return null;
	}
}
