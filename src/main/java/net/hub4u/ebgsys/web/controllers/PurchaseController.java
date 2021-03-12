package net.hub4u.ebgsys.web.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.hub4u.ebgsys.entities.Product;
import net.hub4u.ebgsys.entities.Purchase;
import net.hub4u.ebgsys.entities.PurchaseProduct;
import net.hub4u.ebgsys.entities.Sale;
import net.hub4u.ebgsys.entities.ShopCartItem;
import net.hub4u.ebgsys.entities.Supplier;
import net.hub4u.ebgsys.frwk.EbgSysUtils;
import net.hub4u.ebgsys.services.ProductService;
import net.hub4u.ebgsys.services.PurchaseService;
import net.hub4u.ebgsys.services.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/purchases")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class PurchaseController {

    @Autowired
    PurchaseService purchaseService;
    @Autowired
    ProductService productService;
    @Autowired
    SupplierService supplierService;

    @GetMapping
    public String getPurchasesHome(Model model) {
        buildPurchasesModel(model);
        return "purchases";
    }

    @PostMapping("/create")
    public String createPurchase(@ModelAttribute Purchase purchase, BindingResult bindingResult, Model model, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            log.error("createCreditSale() -  Failed processing form. Errors: " + bindingResult.getAllErrors());
            buildPurchasesModel(model);
            return "purchases";
        }
        if (purchase.getSupplier() == null) {
            log.error("createCreditSale() -  Failed processing form. Supplier CANNOT BE NULL ! ");
            buildPurchasesModel(model);
            return "purchases";
        }

        purchase.setReference(purchase.getNextReferenceView());


        List<ShopCartItem> cartItems = (List<ShopCartItem>) request.getSession().getAttribute(EbgSysUtils.SESSION_SHOPPING_CART_PREFIX_BUY);
        if (cartItems != null) {

            List<PurchaseProduct> purchaseProducts = cartItems.stream().map(cartItem -> {
                PurchaseProduct purchaseProduct = new PurchaseProduct();
                purchaseProduct.setQuantity(cartItem.getQuantity());
                purchaseProduct.setUnitPrice(cartItem.getUnitPrice());
                purchaseProduct.setSubTotalPrice(cartItem.getTotalPrice());

                String productReference = cartItem.getProductReference();
                Product product = productService.fetchProductByReference(productReference);

                purchaseProduct.setProduct(product);
                product.getPurchaseProducts().add(purchaseProduct);

                purchaseProduct.setPurchase(purchase);
                purchase.getPurchaseProducts().add(purchaseProduct);

                return purchaseProduct;
            }).collect(Collectors.toList()) ;

            BigDecimal totalAmount = cartItems.stream()
                    .map(ShopCartItem::getTotalPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            purchase.setAmount(totalAmount);

            purchase.setPurchaseProducts(purchaseProducts);

            Supplier supplier = supplierService.fetchSupplierByReference(purchase.getSupplier().getReference());
            purchase.setSupplier(supplier);
            supplier.getPurchases().add(purchase);

            Purchase createdPurchase = purchaseService.createPurchase(purchase);

            request.getSession().removeAttribute(EbgSysUtils.SESSION_SHOPPING_CART_PREFIX_BUY);
            log.debug("createPurchase()  - Cleared Purchase ShoppingCart ");

            model.addAttribute("purchaseCreated", createdPurchase);

        } else {
            log.warn("createPurchase() - Shopping cart is empty. Add to cart first");
        }



        buildPurchasesModel(model);

        return "purchases";
    }

    /**
     *
     * */
    @GetMapping("/addtopurchasecart/{productId}")
    public @ResponseBody
    String addToCart(
            @PathVariable Long productId,
            @RequestParam Integer qty,
            @RequestParam BigDecimal price,
            HttpServletRequest request) {

        Product product = productService.fetchProduct(productId);
        log.debug("addToCart() - Adding Product '"+product.getName()+"' to Cart");

        List<ShopCartItem> cartItems = (List<ShopCartItem>) request.getSession().getAttribute(EbgSysUtils.SESSION_SHOPPING_CART_PREFIX_BUY);

        if (cartItems == null) {
            cartItems = new ArrayList<ShopCartItem>();
            ShopCartItem item = buildCartItem(product, qty, price);
            cartItems.add(item);

            request.getSession().setAttribute(EbgSysUtils.SESSION_SHOPPING_CART_PREFIX_BUY, cartItems);

        }  else {
            ShopCartItem item = buildCartItem(product, qty, price);
            cartItems.add(item);
        }

        String itemsListAsjson = "";
        try {
            itemsListAsjson  = new ObjectMapper().writeValueAsString(cartItems);
            log.debug("addToCart() - itemsListAsjson: \n" + itemsListAsjson) ;

        } catch (JsonProcessingException e) {
            log.error(">>> ERROR - Failed getting JSON object from Java List: " + cartItems);
        }

        return itemsListAsjson;

    }

    /**
     *
     * */
    @GetMapping("/clearcartsession")
    public @ResponseBody
    String clearCartSession(HttpServletRequest request) {
        if(request.getSession().getAttribute(EbgSysUtils.SESSION_SHOPPING_CART_PREFIX_BUY) != null) {
            request.getSession().removeAttribute(EbgSysUtils.SESSION_SHOPPING_CART_PREFIX_BUY);
            log.debug("clearCartSession()  - Cleared Purchase ShoppingCart ");
        }
        return ""+99;
    }


    /**/
    private void buildPurchasesModel(Model model) {
        List<Purchase> purchases = purchaseService.fetchAllPurchases();
        List<Product> products = productService.fetchAllProducts();
        List<Supplier> suppliers = supplierService.fetchAllSuppliers();

        Purchase maxPurchase = purchases
                .stream()
                .max(Comparator.comparing(Purchase::getReference))
                .orElse(new Purchase());

        model.addAttribute("maxPurchase", maxPurchase);

        model.addAttribute("purchases", purchases);
        model.addAttribute("productsForm", products);
        model.addAttribute("suppliers", suppliers);

        Purchase purchase = new Purchase();
        String nextRef = EbgSysUtils.retrieveNextReference("APP-EBG-", 7, purchases.stream().map(p -> p.getReference()).collect(Collectors.toList()));
        purchase.setNextReferenceView(nextRef);
        model.addAttribute("purchase", purchase);

        model.addAttribute("sidemenuPurchases", true);
        model.addAttribute("subSidemenuPurchases", true);
    }

    /**
     *
     * */
    private ShopCartItem buildCartItem(Product product, int quantity, BigDecimal price) {
        ShopCartItem cartItem = new ShopCartItem();
        cartItem.setProductName(product.getName());
        cartItem.setProductReference(product.getReference());
        cartItem.setUnitPrice(price);

        cartItem.setQuantity(quantity);

        BigDecimal totalPrice = cartItem.getUnitPrice().multiply(new BigDecimal(quantity));
        cartItem.setTotalPrice(totalPrice);

        return cartItem;
    }



}
