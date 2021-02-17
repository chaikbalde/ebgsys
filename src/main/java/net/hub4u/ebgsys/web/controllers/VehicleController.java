package net.hub4u.ebgsys.web.controllers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.hub4u.ebgsys.entities.Product;
import net.hub4u.ebgsys.entities.Vehicle;
import net.hub4u.ebgsys.services.ProductService;
import net.hub4u.ebgsys.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/vehicles")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class VehicleController {

    @Autowired
    VehicleService vehicleService;

    @GetMapping("/settings")
    public String getAllVehicles(Model model) {
        loadVehicles(model);
        return "settingsvehicles";
    }

    @PostMapping("/create")
    public String createProduct(Vehicle vehicle, Model model) {
        Vehicle vehicleCreated = vehicleService.createVehicle(vehicle);
        model.addAttribute("vehicleCreated", vehicleCreated);
        loadVehicles(model);
        return "settingsvehicles";
    }

    @GetMapping("/delete/{id}")
    public String deleteVehicle(@PathVariable Long id, Model model) {
        vehicleService.deleteVehicle(id);
        log.info("deleteVehicle() - Removed Vehicle with ID: " + id);

        model.addAttribute("vehicleDeleted", id);
        loadVehicles(model);

        return "settingsvehicles";
    }


    // HELPERS
    private void loadVehicles(Model model) {
        List<Vehicle> vehicles   = vehicleService.fetchAllVehicles();

        model.addAttribute("sidemenuSettings", true);
        model.addAttribute("subSidemenuVehicles", true);

        model.addAttribute("vehicles", vehicles);
        model.addAttribute("vehicle", new Vehicle());
    }

}
