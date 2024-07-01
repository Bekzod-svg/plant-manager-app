package com.example.plant_service.controller;

import com.example.plant_service.dto.InstallationRequest;
import com.example.plant_service.model.HydrogenInstallation;
import com.example.plant_service.service.HydrogenInstallationService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.network.Mode;
import org.bouncycastle.math.raw.Mod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
//@RequestMapping("/installations")
@RequiredArgsConstructor
public class ThymeleafController {

    private final HydrogenInstallationService plantService;

    @GetMapping
    public String getAllInstallations(@RequestParam(name = "sort", required = false) String sortField, Model model){
        List<HydrogenInstallation> installations;
        if(sortField != null){
            installations = plantService.getAllInstallationsSorted(sortField);
        }else {
            installations = plantService.getAllInstallations();
        }
        model.addAttribute("installations", installations);
        return "installations/index";
    }

    @GetMapping("/{id}")
    public String getInstallation(@PathVariable Long id, Model model){
        HydrogenInstallation installation = plantService.getInstallation(id);
        model.addAttribute("installation", installation);
        return "installations/detail";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model){
        model.addAttribute("installation", new HydrogenInstallation());
        return "installations/new";
    }

    @PostMapping
    public String createInstallation(@ModelAttribute InstallationRequest request){
        plantService.createInstallation(request.getStatus(), request.getLocation(), request.getUserId());
        return "redirect:/installations";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model){
        HydrogenInstallation installation = plantService.getInstallation(id);
        model.addAttribute("installation", installation);
        return "installations/edit";
    }

    @PostMapping ("/{id}")
    public String updateInstallation(@PathVariable Long id, @ModelAttribute HydrogenInstallation installation){
        plantService.updateInstallation(id, installation);
        return "redirect:/installations";
    }

    @GetMapping("/search")
    public String searchInstallation(@RequestParam String query, Model model){
        List<HydrogenInstallation> installations = plantService.searchInstallations(query);
        model.addAttribute("installations", installations);
        return "installations/index";
    }

    @PostMapping("/{id}/delete")
    public String deleteInstallation(@PathVariable Long id) {
        plantService.deleteInstallation(id);
        return "redirect:/installations";
    }

}
