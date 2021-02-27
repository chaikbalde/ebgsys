package net.hub4u.ebgsys.web.controllers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.hub4u.ebgsys.entities.Product;
import net.hub4u.ebgsys.frwk.EbgSysUtils;
import net.hub4u.ebgsys.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/products")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class ProductController {

    static final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance(new Locale("fr", "FR"));


    @Autowired
    ProductService productService;

    @GetMapping("/settings")
    public String getAllProducts(Model model) {
        loadProducts(model);
        return "settingsproducts";
    }

    @PostMapping("/create")
    public String createProduct(Product product, Model model) {
        product.setReference(product.getNextReferenceView());
        Product productCreated = productService.createProduct(product);
        model.addAttribute("productCreated", productCreated);
        loadProducts(model);
        return "settingsproducts";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, Model model) {
        productService.deleteProduct(id);
        log.info("deleteProduct() - Removed Product with ID: " + id);

        model.addAttribute("productDeleted", id);
        loadProducts(model);

        return "settingsproducts";
    }

    /**
     *
     * */
    @GetMapping("/findproductpice/{productId}")
    public @ResponseBody String[] getProductPrice(@PathVariable Long productId) {
        Product product = productService.fetchProduct(productId);
        log.debug("getProductPrice() - Getting price from Product : " + product.getName());

//        return NUMBER_FORMAT.format(product.getUnitPrice()) + " | "+ NUMBER_FORMAT.format(product.getGrossPrice()) ;

        return new String[] {""+product.getUnitPrice(), ""+product.getGrossPrice(), ""+NUMBER_FORMAT.format(product.getUnitPrice()), ""+NUMBER_FORMAT.format(product.getGrossPrice())} ;

//        return new String[] {""+NUMBER_FORMAT.format(product.getUnitPrice()), ""+NUMBER_FORMAT.format(product.getGrossPrice())} ;
    }


    // HELPERS
    private void loadProducts(Model model) {
        List<Product> products   = productService.fetchAllProducts();

        model.addAttribute("sidemenuSettings", true);
        model.addAttribute("subSidemenuProducts", true);

        Product product = new Product();
        String productNextRef = EbgSysUtils.retrieveNextReference("PRD-EBG-", 7, products.stream().map(p -> p.getReference()).collect(Collectors.toList()));
        product.setNextReferenceView(productNextRef);

        model.addAttribute("products", products);
        model.addAttribute("product", product);
    }
}
