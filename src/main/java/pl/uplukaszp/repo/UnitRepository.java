package pl.uplukaszp.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.uplukaszp.domain.Unit;

public interface UnitRepository extends JpaRepository<Unit, Long> {
	
}
