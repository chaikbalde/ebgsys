package net.hub4u.ebgsys.web.controllers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.hub4u.ebgsys.entities.Supplier;
import net.hub4u.ebgsys.services.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/suppliers")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class SupplierController {

    @Autowired
    SupplierService supplierService;

    @GetMapping("/settings")
    public String getAllSuppliers(Model model) {
        loadSuppliers(model);
        return "settingssuppliers";
    }

    @PostMapping("/create")
    public String createSupplier(Supplier supplier, Model model) {
        Supplier supplierCreated  = supplierService.createSupplier(supplier);
        model.addAttribute("supplierCreated", supplierCreated);
        loadSuppliers(model);
        return "settingssuppliers";
    }

    @GetMapping("/delete/{id}")
    public String deleteSupplier(@PathVariable Long id, Model model) {
        supplierService.deleteSupplier(id);
        log.info("deleteSupplier() - Removed Supplier with ID: " + id);

        model.addAttribute("supplierDeleted", id);
        loadSuppliers(model);

        return "settingssuppliers";
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~
    // HELPERS
    // ~~~~~~~~~~~~~~~~~~~~~~~~
    private void loadSuppliers(Model model) {
        List<Supplier> suppliers   = supplierService.fetchAllSuppliers();

        model.addAttribute("sidemenuSettings", true);
        model.addAttribute("subSidemenuSuppliers", true);

        model.addAttribute("suppliers", suppliers);
        model.addAttribute("supplier", new Supplier());
    }

}
