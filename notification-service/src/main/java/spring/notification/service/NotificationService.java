package spring.notification.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.notification.entity.Notification;
import spring.notification.entity.NotificationType;
import spring.notification.entity.User;
import spring.notification.repository.NotificationRepository;
import spring.notification.repository.UserRepository;

import java.util.Date;
import java.util.Optional;

@Service
public class NotificationService {
    private NotificationRepository notificationRepository;
    private UserRepository userRepository;
    @Autowired
    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }
    public void createNotification(Long userId, String message) {
        Optional<User> user = this.userRepository.findById(userId);
        if(user.isPresent()) {
            Notification notification = new Notification();
            notification.setMessage(new User(userId));
            notificationRepository.save(notification);
        }
    }
}
