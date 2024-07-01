package spring.monitoring.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.network.Mode;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import spring.monitoring.dto.InstallationRequest;
import spring.monitoring.entity.HydrogenInstallation;
import spring.monitoring.service.InstallationService;

import java.util.List;

@Controller
@RequestMapping("/installations")
@RequiredArgsConstructor
public class InstalltionServiceController {
    private final InstallationService plantService;


    @GetMapping
    public String getAllInstallations(@RequestParam(name = "sort", required = false) String sortField, Model model){
        if(sortField != null){
            plantService.getAllInstallationsSorted(sortField)
                    .collectList()
                    .doOnSuccess(installations -> model.addAttribute("installations", installations))
                    .block();
//                    .subscribe(installations -> model.addAttribute("installations", installations));
        }else {
            plantService.getAllInstallations()
                    .collectList()
                    .doOnSuccess(installations -> model.addAttribute("installations", installations))
                    .block();
        }
        return "installations/index";
    }

    @GetMapping("/{id}")
    public String getInstallation(@PathVariable Long id, Model model){
        plantService.getInstallation(id)
                .doOnSuccess(installation -> model.addAttribute("installation", installation));
        return "installations/detail";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model){
        model.addAttribute("installation", new HydrogenInstallation());
        return "installations/new";
    }

    @PostMapping
    public String createInstallation(@ModelAttribute InstallationRequest request) {
        plantService.createInstallation(request.getStatus(), request.getLocation(), request.getUserId())
                .block();
        return "redirect:/installations";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model){
        plantService.getInstallation(id)
                .doOnSuccess(installation -> model.addAttribute("installation", installation))
                .block();
        return "installations/edit";
    }

    @PostMapping("/{id}")
    public String updateInstallation(@PathVariable Long id, @ModelAttribute HydrogenInstallation installation, Model model){
        plantService.updateInstallation(id,installation)
                .block();
        return "redirect:/installations";
    }

    @GetMapping("/search")
    public String searchInstallations(@RequestParam String query, Model model){
        plantService.searchInstallation(query)
                .collectList()
                .doOnSuccess(installations -> model.addAttribute("installations", installations))
                .block();
        return "installations/index";
    }

    @PostMapping("/{id}/delete")
    public String deleteInstallation(@PathVariable Long id){
        plantService.deleteInstallation(id)
                .block();
        return "redirect:/installations";
    }


    //


    //    @Autowired
//    public InstalltionServiceController(WebClient.Builder webClientBuilder, InstallationService plantService) {
//        this.webClient = webClientBuilder.build();
//        this.plantService = plantService;
//    }

//    @GetMapping("/installations")
//    public String getAllInstallations(Model model) {
//        Flux<HydrogenInstallation> installations = webClient.get()
//                .uri("/api/v1/installations")
//                .retrieve()
//                .bodyToFlux(HydrogenInstallation.class);
//
//        installations.collectList().subscribe(installationList -> {
//            model.addAttribute("installations", installationList);
//        });
//
//        return "installations/index";
//    }
    //GetMapping
    //public static void printMessage(){
    //     List<Installation> instls = new ArrayList<Installation>;
    //     insts,forEach(instls.getNotigication())
    // }
    //
    //
    //
    //
//
}
