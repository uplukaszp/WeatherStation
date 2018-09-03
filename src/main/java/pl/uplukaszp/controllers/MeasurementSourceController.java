package pl.uplukaszp.controllers;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.fabric.xmlrpc.base.Array;

import io.jsonwebtoken.lang.Arrays;
import pl.uplukaszp.domain.Location;
import pl.uplukaszp.domain.MeasurementSource;
import pl.uplukaszp.dto.MeasurementSourceDTO;
import pl.uplukaszp.dto.PositionDTO;
import pl.uplukaszp.dto.projections.MeasurementSourceWithoutOwner;
import pl.uplukaszp.dto.projections.SensorWithLastMeasurement;
import pl.uplukaszp.repo.LocationRepository;
import pl.uplukaszp.repo.MeasurementSourceRepository;
import pl.uplukaszp.repo.UserRepository;
import pl.uplukaszp.util.LocationComparator;
import pl.uplukaszp.util.ValidationErrorParser;
import pl.uplukaszp.util.exceptions.InconclusiveDataException;

@RestController
public class MeasurementSourceController {
	@Value("${tokens.locationIQ}")
	private String token;
	@Autowired
	MeasurementSourceRepository repo;

	@Autowired
	UserRepository userRepo;

	@Autowired
	LocationRepository locationRepo;

	@PostMapping("/measurementSource")
	public ResponseEntity<Map<String, String>> createSource(Authentication auth,
			@RequestBody @Valid MeasurementSourceDTO sourceDTO, Errors errors) {
		if (errors.hasErrors()) {
			return ResponseEntity.badRequest().body(ValidationErrorParser.parseErrors(errors));
		}
		MeasurementSource source = new MeasurementSource();
		Location loc = new Location();
		loc.setLatitude(Double.valueOf(sourceDTO.getLatitude()));
		loc.setLongitude(Double.valueOf(sourceDTO.getLongitude()));
		loc = locationRepo.save(loc);
		source.setName(sourceDTO.getName());
		source.setLocation(loc);
		source.setPublicly(sourceDTO.isPublicly());
		source.setOwner(userRepo.findByEmail(auth.getName()));
		repo.save(source);
		return ResponseEntity.ok(null);
	}

	@DeleteMapping("/measurementSource")
	public ResponseEntity<Map<String, String>> removeSource() {
		return null;
	}

	@GetMapping("/measurementSource")
	public ResponseEntity<List<MeasurementSourceWithoutOwner>> getSources(Authentication auth,
			@RequestParam(value = "address", required = false) String address) {
		if (address == null) {
			List<MeasurementSourceWithoutOwner> sources = repo.findByOwnerEmail(auth.getName());
			return new ResponseEntity<List<MeasurementSourceWithoutOwner>>(sources, HttpStatus.OK);

		}
		String uri = "https://eu1.locationiq.com/v1/search.php?key=" + token + "&q=" + address + "&format=json";
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
		try {
			Location l = getLocationFromResponseBody(response.getBody());
			List<MeasurementSourceWithoutOwner> sources = repo.findByOwnerEmailOrPubliclyIsTrue(auth.getName());
			sources.sort(new LocationComparator(l));
			ResponseEntity<List<MeasurementSourceWithoutOwner>> res = new ResponseEntity<List<MeasurementSourceWithoutOwner>>(
					sources, HttpStatus.OK);
			return res;
		} catch (InconclusiveDataException e) {
			return ResponseEntity.badRequest().build();
		} catch (IllegalArgumentException | IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	private Location getLocationFromResponseBody(String body)
			throws InconclusiveDataException, IllegalArgumentException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		List<Map<String, List<String>>> result;

		result = mapper.convertValue(mapper.readTree(body),
				new TypeReference<ArrayList<HashMap<String, ArrayList<String>>>>() {
				});
		if (result.size() > 1) {
			throw new InconclusiveDataException();
		}
		Location targetLocation = new Location();
		String lon = (result.get(0).get("lon").get(0));
		String lat = (result.get(0).get("lat").get(0));
		targetLocation.setLongitude(Double.valueOf(lon));
		targetLocation.setLatitude(Double.valueOf(lat));
		return targetLocation;

	}

}
