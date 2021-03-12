package net.hub4u.ebgsys.web.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.hub4u.ebgsys.entities.Customer;
import net.hub4u.ebgsys.entities.CustomerType;
import net.hub4u.ebgsys.entities.Payment;
import net.hub4u.ebgsys.entities.Product;
import net.hub4u.ebgsys.entities.Sale;
import net.hub4u.ebgsys.entities.SaleProduct;
import net.hub4u.ebgsys.entities.SaleTxType;
import net.hub4u.ebgsys.entities.SaleType;
import net.hub4u.ebgsys.entities.ShopCartItem;
import net.hub4u.ebgsys.frwk.EbgSysUtils;
import net.hub4u.ebgsys.services.CustomerService;
import net.hub4u.ebgsys.services.ProductService;
import net.hub4u.ebgsys.services.SaleService;
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
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/sales")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class SaleController {

    final static String SALE_TYPE_RETAIL = "En Détails";
    final static String SALE_TYPE_WHOLESALE = "En Gros";

    @Autowired
    SaleService saleService;
    @Autowired
    ProductService productService;
    @Autowired
    CustomerService customerService;

    @GetMapping("/cash")
    public String getCashSales(Model model) {
        loadCashSales(model);
        return "salescash";
    }

    @GetMapping("/credit")
    public String getCreditSales(Model model) {
        loadCreditSales(model);
        return "salescredit";
    }

    @PostMapping("/createcashsale")
    public String createCashSale(@ModelAttribute Sale cashSale,  BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            log.error("createCashSale() -  Failed processing form. Errors: " + bindingResult.getAllErrors());
        }

