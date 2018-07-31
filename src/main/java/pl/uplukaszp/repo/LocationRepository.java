package pl.uplukaszp.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.uplukaszp.domain.Location;

public interface LocationRepository extends JpaRepository<Location, Long>{

}
