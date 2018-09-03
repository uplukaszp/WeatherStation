package pl.uplukaszp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.uplukaszp.domain.Measurement;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {

}
