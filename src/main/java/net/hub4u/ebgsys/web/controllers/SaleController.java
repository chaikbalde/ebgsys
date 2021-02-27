package net.hub4u.ebgsys.web.controllers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.hub4u.ebgsys.entities.Customer;
import net.hub4u.ebgsys.entities.CustomerType;
import net.hub4u.ebgsys.entities.Product;
import net.hub4u.ebgsys.entities.Sale;
import net.hub4u.ebgsys.entities.SaleTxType;
import net.hub4u.ebgsys.entities.SaleType;
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

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
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

        cashSale.setReference(cashSale.getNextReferenceView());

        Date saleDt = cashSale.getSaleDate();

        if (cashSale.getProduct() == null) {
            log.error("createCashSale() - Failed creating Sale. Product is CANNOT BE NULL !");
            loadCashSales(model);
            return "salescash";
        }

        long prodId = cashSale.getProduct().getId();
        Product product = productService.fetchProduct(prodId);
        product.getSales().add(cashSale);
        cashSale.setProduct(product);

        BigDecimal unitPrice = retrieveUnitPrice(cashSale, product);

        BigDecimal totalPrice = unitPrice.multiply(new BigDecimal(cashSale.getQuantity()));
        cashSale.setAmount(totalPrice);

        cashSale.setUnitPriceView(unitPrice);

        cashSale.setSaleTxType(SaleTxType.CASH);
        Sale createdSale = saleService.createSale(cashSale);
        log.info("createCashSale() - Created sale '"+createdSale.getReference()+"' with Id '"+createdSale.getId()+"'");

        loadCashSales(model);

        return "salescash";
    }

    ////////////////////////////////////////  START

    @PostMapping("/createcreditsale")
    public String createCreditSale(@ModelAttribute Sale creditSale,  BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            log.error("createCreditSale() -  Failed processing form. Errors: " + bindingResult.getAllErrors());
        }

        creditSale.setReference(creditSale.getNextReferenceView());

        if (creditSale.getProduct() == null) {
            log.error("createCreditSale() - Failed creating Sale. Product CANNOT BE NULL !");
            loadCreditSales(model);
            return "salescredit";
        }

        long prodId = creditSale.getProduct().getId();
        Product product = productService.fetchProduct(prodId);
        product.getSales().add(creditSale);
        creditSale.setProduct(product);

        BigDecimal unitPrice = retrieveUnitPrice(creditSale, product);
        BigDecimal totalPrice = unitPrice.multiply(new BigDecimal(creditSale.getQuantity()));
        creditSale.setAmount(totalPrice);
        creditSale.setUnitPriceView(unitPrice);

        BigDecimal payment = creditSale.getPayment();
        if (payment != null) {
            BigDecimal restToPay = totalPrice.subtract(payment);
            creditSale.setRest(restToPay);
            creditSale.setPaid( (restToPay.intValue() > 0) ? false: true );
            creditSale.setBalance(payment);
        } else {
            creditSale.setRest(totalPrice);
            creditSale.setPaid(false);
            creditSale.setBalance(new BigDecimal("0"));
        }

        if (creditSale.getCustomer() == null) {
            log.error("createCreditSale() - Failed creating Sale. Customer CANNOT BE NULL !");
            loadCreditSales(model);
            return "salescredit";
        }

        long customerId = creditSale.getCustomer().getId();
        Customer customer = customerService.fetchCustomer(customerId);
        customer.getSales().add(creditSale);
        creditSale.setCustomer(customer);

        creditSale.setSaleTxType(SaleTxType.CREDIT);
        creditSale.setCreationDate(new Date());

        Sale createdSale = saleService.createSale(creditSale);
        log.info("createCreditSale() - Created sale '"+createdSale.getReference()+"' with Id '"+createdSale.getId()+"'");

        model.addAttribute("saleCreated", createdSale);

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
        setSaleTypeView(sale);

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
            setSaleTypeView(sale);

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

        String payAmountStr = request.getParameter("payamount");
        BigDecimal payAmount = new BigDecimal(payAmountStr);

        Long saleId = Long.valueOf(request.getParameter("saleId"));
        Sale sale = saleService.fetchSale(saleId);
        BigDecimal currentBalance = sale.getBalance().add(payAmount);
        sale.setBalance(currentBalance);
        sale.setRest(sale.getAmount().subtract(currentBalance));
        if (currentBalance.compareTo(sale.getAmount()) >= 0 ) {

            // OR - if (rest <= 0) ==> paid=true !!
            sale.setPaid(true);
        }

        sale.setModificationDate(new Date());

        Sale updatedSale = saleService.updateSale(sale);
        log.info("makePayment() - Payment has been made with amount: " + payAmount);

        loadCreditSales(model);
        return "salescredit";
    }


    ////////////////////////////////////////  END

    //
    private BigDecimal retrieveUnitPrice(Sale sale, Product product) {
        BigDecimal unitPrice = BigDecimal.ZERO;
        if (sale.getSaleType() != null && sale.getSaleType().equals(SaleType.RETAIL)) {
            unitPrice = product.getUnitPrice();
        } else  if (sale.getSaleType() != null && sale.getSaleType().equals(SaleType.WHOLESALE)) {
            unitPrice = product.getGrossPrice();
        }

        return unitPrice;
    }

    //
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

            setSaleTypeView(sale);

            if (sale.getSaleTxType() != null && sale.getSaleTxType().equals(SaleTxType.CASH)) {
                cashSales.add(sale);
            }
        }

        model.addAttribute("cashSales", cashSales);
        model.addAttribute("cashSalesSize", cashSales.size());

        // For the Creation Form
        model.addAttribute("productsForm", productService.fetchAllProducts());
        Sale cashSaleForm = new Sale();
        cashSaleForm.setSaleType(SaleType.RETAIL);
        String nextRef = EbgSysUtils.retrieveNextReference("VTC-EBG-", 7, cashSales.stream().map(s -> s.getReference()).collect(Collectors.toList()));
        cashSaleForm.setNextReferenceView(nextRef);
        model.addAttribute("cashSale", cashSaleForm);

        // Side menu
        model.addAttribute("sidemenuSales", true);
        model.addAttribute("subSidemenuSalesCash", true);

    }

    private void loadCreditSales(Model model) {
        List<Sale> sales = saleService.fetchAllSales()
                .stream()
                .filter(sale -> sale.getSaleTxType().equals(SaleTxType.CREDIT))
                .collect(Collectors.toList());

        List<Sale> creditSales = new ArrayList<>();
        for (Sale sale: sales) {
            setCustomerNameView(sale);
            setSaleTypeView(sale);
            creditSales.add(sale);
        }

        model.addAttribute("creditSales", creditSales);
        model.addAttribute("customers", customerService.fetchAllCustomers());

        model.addAttribute("productsForm", productService.fetchAllProducts());
        Sale creditSale = new Sale();
        creditSale.setSaleType(SaleType.RETAIL);
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

    private void setSaleTypeView(Sale sale) {
        if (sale.getSaleType() != null && sale.getSaleType().equals(SaleType.RETAIL)) {
            sale.setSaleTypeView(SALE_TYPE_RETAIL);

        } else if (sale.getSaleType() != null && sale.getSaleType().equals(SaleType.WHOLESALE)) {
            sale.setSaleTypeView(SALE_TYPE_WHOLESALE);
        }

    }

}
