package spring.notification.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import spring.notification.entity.Notification;
@Repository
public interface NotificationRepository extends CrudRepository<Notification, Long> {
}
