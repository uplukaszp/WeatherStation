package pl.uplukaszp.dto;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class MeasurementDTO {

	@NotNull
	private Long id;
	@NotNull
	private Float value;
	@DateTimeFormat
	private String date;

}
