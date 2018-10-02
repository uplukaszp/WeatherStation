package pl.uplukaszp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.uplukaszp.domain.Location;
import pl.uplukaszp.repo.LocationRepository;

@Service
public class LocationServiceImpl {
	@Autowired
	LocationRepository repo;

	public Location save(Location loc) {
		return repo.save(loc);
	}

}
