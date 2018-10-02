package pl.uplukaszp.controllers;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pl.uplukaszp.dto.SensorDTO;
import pl.uplukaszp.dto.projections.SensorWithSourceAndMeasurements;
import pl.uplukaszp.repo.MeasurementSourceRepository;
import pl.uplukaszp.repo.UnitRepository;
import pl.uplukaszp.services.SensorServiceImpl;
import pl.uplukaszp.util.ValidationErrorParser;

@RestController
public class SensorController {

	@Autowired
	SensorServiceImpl sensorService;

	@Autowired
	MeasurementSourceRepository sourceRepo;

	@Autowired
	UnitRepository unitRepo;

	/**
	 * Saves new Sensor in the database.
	 * 
	 * @return @see ValidationErrorParser#parseErrors(Errors)
	 */
	@PostMapping("/sensor")
	public ResponseEntity<Map<String, String>> addSensor(@RequestBody @Valid SensorDTO s, Errors errors) {
		if (errors.hasErrors()) {
			return ResponseEntity.badRequest().body(ValidationErrorParser.parseErrors(errors));
		}
		return sensorService.save(s);

	}

	/**
	 * @param ids
	 *            JSON array with ids of sensors
	 * @return list of sensors from the list with ids or BAD REQUEST when ids is not
	 *         array
	 */
	@GetMapping("/sensor")
	public ResponseEntity<List<SensorWithSourceAndMeasurements>> getMeasurementsData(@RequestParam("id") String ids,
			@RequestParam(name = "startDate", required = false) String startDate,
			@RequestParam(name = "endDate", required = false) String endDate) {

		if (startDate != null && endDate != null) {
			return sensorService.findAllByIdInWhereMeasurmentDateIsBetween(ids, startDate, endDate);

		}
		return sensorService.findAllByIdIn(ids);
	}

}
