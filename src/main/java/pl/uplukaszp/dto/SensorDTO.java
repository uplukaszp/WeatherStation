package pl.uplukaszp.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SensorDTO {
	@NotNull
	private Long unit;
	@NotNull
	private Long source;
}
