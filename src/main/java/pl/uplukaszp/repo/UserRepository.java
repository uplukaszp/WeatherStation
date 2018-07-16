package pl.uplukaszp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.uplukaszp.domain.UserData;

@Repository
public interface UserRepository extends JpaRepository<UserData, Long> {
	UserData findByEmail(String name);
}
