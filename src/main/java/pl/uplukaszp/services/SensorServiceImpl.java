package pl.uplukaszp.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import pl.uplukaszp.domain.Measurement;
import pl.uplukaszp.domain.MeasurementSource;
import pl.uplukaszp.domain.Sensor;
import pl.uplukaszp.domain.Unit;
import pl.uplukaszp.dto.SensorDTO;
import pl.uplukaszp.dto.projections.SensorWithSourceAndMeasurements;
import pl.uplukaszp.repo.SensorRepository;
import pl.uplukaszp.util.ValidationErrorParser;

@Service
public class SensorServiceImpl {

	@Autowired
	private SensorRepository repo;

	@Autowired
	private MeasurementSourceServiceImpl sourceService;

	@Autowired
	private UnitServiceImpl unitService;

	public Optional<Sensor> findById(Long id) {
		return repo.findById(id);
	}

	public ResponseEntity<Map<String, String>> save(SensorDTO s) {
		Optional<MeasurementSource> source = sourceService.findById(s.getSource());
		if (!source.isPresent())
			return ResponseEntity.badRequest()
					.body(ValidationErrorParser.parseError("source", "Source does not exist"));
		Optional<Unit> unit = unitService.findById(s.getUnit());
		if (!unit.isPresent())
			return ResponseEntity.badRequest().body(ValidationErrorParser.parseError("unit", "Unit does not exist"));
		Sensor sensor = new Sensor();

		sensor.setUnit(unit.get());
		sensor.setSource(source.get());
		sensor = repo.save(sensor);

		source.get().addSensor(sensor);
		sourceService.save(source.get());

		return ResponseEntity.ok().build();
	}

	public ResponseEntity<List<SensorWithSourceAndMeasurements>> findAllByIdIn(String list) {
		List<Long> idList;
		try {
			idList = parseIdList(list);
			System.out.println(repo.findAllByIdIn(idList).get(0).getMeasurements().size());
			return ResponseEntity.ok().body(repo.findAllByIdIn(idList));
		} catch (IOException e) {
			return ResponseEntity.badRequest().build();
		}

	}

	public ResponseEntity<List<SensorWithSourceAndMeasurements>> findAllByIdInWhereMeasurmentDateIsBetween(String list,
			String startDate, String endDate) {
		List<Long> idList;
		try {
			idList = parseIdList(list);
			LocalDateTime start = LocalDateTime.parse(startDate, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
			LocalDateTime end = LocalDateTime.parse(endDate, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
			List<SensorWithSourceAndMeasurements> sensors = repo.findAllByIdIn(idList);
			for (SensorWithSourceAndMeasurements sensor : sensors) {
				sensor.setMeasurements(filterByDate(sensor.getMeasurements(), start, end));
			}
			return new ResponseEntity<List<SensorWithSourceAndMeasurements>>(sensors, HttpStatus.OK);
		} catch (IOException e) {
			return ResponseEntity.badRequest().build();
		}

	}

	private List<Long> parseIdList(String ids) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		List<Long> list = null;

		list = mapper.readValue(ids, new TypeReference<ArrayList<Long>>() {
		});
		return list;
	}

	private List<Measurement> filterByDate(List<Measurement> list, LocalDateTime start, LocalDateTime end) {
		List<Measurement> filteredList = new ArrayList<>();
		for (Measurement measurement : list) {
			if (measurement.getDate().isAfter(start) && measurement.getDate().isBefore(end))
				filteredList.add(measurement);
		}
		return filteredList;
	}
}
