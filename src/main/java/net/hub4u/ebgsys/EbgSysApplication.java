package net.hub4u.ebgsys;

import net.hub4u.ebgsys.entities.Customer;
import net.hub4u.ebgsys.entities.Employee;
import net.hub4u.ebgsys.entities.Product;
import net.hub4u.ebgsys.entities.Supplier;
import net.hub4u.ebgsys.entities.Vehicle;
import net.hub4u.ebgsys.services.CustomerService;
import net.hub4u.ebgsys.services.EmployeeService;
import net.hub4u.ebgsys.services.ProductService;
import net.hub4u.ebgsys.services.SupplierService;
import net.hub4u.ebgsys.services.VehicleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;

@SpringBootApplication
public class EbgSysApplication {

	public static void main(String[] args) {
		SpringApplication.run(EbgSysApplication.class, args);
	}

	@Bean
	CommandLineRunner init(
			CustomerService customerService,
			EmployeeService employeeService,
			ProductService productService,
			VehicleService vehicleService,
			SupplierService supplierService) {
		return args -> {

			Arrays.asList(
				new Employee("EMP-EBG-001", "Dino", "Kelly", asDate(1974, 2, 18 ),
						"Bonfi Port", "622 58 65 54", "dkelly@ebg.com", "DRH"),
				new Employee("EMP-EBG-002", "Ali", "Jabber", asDate(1969, 4, 23),
						"Dixinn Gare", "622 44 65 33", "ajabber@ebg.com", "Resp. Ventes"),
			new Employee("EMP-EBG-011", "Diallo", "Binta", asDate(1982, 5, 29),
					"Kagbelen", "628 78 95 03", "dbinta@ebg.com", "Comptable")

			).forEach(employeeService::createEmployee);

			Arrays.asList(
				new Customer("Hub4U", "Ref001", "Kouliwondi", "628 55 44 21"),
				new Customer("Ref002", "Chaikou", "Baldé", "Bonfi Port", "628 85 30 21"),
				new Customer("Cellcom", "Ref010", "Port En Ville", "655 55 55 22"),
				new Customer("Rio Tinto", "Ref021", "Bellevue", "622 87 98 88"),
				new Customer("Ref012", "Oumar", "Diallo", "Bellevue", "628 45 66 76"),
				new Customer("Ref014", "Abdourahmane", "Diaby", "Kipé", "624 33 66 90")
			).forEach(customerService::createCustomer);

			Arrays.asList(
				new Product("Clou 21", "VT-EBG-122", "Description Clou 21", new BigDecimal("500"), new BigDecimal("130000"), new BigDecimal("100000")),
				new Product("Arrache clou", "VT-EBG-202", "Description Arrache clou", new BigDecimal("2000"),new BigDecimal("20000"), new BigDecimal("15000") ),
				new Product("Barbelets 10m", "VT-EBG-154", "Description Barbelets 10 mmmm", new BigDecimal("105000"), new BigDecimal("1200000"), new BigDecimal("1000000")),
				new Product("Fer 200", "VT-EBG-123", "Fer 200 pour béton armé", new BigDecimal("15000"), new BigDecimal("120000"), new BigDecimal("100000"))
			).forEach(productService::createProduct);

			Arrays.asList(
				new Vehicle("VHEBG0023", "RC 4553 AB", "Camion", "MERCEDES-BENZ Actros", "Camion de livraison pour la région forestière"),
				new Vehicle("VHEBG0023", "RC 4553 AB", "Camion Poids Lourd", "MERCEDES-BENZ Atego", "Camion de livraison pour Conakry"),
				new Vehicle("VHEBG0023", "RC 4553 AB", "Camion", "Volkswagen Transporter", "Camion de livraison Labé")
			).forEach(vehicleService::createVehicle); ;

			Arrays.asList(
				new Supplier("TAFAGUI", "FR-EBG-0015", "Diallo Mamadou Aliou", "Km 36", "628 55 44 11", "info@tafagui.com", "www.tafagui.gn"),
				new Supplier("EJICO", "FR-EBG-0017", "Bah Ibrahima", "Cimenterie", "622 55 33 22", "contact@ejico.com", "www.ejico.gn"),
				new Supplier("Ets Mamadou Bhoye et Frère", "FR-EBG-0023", "Barry Mamadou Oury", "Kagbele", "623 56 35 11", "barry23@gmail.com", ""),
				new Supplier("Walidian Quincailleries", "FR-EBG-0021", "Diallo Boubacar", "Sanoya", "622 67 67 67", "walidian@gmail.com", "www.walidian.com")
			).forEach(supplierService::createSupplier);
		};
	}

	//
	static Date asDate(int year, int month, int dayOfMonth) {
		return  Date.from(LocalDate.of(year, month, dayOfMonth).atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
}
