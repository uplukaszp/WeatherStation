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
