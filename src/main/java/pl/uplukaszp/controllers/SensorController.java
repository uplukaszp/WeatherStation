package pl.uplukaszp.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import pl.uplukaszp.domain.MeasurementSource;
import pl.uplukaszp.domain.Sensor;
import pl.uplukaszp.domain.Unit;
import pl.uplukaszp.dto.SensorDTO;
import pl.uplukaszp.repo.MeasurementSourceRepository;
import pl.uplukaszp.repo.SensorRepository;
import pl.uplukaszp.repo.UnitRepository;
import pl.uplukaszp.util.ValidationErrorParser;

@RestController
public class SensorController {

	@Autowired
	SensorRepository sensorRepo;

	@Autowired
	MeasurementSourceRepository sourceRepo;

	@Autowired
	UnitRepository unitRepo;

	//TODO return error if source or unit not exist
	@PostMapping("/sensor")
	public ResponseEntity<Map<String, String>> addSensor(@RequestBody @Valid SensorDTO s, Errors errors) {
		if (errors.hasErrors()) {
			return ResponseEntity.badRequest().body(ValidationErrorParser.parseErrors(errors));
		}

		MeasurementSource source = sourceRepo.getOne(Long.valueOf(s.getSource()));
		Unit unit = unitRepo.getOne(Long.valueOf(s.getUnit()));
		Sensor sensor = new Sensor();

		sensor.setUnit(unit);
		sensor = sensorRepo.save(sensor);

		source.addSensor(sensor);
		sourceRepo.save(source);

		return ResponseEntity.ok().body(null);

	}
}
