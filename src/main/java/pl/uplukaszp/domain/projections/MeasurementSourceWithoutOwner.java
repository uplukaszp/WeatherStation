package pl.uplukaszp.domain.projections;

import java.util.List;

import pl.uplukaszp.domain.Location;
import pl.uplukaszp.domain.Sensor;

public interface MeasurementSourceWithoutOwner {
	Long getId();
	String getName();
	boolean isPublicly();
	Location getLocation();
	List<Sensor>getSensors();
}
