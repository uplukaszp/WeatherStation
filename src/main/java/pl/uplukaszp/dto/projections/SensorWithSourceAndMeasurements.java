package pl.uplukaszp.dto.projections;

import java.util.List;

import pl.uplukaszp.domain.Measurement;

public interface SensorWithSourceAndMeasurements extends SensorWithoutSourceAndMeasurements{
	public MeasurementSourceOnlyWithName getSource();
	public List<Measurement> getMeasurements();

}
