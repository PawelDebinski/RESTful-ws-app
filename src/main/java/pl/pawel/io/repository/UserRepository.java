package pl.pawel.io.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pawel.io.entity.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long > {

}
