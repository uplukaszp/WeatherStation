package pl.uplukaszp.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.uplukaszp.domain.MeasurementSource;
import pl.uplukaszp.dto.projections.MeasurementSourceWithoutOwner;

@Repository
public interface MeasurementSourceRepository extends JpaRepository<MeasurementSource, Long>{

	ArrayList<MeasurementSourceWithoutOwner> findByOwnerEmail(String email);
	
	ArrayList<MeasurementSourceWithoutOwner> findByOwnerEmailOrPubliclyIsTrue(String email);

	List<MeasurementSource> findAllByOwnerId(Long id);
	
}
