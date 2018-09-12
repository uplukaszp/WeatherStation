package pl.uplukaszp.domain;

import java.beans.Transient;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Represents location of measurement source. Used to determine the position
 * relative to position provided by user
 */

@Data
@Entity
public class Location {
	@Id
	@GeneratedValue
	private Long id;
	@Column(nullable = false)
	private double longitude;
	@Column(nullable = false)
	double latitude;

	private double distance;

	@Transient
	@JsonProperty
	public double getDistane() {
		return distance;
	}
}
