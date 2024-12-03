package org.example.cropmonitoringsystembackend.dao;

import org.example.cropmonitoringsystembackend.entity.impl.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface VehicleDAO extends JpaRepository<Vehicle, String> {
    List<Vehicle> findByVehicleCodeOrLicensePlateNumber(String vehicleCode, String licensePlateNumber);
}
