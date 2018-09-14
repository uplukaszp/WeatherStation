package pl.uplukaszp.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import pl.uplukaszp.domain.Location;
import pl.uplukaszp.domain.MeasurementSource;
import pl.uplukaszp.dto.MeasurementSourceDTO;
import pl.uplukaszp.dto.projections.MeasurementSourceWithoutOwner;
import pl.uplukaszp.repo.LocationRepository;
import pl.uplukaszp.repo.MeasurementSourceRepository;
import pl.uplukaszp.repo.UserRepository;
import pl.uplukaszp.util.LocationComparator;
import pl.uplukaszp.util.ValidationErrorParser;
import pl.uplukaszp.util.exceptions.InconclusiveDataException;
import pl.uplukaszp.util.exceptions.LocationNotFoundException;

@RestController
public class MeasurementSourceController {

	/**
	 * Address of forward geocoding API
	 */
	private static final String LINK = "https://eu1.locationiq.com/v1/search.php?key=";

	/**
	 * Used for authentication in forward geocoding service locationIq.com;
	 */
	@Value("${tokens.locationIQ}")
	private String token;

	@Autowired
	MeasurementSourceRepository repo;

	@Autowired
	UserRepository userRepo;

	@Autowired
	LocationRepository locationRepo;

	/**
	 * Saves new Measurement Source in the database.
	 * 
	 * @return @see ValidationErrorParser#parseErrors(Errors)
	 */
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
		return ResponseEntity.ok().build();
	}

	/**
	 * 
	 * 
	 * @return When address parameter is specified: list of Measurement Sources, in
	 *         given radius ordered by distance between adders and source, or limit
	 *         the list to 10 sources when radius is not defined
	 *
	 * @return When address parameter is not specified: list of all Measurement
	 *         Sources belonging to the user.
	 * @return When address is not accurate or address does not exist, returns bad
	 *         Request or not found HTTP error
	 */
	@GetMapping("/measurementSource")
	public ResponseEntity<List<MeasurementSourceWithoutOwner>> getSources(Authentication auth,
			@RequestParam(value = "address", required = false) String address,
			@RequestParam(value = "range", required = false) Integer radius) {

		if (address == null) {
			ArrayList<MeasurementSourceWithoutOwner> sources = repo.findByOwnerEmail(auth.getName());
			return new ResponseEntity<List<MeasurementSourceWithoutOwner>>(sources, HttpStatus.OK);
		}
		if (radius != null) {
			if (radius < 0)
				return ResponseEntity.badRequest().build();
		}

		try {
			Location l = getLocationFromAddres(address);
			ArrayList<MeasurementSourceWithoutOwner> sources = repo.findByOwnerEmailOrPubliclyIsTrue(auth.getName());
			return ResponseEntity.ok().body(prepareList(sources, l, radius));

		} catch (LocationNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (InconclusiveDataException e) {
			return ResponseEntity.badRequest().build();
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	private Location getLocationFromAddres(String address)
			throws InconclusiveDataException, LocationNotFoundException, IOException {
		String uri = LINK + token + "&q=" + address + "&format=json";
		RestTemplate restTemplate = new RestTemplate();
		try {
			ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
			return getLocationFromAPIResponseBody(response.getBody());
		} catch (RestClientException e) {
			throw new LocationNotFoundException();
		}
	}

	private Location getLocationFromAPIResponseBody(String body) throws InconclusiveDataException, IOException {
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

	private List<MeasurementSourceWithoutOwner> prepareList(ArrayList<MeasurementSourceWithoutOwner> list, Location l,
			Integer radius) {
		list.sort(new LocationComparator(l));
		if (radius == null)
			return (list.size() > 10) ? list.subList(0, 10) : list;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getLocation().getDistance() > radius)
				return list.subList(0, i);
		}
		return list;
	}

}
