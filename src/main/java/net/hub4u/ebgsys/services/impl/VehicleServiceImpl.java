package net.hub4u.ebgsys.services.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.hub4u.ebgsys.entities.Vehicle;
import net.hub4u.ebgsys.repositories.VehicleRepository;
import net.hub4u.ebgsys.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    VehicleRepository vehicleRepository;

    @Override
    public Vehicle createVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    @Override
    public List<Vehicle> fetchAllVehicles() {
        Iterable<Vehicle> vehicleIterable = vehicleRepository.findAll();
        List<Vehicle> vehicles = new ArrayList<>();
        vehicleIterable.forEach(vehicles::add);
        return vehicles;
    }

    @Override
    public void deleteVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }
}
