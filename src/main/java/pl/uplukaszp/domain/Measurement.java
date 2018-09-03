package pl.uplukaszp.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Data
@Entity
public class Measurement {
	@Id
	@GeneratedValue
	private Long id;
	@Column(nullable=false)
	private float value;
	@ManyToOne
    @JsonBackReference
	private Sensor sensor;
	
	@Column(nullable=false)
	private LocalDateTime date;
}
