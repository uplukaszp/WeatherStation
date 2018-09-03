package pl.uplukaszp.dto;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class MeasurementDTO {
	List<Map<@NotNull String,@NotNull String>> measurements;
}
