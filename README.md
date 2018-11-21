# Weather Station

Service enables the acquisition of weather data (temperature, wind speed, humidity, etc.). Data is sent using http requests. The user can download weather data from places selected by him.

## Installation

Use maven to install service.

```maven
maven install
```

## Configuration

You should configure the following environment variables to run the program:
* settings.allowed_orgins
* settings.expiration_time
* settings.secret_key
* tokens.locationIQ

## Tech stack
* Java 8
* Spring Boot 2.0.3
* Spring Security
* Spring Data
* Lombok
* Maven
* MySQL
* JWT
* LocationIQ API

## Related projects:
An exemplary frontend that supports this service [link](https://github.com/uplukaszp/WeatherStationFront)
