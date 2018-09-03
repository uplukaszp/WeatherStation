package pl.uplukaszp.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.uplukaszp.domain.Sensor;
import pl.uplukaszp.dto.projections.SensorWithSourceAndMeasurements;

public interface SensorRepository extends JpaRepository<Sensor, Long>{
	public List<SensorWithSourceAndMeasurements> findAllByIdIn(List<Long> id);
}
