package pl.uplukaszp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.uplukaszp.domain.Unit;
import pl.uplukaszp.repo.UnitRepository;

@Service
public class UnitServiceImpl {

	@Autowired
	UnitRepository repo;
	
	public Optional<Unit> findById(Long id) {
		return repo.findById(id);
	}

	public List<Unit> findAll() {
		return repo.findAll();
	}

}
