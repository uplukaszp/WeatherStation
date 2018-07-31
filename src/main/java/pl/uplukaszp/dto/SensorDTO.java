package pl.uplukaszp.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SensorDTO {
	@NotNull
	private String unit;
	@NotNull
	private String source;
}
