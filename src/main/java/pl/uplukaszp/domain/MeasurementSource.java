package pl.uplukaszp.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

/**
 * Represents device with sensors, assigned to a specific user,capable of
 * sending data with the Http requests.
 */
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
	@OnDelete(action = OnDeleteAction.CASCADE)
	private UserData owner;

	@Column(nullable = false)
	private boolean publicly;

	@OneToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Location location;

	@OneToMany(mappedBy = "source")
	@JsonManagedReference
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<Sensor> sensors;

	public void addSensor(Sensor s) {
		sensors.add(s);
	}
}
