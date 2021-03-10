package net.hub4u.ebgsys.web.controllers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.hub4u.ebgsys.entities.Purchase;
import net.hub4u.ebgsys.services.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/purchases")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseController {

    @Autowired
    PurchaseService purchaseService;

    @GetMapping
    public String getPurchasesHome(Model model) {

        List<Purchase> purchases = purchaseService.fetchAllPurchases();

        model.addAttribute("purchases", purchases);

        model.addAttribute("sidemenuPurchases", true);
        model.addAttribute("subSidemenuPurchases", true);

        return "purchases";
    }

    @PostMapping("/create")
    public String createPurchase(Purchase purchase, Model model) {

        Purchase createdPurchase = purchaseService.createSale(purchase);

        model.addAttribute("purchaseCreated", createdPurchase);

        model.addAttribute("sidemenuPurchases", true);
        model.addAttribute("subSidemenuPurchases", true);

        return "purchases";
    }


}
