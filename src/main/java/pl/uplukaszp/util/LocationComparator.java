package pl.uplukaszp.util;

import java.util.Comparator;

import pl.uplukaszp.domain.Location;
import pl.uplukaszp.dto.projections.MeasurementSourceWithoutOwner;

/**
 * Compares two measurement sources based on their distance relative to the
 * target object. The closer source is earlier in the order in relation to the
 * further source.
 */
public class LocationComparator implements Comparator<MeasurementSourceWithoutOwner> {

	Location target;

	public LocationComparator(Location target) {
		this.target = target;
	}

	@Override
	public int compare(MeasurementSourceWithoutOwner o1, MeasurementSourceWithoutOwner o2) {
		Location l1 = o1.getLocation();
		Location l2 = o2.getLocation();
		double distance1 = calculateDistance(l1, target);
		double distance2 = calculateDistance(l2, target);
		l1.setDistance(distance1);
		l2.setDistance(distance2);
		return Double.compare(distance1, distance2);
	}

	private double calculateDistance(Location l1, Location l2) {
		final double R = 6371; // Radius of the earth in km

		double dLat = Math.toRadians(l1.getLatitude() - l2.getLatitude()); // deg2rad below
		double dLon = Math.toRadians(l1.getLongitude() - l2.getLongitude());
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(l2.getLatitude()))
				* Math.cos(Math.toRadians(l1.getLatitude())) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return R * c; // Distance in km
	}

}
