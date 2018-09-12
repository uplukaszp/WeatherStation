package pl.uplukaszp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import pl.uplukaszp.dto.projections.ApiKeyWithoutOwner;
import pl.uplukaszp.repo.ApiKeyRepository;

@RestController
public class ApiKeyController {

	@Autowired
	private ApiKeyRepository repo;

	/**
	 * @return An Api key, related to user
	 */
	@GetMapping("/key")
	@ResponseBody
	public ApiKeyWithoutOwner getKey(Authentication auth) {
		return repo.findOneByOwnerEmail(auth.getName());
	}
}
