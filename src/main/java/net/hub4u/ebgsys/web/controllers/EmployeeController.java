package net.hub4u.ebgsys.web.controllers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.hub4u.ebgsys.entities.Employee;
import net.hub4u.ebgsys.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/employees")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @GetMapping("/settings")
    public String getAllEmployees(Model model) {
        loadEmployees(model);
        return "settingsemployees";
    }

    @PostMapping("/create")
    public String createEmployee(Employee employee, Model model) {
        Employee employeeCreated = employeeService.createEmployee(employee);
        log.info("createEmployee() - Created Employee with ID: " + employeeCreated.getId());

        model.addAttribute("employeeCreated", employeeCreated);
        loadEmployees(model);

        return "settingsemployees";
    }

    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id, Model model) {
        employeeService.deleteEmployee(id);
        log.info("deleteEmployee() - Deleted Employee with ID: " + id);

        model.addAttribute("employeeDeleted", id);
        loadEmployees(model);

        return "settingsemployees";
    }


    // HELPERS

    private void loadEmployees(Model model) {
        List<Employee> employees = employeeService.fetchAllEmployees();

        model.addAttribute("employees", employees);
        model.addAttribute("employee", new Employee());

        model.addAttribute("sidemenuSettings", true);
        model.addAttribute("subSidemenuEmployees", true);
    }
}
