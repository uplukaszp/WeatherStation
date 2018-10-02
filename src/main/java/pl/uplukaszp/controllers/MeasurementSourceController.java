package pl.uplukaszp.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pl.uplukaszp.dto.MeasurementSourceDTO;
import pl.uplukaszp.dto.projections.MeasurementSourceWithoutOwner;

import pl.uplukaszp.services.MeasurementSourceServiceImpl;

import pl.uplukaszp.util.ValidationErrorParser;

@RestController
public class MeasurementSourceController {

	@Autowired
	MeasurementSourceServiceImpl measurementSourceService;

	/**
	 * Saves new Measurement Source in the database.
	 * 
	 * @return @see ValidationErrorParser#parseErrors(Errors)
	 */
	@PostMapping("/measurementSource")
	public ResponseEntity<Map<String, String>> createSource(Authentication auth,
			@RequestBody @Valid MeasurementSourceDTO sourceDTO, Errors errors) {
		if (errors.hasErrors()) {
			return ResponseEntity.badRequest().body(ValidationErrorParser.parseErrors(errors));
		}
		return measurementSourceService.save(sourceDTO, auth.getName());
	}

	/**
	 * 
	 * 
	 * @return When address parameter is specified: list of Measurement Sources, in
	 *         given radius ordered by distance between adders and source, or limit
	 *         the list to 10 sources when radius is not defined
	 *
	 * @return When address parameter is not specified: list of all Measurement
	 *         Sources belonging to the user.
	 * @return When address is not accurate or address does not exist, returns bad
	 *         Request or not found HTTP error
	 */
	@GetMapping("/measurementSource")
	public ResponseEntity<List<MeasurementSourceWithoutOwner>> getSources(Authentication auth,
			@RequestParam(value = "address", required = false) String address,
			@RequestParam(value = "range", required = false) Integer radius) {

		if (address == null) {
			ArrayList<MeasurementSourceWithoutOwner> sources = measurementSourceService
					.findByOwnerEmail(auth.getName());
			return new ResponseEntity<List<MeasurementSourceWithoutOwner>>(sources, HttpStatus.OK);
		}
		if (radius != null) {
			if (radius < 0)
				return ResponseEntity.badRequest().build();
		}
		return measurementSourceService.findByLocation(auth.getName(), address, radius);

	}

}
