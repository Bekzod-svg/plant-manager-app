package spring.monitoring.controller;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String username;
    private String password;
}