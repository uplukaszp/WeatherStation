package pl.uplukaszp.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class PositionDTO {
	
	@NotNull(message="Can not be empty")
	String Adress;
	
	@Min(value=0,message="Range must be greater than 0")
	int range;
}