//        cashSale.setReference(cashSale.getNextReferenceView());
//
//        Date saleDt = cashSale.getSaleDate();
//
//        if (cashSale.getProduct() == null) {
//            log.error("createCashSale() - Failed creating Sale. Product is CANNOT BE NULL !");
//            loadCashSales(model);
//            return "salescash";
//        }
//
//        long prodId = cashSale.getProduct().getId();
//        Product product = productService.fetchProduct(prodId);
//        product.getSales().add(cashSale);
//        cashSale.setProduct(product);
//
//        BigDecimal unitPrice = retrieveUnitPrice(cashSale, product);
//
//        BigDecimal totalPrice = unitPrice.multiply(new BigDecimal(cashSale.getQuantity()));
//        cashSale.setAmount(totalPrice);
//
//        cashSale.setUnitPriceView(unitPrice);
//
//        cashSale.setSaleTxType(SaleTxType.CASH);
//        Sale createdSale = saleService.createSale(cashSale);
//        log.info("createCashSale() - Created sale '"+createdSale.getReference()+"' with Id '"+createdSale.getId()+"'");

        loadCashSales(model);

        return "salescash";
    }

    ////////////////////////////////////////  START

    @PostMapping("/createcreditsale")
    public String createCreditSale(@ModelAttribute Sale creditSale,  BindingResult bindingResult, Model model, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            log.error("createCreditSale() -  Failed processing form. Errors: " + bindingResult.getAllErrors());
            loadCreditSales(model);
            return "salescredit";
        }

        creditSale.setReference(creditSale.getNextReferenceView());

        if (creditSale.getCustomer() == null) {
            log.error("createCreditSale() - Failed creating Sale. Customer CANNOT BE NULL !");
            loadCreditSales(model);
            return "salescredit";
        }

        long customerId = creditSale.getCustomer().getId();
        Customer customer = customerService.fetchCustomer(customerId);
        customer.getSales().add(creditSale);
        creditSale.setCustomer(customer);

        // [2] Get CartContent:
        // Products:
        // id, name, reference, unit_price, sale_type, qty, sub_total

        BigDecimal totalAmount = new BigDecimal("0");

        List<ShopCartItem> cartItems = (List<ShopCartItem>) request.getSession().getAttribute(EbgSysUtils.SESSION_SHOPPING_CART_PREFIX_SELL);
        for (ShopCartItem cartItem: cartItems) {
            Product product = productService.fetchProductByReference(cartItem.getProductReference());

            SaleProduct saleProduct = new SaleProduct();
            saleProduct.setSaleType(cartItem.getSaleType());
            saleProduct.setSubTotalPrice(cartItem.getTotalPrice());
            saleProduct.setQuantity(cartItem.getQuantity());
            saleProduct.setUnitPrice(cartItem.getUnitPrice());

            product.getSaleProducts().add(saleProduct);
            saleProduct.setProduct(product);
            creditSale.getSaleProducts().add(saleProduct);
            saleProduct.setSale(creditSale);

            totalAmount = totalAmount.add(cartItem.getTotalPrice());

        }

        creditSale.setAmount(totalAmount);

        BigDecimal paidAmount = creditSale.getPaidAmount();
        if (paidAmount != null) {
            Payment payment = new Payment();
            payment.setAmount(paidAmount);
            payment.setPayDate(creditSale.getSaleDate());

            payment.setSale(creditSale);
            creditSale.getPayments().add(payment);

            BigDecimal restToPay = totalAmount.subtract(paidAmount);
            creditSale.setRest(restToPay);
            creditSale.setPaid( (restToPay.intValue() > 0) ? false: true );
            creditSale.setBalance(paidAmount);
        } else {
            creditSale.setRest(totalAmount);
            creditSale.setPaid(false);
            creditSale.setBalance(new BigDecimal("0"));
        }


        creditSale.setSaleTxType(SaleTxType.CREDIT);
        creditSale.setCreationDate(new Date());

        Sale createdSale = saleService.createSale(creditSale);
        log.info("createCreditSale() - Created sale '"+createdSale.getReference()+"' with Id '"+createdSale.getId()+"'");

        model.addAttribute("saleCreated", createdSale);

        // clear the shopping cart session
        request.getSession().removeAttribute(EbgSysUtils.SESSION_SHOPPING_CART_PREFIX_SELL);

        loadCreditSales(model);


        return "salescredit";
    }

    /**
     *
     * */
    @GetMapping("/credit/{saleId}")
    public String getCreditSaleDetails(@PathVariable Long saleId, Model model) {

        Sale sale = saleService.fetchSale(saleId);

        setCustomerNameView(sale);

        model.addAttribute("sale", sale);

        // Side menu
        model.addAttribute("sidemenuSales", true);
        model.addAttribute("subSidemenuSalesCredit", true);

        return "salescreditdetails";
    }

    /**
     *
     * */
    @GetMapping("/invoiceprint/{saleId}")
    public String saleInvoicePrint(@PathVariable Long saleId, Model model) {
        // Side menu
        model.addAttribute("sidemenuSales", true);
        model.addAttribute("subSidemenuSalesCredit", true);

        if (saleId != null) {

            Sale sale = saleService.fetchSale(saleId);
            setCustomerNameView(sale);
            model.addAttribute("sale", sale);

        } else {

            return "redirect:/salesintro";
        }

        return "salesinvoice_print";
    }

    /**
     *
     * */
    @GetMapping("/makepayment")
    public String makePayment(HttpServletRequest request, Model model) {
        // TODO - Validate saleId, paymamount (integer)

        String paidAmountStr = request.getParameter("payamount");
        BigDecimal paidAmount = new BigDecimal(paidAmountStr);

        Long saleId = Long.valueOf(request.getParameter("saleId"));
        Sale sale = saleService.fetchSale(saleId);
        BigDecimal currentBalance = sale.getBalance().add(paidAmount);
        sale.setBalance(currentBalance);
        sale.setRest(sale.getAmount().subtract(currentBalance));
        if (currentBalance.compareTo(sale.getAmount()) >= 0 ) {
            // OR - if (rest <= 0) ==> paid=true !!
            sale.setPaid(true);
        }

        Payment payment = new Payment();
        payment.setAmount(paidAmount);
        payment.setPayDate(new Date());
        payment.setSale(sale);
        sale.getPayments().add(payment);

        sale.setModificationDate(new Date());

        saleService.updateSale(sale);
        log.info("makePayment() - Payment has been made with amount: " + paidAmount);

        model.addAttribute("madePayment", payment);

        loadCreditSales(model);

        return "salescredit";
    }

    /**
     *
     * */
    @GetMapping("/add2cart/{productId}")
    public @ResponseBody String addToCart(
            @PathVariable Long productId,
            @RequestParam String cartType,
            @RequestParam String saleType,
            @RequestParam Integer qty,
            HttpServletRequest request) {

        Product product = productService.fetchProduct(productId);
        log.debug("addToCart() - Adding Product '"+product.getName()+"' to Cart");

        List<ShopCartItem> cartItems = (List<ShopCartItem>) request.getSession().getAttribute(EbgSysUtils.SESSION_SHOPPING_CART_PREFIX_SELL);

        if (cartItems == null) {
            cartItems = new ArrayList<ShopCartItem>();
            ShopCartItem item = buildCartItem(product, saleType, qty);
            cartItems.add(item);

            request.getSession().setAttribute(EbgSysUtils.SESSION_SHOPPING_CART_PREFIX_SELL, cartItems);

        }  else {
            ShopCartItem item = buildCartItem(product, saleType, qty);
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
    private ShopCartItem buildCartItem(Product product, String saleType, int quantity) {
        ShopCartItem cartItem = new ShopCartItem();
        cartItem.setProductName(product.getName());
        cartItem.setProductReference(product.getReference());
        cartItem.setSaleType(saleType.equals("RETAIL") ? SaleType.RETAIL : SaleType.WHOLESALE);
        cartItem.setUnitPrice(saleType.equals("RETAIL") ? product.getUnitPrice() : product.getGrossPrice());
        cartItem.setQuantity(quantity);

        BigDecimal totalPrice = cartItem.getUnitPrice().multiply(new BigDecimal(quantity));
        cartItem.setTotalPrice(totalPrice);

        return cartItem;
    }

        ////////////////////////////////////////  END

    private void loadSales(Model model) {

        loadCashSales(model);
        loadCreditSales(model);

        model.addAttribute("sidemenuSales", true);
        model.addAttribute("subSidemenuSalesIntro", true);

    }

    //
    private void loadCashSales(Model model) {

        List<Sale> sales = saleService.fetchAllSales();
        List<Sale> cashSales = new ArrayList<>();

        for (Sale sale: sales) {

            if (sale.getSaleTxType() != null && sale.getSaleTxType().equals(SaleTxType.CASH)) {
                cashSales.add(sale);
            }
        }

        model.addAttribute("cashSales", cashSales);
        model.addAttribute("cashSalesSize", cashSales.size());

        // For the Creation Form
        model.addAttribute("productsForm", productService.fetchAllProducts());
        Sale cashSaleForm = new Sale();
        String nextRef = EbgSysUtils.retrieveNextReference("VTC-EBG-", 7, cashSales.stream().map(s -> s.getReference()).collect(Collectors.toList()));
        cashSaleForm.setNextReferenceView(nextRef);
        model.addAttribute("cashSale", cashSaleForm);

        // Side menu
        model.addAttribute("sidemenuSales", true);
        model.addAttribute("subSidemenuSalesCash", true);

    }

    /**
     *
     * */
    private void loadCreditSales(Model model) {
        List<Sale> sales = saleService.fetchAllSales()
                .stream()
                .filter(sale -> sale.getSaleTxType().equals(SaleTxType.CREDIT))
                .collect(Collectors.toList());

        List<Sale> creditSales = new ArrayList<>();
        for (Sale sale: sales) {
            setCustomerNameView(sale);
            creditSales.add(sale);
        }

        Sale maxCreditSale = creditSales
                .stream()
                .max(Comparator.comparing(Sale::getReference))
                .orElse(new Sale());
        setCustomerNameView(maxCreditSale);

        model.addAttribute("creditSales", creditSales);
        model.addAttribute("maxCreditSale", maxCreditSale);
        model.addAttribute("customers", customerService.fetchAllCustomers());

        model.addAttribute("productsForm", productService.fetchAllProducts());
        Sale creditSale = new Sale();
        String nextRef = EbgSysUtils.retrieveNextReference("VTT-EBG-", 7, creditSales.stream().map(s -> s.getReference()).collect(Collectors.toList()));
        creditSale.setNextReferenceView(nextRef);
        model.addAttribute("creditSale", creditSale);

        // Side menu
        model.addAttribute("sidemenuSales", true);
        model.addAttribute("subSidemenuSalesCredit", true);
    }

    private void setCustomerNameView(Sale sale) {
        Customer customer = sale.getCustomer();
        if (customer != null) {
            if (customer.getCustomerType().equals(CustomerType.PERSON)) {
                sale.setCustomerNameView(customer.getLastName() + ' ' + customer.getFirstName());
            } else {
                sale.setCustomerNameView(customer.getName());
            }
        }
    }

}
