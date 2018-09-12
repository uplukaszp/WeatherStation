package pl.uplukaszp.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import pl.uplukaszp.domain.Measurement;
import pl.uplukaszp.domain.Sensor;
import pl.uplukaszp.dto.MeasurementDTO;
import pl.uplukaszp.dto.MeasurementListDTO;
import pl.uplukaszp.repo.MeasurementRepository;
import pl.uplukaszp.repo.SensorRepository;
import pl.uplukaszp.util.ValidationErrorParser;

@RestController
public class MeasurementController {

	@Autowired
	MeasurementRepository repo;
	@Autowired
	SensorRepository sensorRepo;

	/**
	 * Receives data from sensors and saves them in the database only when all data
	 * is correct
	 * 
	 * @return @see ValidationErrorParser#parseErrors(Errors)
	 */
	@PostMapping("/measurement")
	public ResponseEntity<Map<String, String>> postMeasurement(@RequestBody @Valid MeasurementListDTO measurementsDTO,
			Errors errors) {
		List<Measurement> measurements = new ArrayList<>();

		if (errors.getAllErrors().size() > 0) {
			return ResponseEntity.badRequest().body(ValidationErrorParser.parseErrors(errors));

		}

		for (int i = 0; i < measurementsDTO.getMeasurements().size(); i++) {
			MeasurementDTO m = measurementsDTO.getMeasurements().get(i);
			Measurement measurement = new Measurement();
			Optional<Sensor> s = sensorRepo.findById(m.getId());

			if (!s.isPresent())
				return ResponseEntity.badRequest().body(ValidationErrorParser.parseError("measurements[" + i + "].id",
						"Sensor with id " + m.getId() + " does not exist"));

			measurement.setSensor(s.get());
			measurement.setValue(m.getValue());
			measurement.setDate(parseDate(m.getDate()));

			measurements.add(measurement);
		}
		repo.saveAll(measurements);
		return ResponseEntity.ok().build();
	}

	private LocalDateTime parseDate(String date) {
		if (date != null) {
			DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
			LocalDateTime localDate = LocalDateTime.parse(date, formatter);
			return localDate;
		} else {
			return LocalDateTime.now();
		}
	}
}
