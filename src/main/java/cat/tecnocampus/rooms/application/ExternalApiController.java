package cat.tecnocampus.rooms.application;

import cat.tecnocampus.rooms.application.dtos.HouseDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@Component
public class ExternalApiController {
	private final RestTemplate restTemplate;
	private final String REQUEST_URI = "https://anapioficeandfire.com/api/houses/";

	public ExternalApiController(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public HouseDTO getRandomHouse() {
		int randomHouse = new Random().nextInt(444) + 1;
		return restTemplate.getForObject(REQUEST_URI + randomHouse, HouseDTO.class);
	}
}

