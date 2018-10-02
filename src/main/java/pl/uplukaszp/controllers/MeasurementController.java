package pl.uplukaszp.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import pl.uplukaszp.dto.MeasurementListDTO;
import pl.uplukaszp.services.MeasurementServiceImpl;
import pl.uplukaszp.services.SensorServiceImpl;
import pl.uplukaszp.util.ValidationErrorParser;

@RestController
public class MeasurementController {

	@Autowired
	MeasurementServiceImpl measurementService;
	@Autowired
	SensorServiceImpl sensorService;

	/**
	 * Receives data from sensors and saves them in the database only when all data
	 * is correct
	 * 
	 * @return @see ValidationErrorParser#parseErrors(Errors)
	 */
	@PostMapping("/measurement")
	public ResponseEntity<Map<String, String>> postMeasurement(@RequestBody @Valid MeasurementListDTO measurementsDTO,
			Errors errors) {

		if (errors.getAllErrors().size() > 0) {
			return ResponseEntity.badRequest().body(ValidationErrorParser.parseErrors(errors));
		}
		return measurementService.saveAll(measurementsDTO);
	}
}
