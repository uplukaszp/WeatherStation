package pl.uplukaszp.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import pl.uplukaszp.domain.MeasurementSource;
import pl.uplukaszp.domain.Sensor;
import pl.uplukaszp.domain.Unit;
import pl.uplukaszp.dto.SensorDTO;
import pl.uplukaszp.dto.projections.SensorWithSourceAndMeasurements;
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

	@PostMapping("/sensor")
	public ResponseEntity<Map<String, String>> addSensor(@RequestBody @Valid SensorDTO s, Errors errors) {
		if (errors.hasErrors()) {
			return ResponseEntity.badRequest().body(ValidationErrorParser.parseErrors(errors));
		}

		MeasurementSource source = sourceRepo.getOne(Long.valueOf(s.getSource()));
		if (source == null)
			return ResponseEntity.badRequest()
					.body(ValidationErrorParser.parseError("source", "Source does not exist"));
		Unit unit = unitRepo.getOne(Long.valueOf(s.getUnit()));
		if (unit == null)
			return ResponseEntity.badRequest().body(ValidationErrorParser.parseError("unit", "Unit does not exist"));
		Sensor sensor = new Sensor();

		sensor.setUnit(unit);
		sensor.setSource(source);
		sensor = sensorRepo.save(sensor);

		source.addSensor(sensor);

		sourceRepo.save(source);

		return ResponseEntity.ok().body(null);

	}

	@GetMapping("/sensor")
	public ResponseEntity<List<SensorWithSourceAndMeasurements>> getMeasurementsData(@RequestParam("id") String ids) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			List<Long> idList = mapper.readValue(ids, new TypeReference<ArrayList<Long>>() {
			});
			List<SensorWithSourceAndMeasurements> sensors = sensorRepo.findAllByIdIn(idList);
			return new ResponseEntity<List<SensorWithSourceAndMeasurements>>(sensors, HttpStatus.OK);
		} catch (IOException e) {
			return ResponseEntity.badRequest().build();
		}
	}
}
