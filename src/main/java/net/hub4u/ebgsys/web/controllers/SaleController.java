package net.hub4u.ebgsys.web.controllers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.hub4u.ebgsys.entities.Product;
import net.hub4u.ebgsys.entities.Sale;
import net.hub4u.ebgsys.entities.SaleTxType;
import net.hub4u.ebgsys.entities.SaleType;
import net.hub4u.ebgsys.frwk.EbgSysUtils;
import net.hub4u.ebgsys.services.ProductService;
import net.hub4u.ebgsys.services.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.LifecycleAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
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

    @Autowired SaleService saleService;
    @Autowired ProductService productService;

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

//        Date saleDt = creditSale.getSaleDate();

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

        BigDecimal restToPay = totalPrice.subtract(creditSale.getPayment());
        creditSale.setRest(restToPay);

        creditSale.setPaid( (restToPay.intValue() > 0) ? false: true );

        creditSale.setSaleTxType(SaleTxType.CREDIT);

        Sale createdSale = saleService.createSale(creditSale);
        log.info("createCreditSale() - Created sale '"+createdSale.getReference()+"' with Id '"+createdSale.getId()+"'");

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

            if (sale.getSaleType() != null && sale.getSaleType().equals(SaleType.RETAIL)) {
                sale.setSaleTypeView(SALE_TYPE_RETAIL);

            } else if (sale.getSaleType() != null && sale.getSaleType().equals(SaleType.WHOLESALE)) {
                sale.setSaleTypeView(SALE_TYPE_WHOLESALE);
            }


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
        List<Sale> creditSales = saleService.fetchAllSales()
                .stream()
                .filter(sale -> sale.getSaleTxType().equals(SaleTxType.CREDIT))
                .collect(Collectors.toList());

        model.addAttribute("creditSales", creditSales);
        model.addAttribute("creditSalesSize", creditSales.size());

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

    //
    String retrieveNextReference(String prefix, List<String> references) {

        List<Integer> refDigits = references.stream()
                .map(ref -> Integer.valueOf(ref.substring(ref.lastIndexOf("-")+1, ref.length())) )
                .collect(Collectors.toList());

        int nextInt = 0;
        if (refDigits != null && !refDigits.isEmpty()) {
            nextInt = Collections.max(refDigits);
        }
        nextInt++;

        return prefix + nextInt;
    }

}
