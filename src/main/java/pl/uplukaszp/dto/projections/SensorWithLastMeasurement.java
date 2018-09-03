package pl.uplukaszp.dto.projections;

import org.springframework.beans.factory.annotation.Value;

import pl.uplukaszp.domain.Measurement;

public interface SensorWithLastMeasurement extends SensorWithoutSourceAndMeasurements{
	@Value("#{target.getMeasurements().size()>0? target.getMeasurements().get(target.getMeasurements().size()-1):null}")
	public Measurement getMeasurements();
}
