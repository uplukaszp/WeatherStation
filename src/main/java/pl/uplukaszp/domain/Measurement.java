package pl.uplukaszp.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

/**
 * Represents measured value, at a specified time and by a specific sensor.
 */
@Data
@Entity
public class Measurement {
	@Id
	@GeneratedValue
	private Long id;
	@Column(nullable = false)
	private float value;
	@ManyToOne
	@JsonBackReference
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Sensor sensor;

	@Column(nullable = false)
	private LocalDateTime date;
}
