package net.hub4u.ebgsys.services;

import net.hub4u.ebgsys.entities.Employee;

import java.util.List;

public interface EmployeeService {

    Employee createEmployee(Employee employee);

    List<Employee> fetchAllEmployees();

    void deleteEmployee(Long id);
}
