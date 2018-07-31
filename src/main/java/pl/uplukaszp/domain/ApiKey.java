package pl.uplukaszp.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Data;

@Data
@Entity
public class ApiKey {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(unique=true,nullable=false)
	private String accessKey;
	
	@OneToOne
	private UserData owner;
}
