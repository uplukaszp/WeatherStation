package pl.uplukaszp.dto.projections;

import java.util.List;
import pl.uplukaszp.domain.Location;

public interface MeasurementSourceWithoutOwner {
	Long getId();
	String getName();
	boolean isPublicly();
	Location getLocation();
	List<SensorWithLastMeasurement>getSensors();

}
