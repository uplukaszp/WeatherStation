package pl.uplukaszp.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;

@Data
@Entity
public class Sensor {
	@Id
	@GeneratedValue
	private Long id;
	
	@OneToMany(mappedBy="sensor")
    @JsonManagedReference
    @OrderBy("date ASC")
	private List<Measurement> measurements;
	
	@ManyToOne
	private Unit unit;
	
	@ManyToOne(optional=false)
	private MeasurementSource source;
}
