package spring.monitoring.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import reactor.core.publisher.Mono;
import spring.monitoring.entity.HydrogenInstallation;
import spring.monitoring.entity.User;

import java.util.List;
import java.util.Objects;

@Controller
@SessionAttributes("actualUser")
@Slf4j
public class MonitoringController {

    private final WebClient webClient;

    @Autowired
    public MonitoringController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @GetMapping
    public String loginPage(Model model) {
        return "login";
    }

    @GetMapping("/registrationPage")
    public String registrationPage(Model model) {
        model.addAttribute("userForm", new User());
        return "registration";
    }

    @PostMapping("/register")
    public String register(User user, RedirectAttributes redirectAttributes) {
        User monoUser = webClient.post()
                .uri("http://user-service/api/v1/users")
                .body(BodyInserters.fromValue(user))
                .retrieve()
                .bodyToMono(User.class)
                .block();

        redirectAttributes.addFlashAttribute("message", "Registration successful");
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password, RedirectAttributes redirectAttributes, Model model) {
        try {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("username", username);
            formData.add("password", password);

            User user = webClient.post()
                    .uri("http://user-service/api/v1/users/login",
                            uriBuilder -> uriBuilder
                                    .queryParams(formData).build())
                    .retrieve()
                    .bodyToMono(User.class)
                    .block();
            if(user == null) {
                return "login";
            }
            redirectAttributes.addFlashAttribute("message", "Login successful!");
            model.addAttribute("actualUser", user);
            return "home";

        } catch (WebClientResponseException.Unauthorized ex) {
            redirectAttributes.addFlashAttribute("message", "Unauthorized!");
            return "login";
        }
    }

    ///
//    @PostMapping("/login")
//    public String login(@RequestParam("username") String username,
//                        @RequestParam("password") String password, RedirectAttributes redirectAttributes, Model model) {
//        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
//
//        // Set username and password
//        formData.add("username", username);
//        formData.add("password", password);
//
//        User user = webClient.post()
//                .uri("http://user-service/api/v1/users/login",
//                        uriBuilder -> uriBuilder
//                                .queryParams(formData).build())
//                .retrieve()
//                .bodyToMono(User.class)
//                .block();
//        if(user == null) {
//            return "redirect:/loginPage";
//        }
//        redirectAttributes.addFlashAttribute("loginSuccessMessage", "Login successful");
//        model.addAttribute("actualUser", user);
//        return "redirect:/installations";
//    }



    @GetMapping("/homepage")
    public String homepage(Model model) {
        return "home";
    }

    @GetMapping("/logout")
    public String logout(RedirectAttributes redirectAttributes) {
        try {
            webClient.post()
                    .uri("http://user-service/api/v1/users/logout")
                    .retrieve()
                    .onStatus(statusCode -> !HttpStatus.OK.equals(statusCode),
                            res -> res.bodyToMono(String.class).map(Exception::new))
                    .bodyToMono(String.class)
                    .block();

            redirectAttributes.addFlashAttribute("message", "Logout successful!");
            return "login";
        } catch (Exception ex) {
            return "home";
        }
    }
}
