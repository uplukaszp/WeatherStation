package pl.uplukaszp.util;

import java.util.Comparator;

import pl.uplukaszp.domain.Location;
import pl.uplukaszp.domain.MeasurementSource;
import pl.uplukaszp.dto.projections.MeasurementSourceWithoutOwner;

public class LocationComparator implements Comparator<MeasurementSourceWithoutOwner>{

	Location target;
	public LocationComparator(Location target){
		this.target=target;
	}
	@Override
	public int compare(MeasurementSourceWithoutOwner o1, MeasurementSourceWithoutOwner o2) {
		Location l1=o1.getLocation();
		Location l2=o2.getLocation();
		double distance1=l1.distance(target);
		double distance2=l2.distance(target);
		return Double.compare(distance1, distance2);
	}

}
