package pl.uplukaszp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.uplukaszp.domain.ApiKey;
import pl.uplukaszp.dto.projections.ApiKeyWithoutOwner;
import pl.uplukaszp.repo.ApiKeyRepository;

@Service
public class ApiKeyServiceImpl {
	
	@Autowired
	private ApiKeyRepository repo;
	
	public ApiKeyWithoutOwner findOneByOwnerEmail(String email) {
		return repo.findOneByOwnerEmail(email);
	}

	public void save(ApiKey key) {
		repo.save(key);
	}
}
