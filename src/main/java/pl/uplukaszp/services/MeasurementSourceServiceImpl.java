package pl.uplukaszp.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import pl.uplukaszp.domain.Location;
import pl.uplukaszp.domain.MeasurementSource;
import pl.uplukaszp.dto.MeasurementSourceDTO;
import pl.uplukaszp.dto.projections.MeasurementSourceWithoutOwner;
import pl.uplukaszp.repo.MeasurementSourceRepository;
import pl.uplukaszp.util.LocationComparator;
import pl.uplukaszp.util.exceptions.InconclusiveDataException;
import pl.uplukaszp.util.exceptions.LocationNotFoundException;

@Service
public class MeasurementSourceServiceImpl {
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
	UserDetailsServiceImpl userService;

	@Autowired
	LocationServiceImpl locationService;

	public ResponseEntity<Map<String, String>> save(MeasurementSourceDTO sourceDTO, String name) {
		MeasurementSource source = new MeasurementSource();
		Location loc = new Location();

		loc.setLatitude(Double.valueOf(sourceDTO.getLatitude()));
		loc.setLongitude(Double.valueOf(sourceDTO.getLongitude()));
		loc = locationService.save(loc);

		source.setName(sourceDTO.getName());
		source.setLocation(loc);
		source.setPublicly(sourceDTO.isPublicly());
		source.setOwner(userService.findByEmail(name));
		if (repo.save(source) != null)
			return ResponseEntity.ok().build();
		else
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

	}

	public ArrayList<MeasurementSourceWithoutOwner> findByOwnerEmail(String email) {
		return repo.findByOwnerEmail(email);
	}

	public ResponseEntity<List<MeasurementSourceWithoutOwner>> findByLocation(String name, String address,
			Integer radius) {
		try {
			Location l = getLocationFromAddres(address);
			ArrayList<MeasurementSourceWithoutOwner> sources = repo.findByOwnerEmailOrPubliclyIsTrue(name);
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

	public Optional<MeasurementSource> findById(Long id) {
		return repo.findById(id);
	}

	public MeasurementSource save(MeasurementSource measurementSource) {
		return repo.save(measurementSource);
	}

}
