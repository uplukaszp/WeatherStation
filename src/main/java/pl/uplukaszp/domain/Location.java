package pl.uplukaszp.domain;

import java.beans.Transient;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.data.geo.Point;

import lombok.Data;

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
	public double getDistane() {
		return distance;
	}

	public double distance(Location l) {
		final double R = 6371; // Radius of the earth in km

		double dLat = Math.toRadians(l.latitude - latitude); // deg2rad below
		double dLon = Math.toRadians(l.longitude - longitude);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(latitude))
				* Math.cos(Math.toRadians(l.latitude)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		distance = R * c; // Distance in km
		return distance;
	}
}
