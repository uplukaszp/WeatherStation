package pl.uplukaszp.controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
		Optional<MeasurementSource> source = sourceRepo.findById(s.getSource());
		if (!source.isPresent())
			return ResponseEntity.badRequest()
					.body(ValidationErrorParser.parseError("source", "Source does not exist"));
		Optional<Unit> unit = unitRepo.findById(s.getUnit());
		if (!unit.isPresent())
			return ResponseEntity.badRequest().body(ValidationErrorParser.parseError("unit", "Unit does not exist"));
		Sensor sensor = new Sensor();

		sensor.setUnit(unit.get());
		sensor.setSource(source.get());
		sensor = sensorRepo.save(sensor);

		source.get().addSensor(sensor);
		sourceRepo.save(source.get());

		return ResponseEntity.ok().build();

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

		List<Long> idList;
		try {

			idList = parseIdList(ids);

		} catch (IOException e) {
			return ResponseEntity.badRequest().build();
		}
		if (startDate != null && endDate != null) {
			LocalDateTime start = LocalDateTime.parse(startDate, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
			LocalDateTime end = LocalDateTime.parse(endDate, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
			List<SensorWithSourceAndMeasurements> sensors = sensorRepo
					.findAllByIdIn(idList);
			for (SensorWithSourceAndMeasurements sensor : sensors) {
				sensor.setMeasurements(filterByDate(sensor.getMeasurements(), start, end));
			}
			return new ResponseEntity<List<SensorWithSourceAndMeasurements>>(sensors, HttpStatus.OK);

		}
		List<SensorWithSourceAndMeasurements> sensors = sensorRepo.findAllByIdIn(idList);
		return new ResponseEntity<List<SensorWithSourceAndMeasurements>>(sensors, HttpStatus.OK);

	}

	private List<Long> parseIdList(String ids) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		List<Long> list = null;

		list = mapper.readValue(ids, new TypeReference<ArrayList<Long>>() {
		});
		return list;
	}
	
	private List<Measurement> filterByDate(List<Measurement> list,LocalDateTime start,LocalDateTime end)
	{
		List<Measurement>filteredList=new ArrayList<>();
		for (Measurement measurement : list) {
			if(measurement.getDate().isAfter(start)&&measurement.getDate().isBefore(end))filteredList.add(measurement);
		}
		return filteredList;
	}
}
