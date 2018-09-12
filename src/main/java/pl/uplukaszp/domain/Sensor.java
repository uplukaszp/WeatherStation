package pl.uplukaszp.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

/**
 * Represents device, module, or subsystem whose purpose is to measure certain
 * physical quantities described by units, which send the information to the
 * measurement source @see pl.uplukaszp.domain.MeasurementSource 
 */
@Data
@Entity
public class Sensor {
	@Id
	@GeneratedValue
	private Long id;

	@OneToMany(mappedBy = "sensor")
	@JsonManagedReference
	@OrderBy("date ASC")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<Measurement> measurements;

	@ManyToOne
	private Unit unit;

	@ManyToOne(optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private MeasurementSource source;
}
