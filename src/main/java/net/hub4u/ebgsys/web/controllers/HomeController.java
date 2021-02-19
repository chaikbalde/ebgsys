package net.hub4u.ebgsys.web.controllers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.hub4u.ebgsys.entities.Customer;
import net.hub4u.ebgsys.entities.Employee;
import net.hub4u.ebgsys.entities.Product;
import net.hub4u.ebgsys.entities.Sale;
import net.hub4u.ebgsys.entities.SaleTxType;
import net.hub4u.ebgsys.entities.Supplier;
import net.hub4u.ebgsys.entities.Vehicle;
import net.hub4u.ebgsys.services.CustomerService;
import net.hub4u.ebgsys.services.EmployeeService;
import net.hub4u.ebgsys.services.ProductService;
import net.hub4u.ebgsys.services.SaleService;
import net.hub4u.ebgsys.services.SupplierService;
import net.hub4u.ebgsys.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HomeController {

    @Autowired
    ProductService productService;
    @Autowired
    CustomerService customerService;
    @Autowired
    SupplierService supplierService;
    @Autowired
    VehicleService vehicleService;
    @Autowired
    EmployeeService employeeService;

    @Autowired
    SaleService saleService;


    @GetMapping("/settingsintro")
    public String settingsHome(Model model) {

        model.addAttribute("sidemenuSettings", true);
        model.addAttribute("subSidemenuSettingsIntro", true);

        List<Product> products = productService.fetchAllProducts();
        List<Customer> customers = customerService.fetchAllCustomers();
        List<Supplier> suppliers = supplierService.fetchAllSuppliers();
        List<Vehicle> vehicles = vehicleService.fetchAllVehicles();
        List<Employee> employees = employeeService.fetchAllEmployees();

        model.addAttribute("products", (products != null) ? products.size() : 0);
        model.addAttribute("customers", (customers != null) ? customers.size() : 0);
        model.addAttribute("suppliers", (suppliers != null) ? suppliers.size() : 0);
        model.addAttribute("vehicles", (vehicles != null) ? vehicles.size() : 0);
        model.addAttribute("employees", (employees != null) ? employees.size() : 0);

        return "settingsintro";
    }

    @GetMapping("/salesintro")
    public String salesHome(Model model) {

        List<Sale> cashSales = saleService.fetchAllSales()
                .stream()
                .filter(sale -> sale.getSaleTxType().equals(SaleTxType.CASH))
                .collect(Collectors.toList());

        List<Sale> creditSales = saleService.fetchAllSales()
                .stream()
                .filter(sale -> sale.getSaleTxType().equals(SaleTxType.CREDIT))
                .collect(Collectors.toList());


        model.addAttribute("cashSales", cashSales);
        model.addAttribute("creditSales", creditSales);

        model.addAttribute("sidemenuSales", true);
        model.addAttribute("subSidemenuSalesIntro", true);

        return "salesintro";
    }
}
