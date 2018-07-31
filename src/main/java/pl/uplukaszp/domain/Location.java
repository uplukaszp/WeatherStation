package pl.uplukaszp.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Location {
	@Id
	@GeneratedValue
	private Long id;
	@Column(nullable=false)
	private double latitude;
	@Column(nullable=false)
	private double longitude; 
}
