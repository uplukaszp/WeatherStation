package pl.uplukaszp.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import pl.uplukaszp.domain.Measurement;
import pl.uplukaszp.domain.Sensor;
import pl.uplukaszp.dto.MeasurementDTO;
import pl.uplukaszp.dto.MeasurementListDTO;
import pl.uplukaszp.repo.MeasurementRepository;
import pl.uplukaszp.util.ValidationErrorParser;

@Service
public class MeasurementServiceImpl {

	@Autowired
	MeasurementRepository repo;

	@Autowired
	SensorServiceImpl sensorService;

	public ResponseEntity<Map<String, String>> saveAll(MeasurementListDTO measurementsDTO) {
		List<Measurement> measurements = new ArrayList<>();

		for (int i = 0; i < measurementsDTO.getMeasurements().size(); i++) {
			MeasurementDTO m = measurementsDTO.getMeasurements().get(i);
			Measurement measurement = new Measurement();
			Optional<Sensor> s = sensorService.findById(m.getId());

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
