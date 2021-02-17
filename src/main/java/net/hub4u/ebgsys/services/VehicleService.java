package net.hub4u.ebgsys.services;

import net.hub4u.ebgsys.entities.Vehicle;

import java.util.List;

public interface VehicleService {

    Vehicle createVehicle(Vehicle vehicle);

    List<Vehicle> fetchAllVehicles();

    void deleteVehicle(Long id);
}
