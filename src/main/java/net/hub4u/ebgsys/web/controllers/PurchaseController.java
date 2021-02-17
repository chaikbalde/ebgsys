package net.hub4u.ebgsys.web.controllers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/purchases")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseController {


    @GetMapping
    public String getPurchasesHome(Model model) {
        model.addAttribute("sidemenuPurchases", true);
        model.addAttribute("subSidemenuIntro", true);
        return "purchasesintro";
    }

}
