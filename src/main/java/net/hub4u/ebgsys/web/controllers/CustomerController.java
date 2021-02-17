package net.hub4u.ebgsys.web.controllers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.hub4u.ebgsys.entities.Customer;
import net.hub4u.ebgsys.entities.CustomerType;
import net.hub4u.ebgsys.frwk.EbgSysUtils;
import net.hub4u.ebgsys.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/customers")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @GetMapping("/settings")
    public String getAllCustomers(Model model) {
        loadCustomers(model);
        return "settingscustomers";
    }

    @PostMapping("/create")
    public String createCustomer(@ModelAttribute Customer customer, Model model) {
        customer.setReference(customer.getNextReferenceView());
        customerService.createCustomer(customer);
        log.info("createCustomer() - Created Customer with ID: " + customer.getId());

        model.addAttribute("customerCreated", customer);
        loadCustomers(model);

        return "settingscustomers";
    }

    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable Long id, Model model) {
        customerService.deleteCustomer(id);
        log.info("deleteCustomer() - Removed Customer with ID: " + id);

        model.addAttribute("customerDeleted", id);
        loadCustomers(model);

        return "settingscustomers";
    }

    // HELPERS

    private void loadCustomers(Model model) {
        List<Customer> customers = customerService.fetchAllCustomers();
        List<Customer> persons = customers.stream()
                .filter(c -> c.getCustomerType().equals(CustomerType.PERSON))
                .collect(Collectors.toList());

        List<Customer> companies = customers.stream()
                .filter(c -> c.getCustomerType().equals(CustomerType.COMPANY))
                .collect(Collectors.toList());

        model.addAttribute("persons", persons);
        model.addAttribute("companies", companies);

        Customer person = new Customer();
        person.setCustomerType(CustomerType.PERSON);
        String personNextRef = EbgSysUtils.retrieveNextReference("CLT-EBG-", 7, persons.stream().map(p -> p.getReference()).collect(Collectors.toList()));
        person.setNextReferenceView(personNextRef);
        model.addAttribute("customer", person);

        Customer company = new Customer();
        company.setCustomerType(CustomerType.COMPANY);
        String companyNextRef = EbgSysUtils.retrieveNextReference("SOC-EBG-", 7, companies.stream().map(c -> c.getReference()).collect(Collectors.toList()));
        company.setNextReferenceView(companyNextRef);
        model.addAttribute("company", company);

        model.addAttribute("sidemenuSettings", true);
        model.addAttribute("subSidemenuCustomers", true);
    }


}
