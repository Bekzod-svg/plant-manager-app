package spring.notification.repository;

import org.springframework.data.repository.CrudRepository;
import spring.notification.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {
}
