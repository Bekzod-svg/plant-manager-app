package com.example.plant_service.config;

import com.example.plant_service.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class UserServiceClient {
    private final RestTemplate restTemplate;

    public User getUserById(Long id){
        return restTemplate.getForObject("http://localhost:8085/api/v1/users/" + id, User.class);
    }
}
