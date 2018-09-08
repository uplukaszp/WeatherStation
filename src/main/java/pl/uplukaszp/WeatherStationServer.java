package pl.uplukaszp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
public class WeatherStationServer {

	public static void main(String[] args) {
		SpringApplication.run(WeatherStationServer.class, args);
	}
}
