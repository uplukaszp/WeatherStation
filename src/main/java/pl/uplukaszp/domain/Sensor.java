package pl.uplukaszp.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Data
@Entity
public class Sensor {
	@Id
	@GeneratedValue
	private Long id;
	
	@OneToMany(mappedBy="sensor")
    @JsonManagedReference
	private List<Measurement> measurements;
	
	@ManyToOne
	private Unit unit;
}
