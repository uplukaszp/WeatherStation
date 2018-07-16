package pl.uplukaszp.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class UserDataDTO {
	
	private static final String emailMessage="Please provide an email.";
	private static final String passwordMessage="Password must be at least 6 characters.";
	
	@NotNull(message=emailMessage)
	@Email(message=emailMessage)
	String email;
	@NotNull(message=passwordMessage)
	@Pattern(regexp=".{6,}", message=passwordMessage)
	String password;
}
