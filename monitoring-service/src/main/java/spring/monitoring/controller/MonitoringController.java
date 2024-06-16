package spring.monitoring.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import reactor.core.publisher.Mono;
import spring.monitoring.entity.User;

import java.util.Objects;

@Controller
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
        Mono<User> monoUser = webClient.post()
                .uri("http://localhost:8085/users")
                .body(BodyInserters.fromValue(user))
                .retrieve()
                .bodyToMono(User.class);
        log.info(Objects.requireNonNull(monoUser.block()).getUsername());

        redirectAttributes.addFlashAttribute("registrationSuccessMessage", "Registration successful");
        return "redirect:/loginPage";
    }
}
