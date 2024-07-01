package spring.monitoring.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import spring.monitoring.configuration.WebClientConfig;
import spring.monitoring.dto.InstallationRequest;
import spring.monitoring.entity.*;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InstallationService {

    private final WebClient.Builder webClientBuilder;
    private WebClient webClient;

    @Value("${plant.url}")
    private String plantServiceUrl;
    @Value("${user-service.url}")
    private String userServiceUrl;
    @PostConstruct
    private void init() {
        this.webClient = webClientBuilder.baseUrl(plantServiceUrl).build();
    }

//    public InstallationService(WebClient.Builder webClientBuilder) {
//        this.webClient = webClientBuilder.baseUrl(plantServiceUrl).build();
//    }

//    public Mono<HydrogenInstallation> createInstallation(StatusType status, Location location, Long userId){
//        return webClient.get()
//                .uri(uriBuilder -> uriBuilder.path("/api/v1/installations/user/{userId}").build(userId))
//                .retrieve()
//                .onStatus(HttpStatusCode::is4xxClientError, response -> {
//                    if(response.statusCode() == HttpStatus.NOT_FOUND){
//                        return Mono.empty();
//                    } else {
//                        return response.createException().flatMap(Mono::error);
//                    }
//                })
//                .bodyToMono(HydrogenInstallation.class)
//                .flatMap(existingInstallation -> {
//                    if(existingInstallation != null){
//                        return Mono.error(new IllegalArgumentException("User already has an installation"));
//                    }
//                    return webClient.get()
//                            .uri(uriBuilder -> uriBuilder.path("/user-service/{userId}").build(userId))
//                            .retrieve()
//                            .bodyToMono(InstallationUser.class)
//                            .flatMap(user -> {
//                                if(user == null){
//                                    return Mono.error(new IllegalArgumentException("User not found"));
//                                }
//                                HydrogenInstallation installation = new HydrogenInstallation();
//                                installation.setStatus(status);
//                                installation.setLocation(location);
//                                installation.setUserId(userId);
//                                installation.setOwner(user);
//
//                                return webClient.post()
//                                        .uri("/api/v1/installations")
//                                        .bodyValue(installation)
//                                        .retrieve()
//                                        .bodyToMono(HydrogenInstallation.class);
//                            });
//                });
//    }

//    public Mono<InstallationRequest> createInstallation(StatusType status, Location location, Long userId){
//        InstallationRequest plant = new InstallationRequest(status, location, userId);
//        return webClient.post()
//                .uri("/api/v1/installations")
//                .bodyValue(plant)
//                .retrieve()
//                .bodyToMono(InstallationRequest.class);
//    }

    public Mono<InstallationRequest> createInstallation(StatusType status, Location location, Long userId) {

        InstallationRequest plant = new InstallationRequest(status, location, userId);
        try {
            String payload = new ObjectMapper().writeValueAsString(plant);
            System.out.println("Request payload: " + payload);
            log.info("Request payload: {}", payload);
        } catch (JsonProcessingException e) {
            log.error("Error serializing request payload", e);
            throw new RuntimeException(e);
        }

        return webClient.post()
                .uri("/api/v1/installations")
                .bodyValue(plant)
                .retrieve()
                .bodyToMono(InstallationRequest.class)
                .doOnError(error -> log.error("Error during installation creation", error));
   }

    public Mono<HydrogenInstallation> assignUserToInstallation(Long installationId, Long userId){
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/installations/user/{userId}").build(userId))
                .retrieve()
                .bodyToMono(HydrogenInstallation.class)
                .flatMap(inst -> {
                    if(inst != null && inst.getId().equals(installationId)){
                        return Mono.error(new IllegalArgumentException("User already has installation"));
                    }
                    return webClient.get()
                            .uri(uriBuilder -> uriBuilder.path("/user-service/{userId}").build(userId))
                            .retrieve()
                            .bodyToMono(InstallationUser.class)
                            .flatMap(user -> {
                                if(user == null){
                                    return Mono.error(new IllegalArgumentException("User not found"));
                                }
                                return webClient.get()
                                        .uri(uriBuilder -> uriBuilder.path("/api/v1/installations/{id}").build(installationId))
                                        .retrieve()
                                        .bodyToMono(HydrogenInstallation.class)
                                        .flatMap(plant -> {
                                            if(plant == null){
                                                return Mono.error(new IllegalArgumentException("Installation was not found"));
                                            }
                                            plant.setUserId(userId);
                                            plant.setOwner(user);
                                            return webClient.put()
                                                    //Should be changed to api/v1/installations/{id}/user
                                                    .uri(uriBuilder -> uriBuilder.path("/api/v1/installations/{id}").build(installationId))
                                                    .bodyValue(plant)
                                                    .retrieve()
                                                    .bodyToMono(HydrogenInstallation.class);
                                        });
                            });
                });
    }

    public Mono<HydrogenInstallation> getInstallation(Long id){
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/installations/{id}").build(id))
                .retrieve()
                .bodyToMono(HydrogenInstallation.class);
    }

    public Flux<HydrogenInstallation> getAllInstallations(){
        return webClient.get()
                .uri("/api/v1/installations")
                .retrieve()
                .bodyToFlux(HydrogenInstallation.class);
    }

    public Mono<Void> deleteInstallation(Long id){
        return webClient.delete()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/installations/{id}").build(id))
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Mono<HydrogenInstallation> updateInstallation(Long id, HydrogenInstallation request){
        return webClient.put()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/installations/{id}").build(id))
                .bodyValue(request)
                .retrieve()
                .bodyToMono(HydrogenInstallation.class);
    }

    public Flux<HydrogenInstallation> searchInstallation(String query){
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/installations/search").queryParam("query", query).build())
                .retrieve()
                .bodyToFlux(HydrogenInstallation.class);
    }

    public Flux<HydrogenInstallation> getAllInstallationsSorted(String sortField){
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/installations").queryParam("sort", sortField).build())
                .retrieve()
                .bodyToFlux(HydrogenInstallation.class);
    }










}
