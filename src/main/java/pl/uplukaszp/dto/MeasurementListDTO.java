package pl.uplukaszp.dto;

import java.util.List;

import javax.validation.Valid;

import lombok.Data;

@Data
public class MeasurementListDTO {
	
	@Valid
	private List<MeasurementDTO> measurements;
}
