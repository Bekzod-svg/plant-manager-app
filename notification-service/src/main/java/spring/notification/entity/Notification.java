package spring.notification.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Notification {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    private Date timestamp;

    @NonNull
    private String message;

    @NonNull
    private NotificationType type;

    @NonNull
    @ManyToOne(targetEntity=User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
