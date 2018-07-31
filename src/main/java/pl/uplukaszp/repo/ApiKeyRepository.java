package pl.uplukaszp.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.uplukaszp.domain.ApiKey;
import pl.uplukaszp.domain.projections.ApiKeyWithoutOwner;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, Long>{
	ApiKeyWithoutOwner findOneByOwnerEmail(String email);
}
