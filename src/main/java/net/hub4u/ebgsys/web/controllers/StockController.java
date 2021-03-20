package net.hub4u.ebgsys.web.controllers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.hub4u.ebgsys.entities.Product;
import net.hub4u.ebgsys.entities.StockItem;
import net.hub4u.ebgsys.entities.StockItemStatus;
import net.hub4u.ebgsys.services.ProductService;
import net.hub4u.ebgsys.services.StockItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/stocks")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class StockController {


    @Autowired
    ProductService productService;

    @Autowired
    StockItemService stockItemService;

    @GetMapping
    public String getStocks(Model model) {

        List<Product> products = productService.fetchAllProducts();
        model.addAttribute("products", products);

        List<StockItem> stockItems = stockItemService.fetchAllStockItems();
        setStatusView(stockItems);

        model.addAttribute("stockItems", stockItems);

        model.addAttribute("sidemenuStocks", true);
        model.addAttribute("subSidemenuStocks", true);

        return "stocks";
    }

    private void setStatusView(List<StockItem> stockItems) {
        stockItems.forEach(item -> {
            if (item.getStatus() == StockItemStatus.AVAILABLE) {
                item.setStatusView("AVAILABLE");
            } else if (item.getStatus() == StockItemStatus.WARNING) {
                item.setStatusView("WARNING");
            } else if (item.getStatus() == StockItemStatus.UNAVAILABLE) {
                item.setStatusView("UNAVAILABLE");
            }
        });
    }

}
