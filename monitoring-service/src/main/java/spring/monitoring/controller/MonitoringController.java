package spring.monitoring.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import reactor.core.publisher.Mono;
import spring.monitoring.entity.User;

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

    @GetMapping("/")
    public String index(Model model) {
        return "redirect:/loginPage";
    }
    @GetMapping("/loginPage")
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
                .uri("http://localhost:8085/user-service")
                .body(BodyInserters.fromValue(user))
                .retrieve()
                .bodyToMono(User.class)
                .block();

        redirectAttributes.addFlashAttribute("registrationSuccessMessage", "Registration successful");
        return "redirect:/loginPage";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password, RedirectAttributes redirectAttributes, Model model) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        // Set username and password
        formData.add("username", username);
        formData.add("password", password);

        User user = webClient.post()
                .uri("http://localhost:8085/user-service/login",
                        uriBuilder -> uriBuilder
                                .queryParams(formData).build())
                .retrieve()
                .bodyToMono(User.class)
                .block();
        if(user == null) {
            return "redirect:/loginPage";
        }
        redirectAttributes.addFlashAttribute("loginSuccessMessage", "Login successful");
        model.addAttribute("actualUser", user);
        return "dashboard";
    }
}
