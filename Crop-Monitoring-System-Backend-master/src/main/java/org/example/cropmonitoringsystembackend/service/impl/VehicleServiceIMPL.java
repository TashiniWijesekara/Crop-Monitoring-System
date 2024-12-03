package org.example.cropmonitoringsystembackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.cropmonitoringsystembackend.dao.VehicleDAO;
import org.example.cropmonitoringsystembackend.dto.impl.VehicleDTO;
import org.example.cropmonitoringsystembackend.entity.impl.Vehicle;
import org.example.cropmonitoringsystembackend.exception.DataPersistException;
import org.example.cropmonitoringsystembackend.exception.VehicleNotFoundException;
import org.example.cropmonitoringsystembackend.service.VehicleService;
import org.example.cropmonitoringsystembackend.util.Mapping;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class VehicleServiceIMPL implements VehicleService {

    private final VehicleDAO vehicleDAO;
    private final Mapping mapping;

    @Override
    public void saveVehicle(VehicleDTO vehicleDTO) {
        Vehicle savedVehicle = vehicleDAO.save(mapping.convertToVehicle(vehicleDTO));
        if (savedVehicle == null) {
            throw new DataPersistException("Can't save vehicle");
        }
    }

    @Override
    public List<VehicleDTO> getAllVehicles() {
        List<Vehicle> allVehicles = vehicleDAO.findAll();
        return mapping.convertToVehicleListDTO(allVehicles);
    }

    @Override
    public List<VehicleDTO> searchVehicles(String searchTerm) {
        List<Vehicle> vehicles = vehicleDAO.findByVehicleCodeOrLicensePlateNumber(searchTerm, searchTerm);
        return mapping.convertToVehicleListDTO(vehicles);
    }

    @Override
    public void deleteVehicle(String vehicleCode) {
        if (!vehicleDAO.existsById(vehicleCode)) {
            throw new VehicleNotFoundException("Vehicle not found with code: " + vehicleCode);
        }
        vehicleDAO.deleteById(vehicleCode);
    }

    @Override
    public void updateVehicle(String vehicleCode, VehicleDTO vehicleDTO) {
        Vehicle existingVehicle = vehicleDAO.findById(vehicleCode)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with code: " + vehicleCode));

        if (vehicleDTO.getLicensePlateNumber() != null) {
            existingVehicle.setLicensePlateNumber(vehicleDTO.getLicensePlateNumber());
        }
        if (vehicleDTO.getVehicleCategory() != null) {
            existingVehicle.setVehicleCategory(vehicleDTO.getVehicleCategory());
        }
        if (vehicleDTO.getFuelType() != null) {
            existingVehicle.setFuelType(vehicleDTO.getFuelType());
        }
        if (vehicleDTO.getStatus() != null) {
            existingVehicle.setStatus(vehicleDTO.getStatus());
        }
        if (vehicleDTO.getRemarks() != null) {
            existingVehicle.setRemarks(vehicleDTO.getRemarks());
        }

        vehicleDAO.save(existingVehicle);
    }
}
