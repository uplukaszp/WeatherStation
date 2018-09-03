package pl.uplukaszp.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import pl.uplukaszp.domain.Measurement;
import pl.uplukaszp.domain.Sensor;
import pl.uplukaszp.dto.MeasurementDTO;
import pl.uplukaszp.repo.MeasurementRepository;
import pl.uplukaszp.repo.SensorRepository;

@RestController
public class MeasurementController {

	@Autowired
	MeasurementRepository repo;
	@Autowired
	SensorRepository sensorRepo;

	@PostMapping("/measurement")
	public ResponseEntity<Map<String, String>> postMeasurement(@RequestBody @Valid MeasurementDTO measurements,
			Errors errors) {
		for (Map<String, String> map : measurements.getMeasurements()) {
			Measurement m = new Measurement();
			Sensor s = new Sensor();
			s.setId(Long.valueOf(map.get("id")));
			m.setSensor(s);
			m.setValue(Float.valueOf(map.get("value")));
			String date = map.get("date");
			if (date != null) {
				DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
				LocalDateTime localDate = LocalDateTime.parse(date, formatter);
				m.setDate(localDate);
			}else
			{
				m.setDate(LocalDateTime.now());
			}
			repo.save(m);

		}
		return ResponseEntity.ok().build();
	}
}
