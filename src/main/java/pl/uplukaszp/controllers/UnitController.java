package pl.uplukaszp.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.uplukaszp.domain.Unit;
import pl.uplukaszp.repo.UnitRepository;

@RestController
public class UnitController {

	@Autowired
	UnitRepository repo;

	/** Returns list of all units stored in database */
	@GetMapping("/unit")
	public ResponseEntity<List<Unit>> getUnits() {
		return ResponseEntity.ok(repo.findAll());
	}
}
