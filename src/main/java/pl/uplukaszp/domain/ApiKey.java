package pl.uplukaszp.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.Data;

/**
 * Represents an Api Key used to authorize user sensors.
 */
@Data
@Entity
public class ApiKey {

	@Id
	@GeneratedValue
	private Long id;

	@Column(unique = true, nullable = false)
	private String accessKey;

	@OneToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	private UserData owner;
}
