package pl.uplukaszp.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;

@Data
@Entity
public class MeasurementSource {
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@OneToOne
	@JoinColumn(name = "user_data_id")
	private UserData owner ;
	
	@Column(nullable = false)
	private boolean publicly;
	
	@OneToOne
	private Location location;
	
	@OneToMany(mappedBy="source")
	@JsonManagedReference
	private List<Sensor> sensors;

	public void addSensor(Sensor s) {
		sensors.add(s);
	}
}
