package pl.uplukaszp.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.uplukaszp.domain.Sensor;

public interface SensorRepository extends JpaRepository<Sensor, Long>{

}
