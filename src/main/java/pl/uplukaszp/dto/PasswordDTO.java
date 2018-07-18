package pl.uplukaszp.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class PasswordDTO {
	
private static final String passwordMessage="Password must be at least 6 characters.";
	
	@NotNull(message=passwordMessage)
	@Pattern(regexp=".{6,}", message=passwordMessage)
	String password;

}
