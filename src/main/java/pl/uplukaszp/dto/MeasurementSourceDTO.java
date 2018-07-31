package pl.uplukaszp.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class MeasurementSourceDTO {

	private static final String nameMessage="Name cannot be empty";
	private static final String latitudeMessage="Please provide correct latitude";
	private  static final String longitudeMessage="Please provide correct longitude";
	@NotNull(message=nameMessage)
	private String name;
	@NotNull(message=latitudeMessage)
	@Pattern(regexp="^(\\+|-)?(?:90(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-8][0-9])(?:(?:\\.[0-9]{1,6})?))$",message=latitudeMessage)
	private String latitude;
	@NotNull(message=longitudeMessage)
	@Pattern(regexp="^(\\+|-)?(?:180(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,6})?))$",message=longitudeMessage)
	private String longitude;
	@NotNull
	private boolean publicly;
}
