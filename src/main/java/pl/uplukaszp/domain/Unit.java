package pl.uplukaszp.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

/**
 * Describes the physical quantity measured by the sensor
 */
@Data
@Entity
public class Unit {
	@Id
	@GeneratedValue
	private Long id;
	@Column(nullable = false, unique = true)
	private String symbol;
	private String description;
}
