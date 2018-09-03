package pl.uplukaszp.dto.projections;

import java.util.List;

import pl.uplukaszp.domain.Measurement;
import pl.uplukaszp.domain.Unit;

public interface SensorWithoutSourceAndMeasurements {
	public Long getId();
	public Unit getUnit();
}
