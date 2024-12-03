package org.example.cropmonitoringsystembackend.util;

import org.example.cropmonitoringsystembackend.dto.impl.*;
import org.example.cropmonitoringsystembackend.entity.impl.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class Mapping {
    @Autowired
    private ModelMapper modelMapper;

    // Field mappings
    public FieldDTO convertToFieldDTO(Field field) {
        return modelMapper.map(field, FieldDTO.class);
    }

    public Field convertToField(FieldDTO fieldDTO) {
        return modelMapper.map(fieldDTO, Field.class);
    }

    public List<FieldDTO> convertToFieldListDTO(List<Field> fields) {
        return modelMapper.map(fields, new TypeToken<List<FieldDTO>>() {
        }.getType());
    }

    // Vehicle mappings
    public VehicleDTO convertToVehicleDTO(Vehicle vehicle) {
        return modelMapper.map(vehicle, VehicleDTO.class);
    }

    public Vehicle convertToVehicle(VehicleDTO vehicleDTO) {
        return modelMapper.map(vehicleDTO, Vehicle.class);
    }

    public List<VehicleDTO> convertToVehicleListDTO(List<Vehicle> vehicles) {
        return modelMapper.map(vehicles, new TypeToken<List<VehicleDTO>>() {}.getType());
    }

    //Crop mappings
    public CropDTO convertToCropDTO(Crop crop) {
        return modelMapper.map(crop, CropDTO.class);
    }

    public Crop convertToCrop(CropDTO cropDTO) {
        return modelMapper.map(cropDTO, Crop.class);
    }

    public List<CropDTO> convertToCropListDTO(List<Crop> crops) {
        return modelMapper.map(crops, new TypeToken<List<CropDTO>>() {}.getType());
    }

    //Staff mappings
    public StaffDTO convertToStaffDTO(Staff staff) {
        return modelMapper.map(staff, StaffDTO.class);
    }

    public Staff convertToStaff(StaffDTO staffDTO) {
        return modelMapper.map(staffDTO, Staff.class);
    }

    public List<StaffDTO> convertToStaffListDTO(List<Staff> staff) {
        return modelMapper.map(staff, new TypeToken<List<StaffDTO>>() {}.getType());
    }

    //Equipment mapping
    public EquipmentDTO convertToEquipmentDTO(Equipment equipment) {
        return modelMapper.map(equipment, EquipmentDTO.class);
    }

    public Equipment convertToEquipment(EquipmentDTO equipmentDTO) {
        return modelMapper.map(equipmentDTO, Equipment.class);
    }

    public List<EquipmentDTO> convertToEquipmentListDTO(List<Equipment> equipments) {
        return equipments.stream().map(equipment -> {
            EquipmentDTO equipmentDTO = modelMapper.map(equipment, EquipmentDTO.class);
            equipmentDTO.setFieldCode(equipment.getField().getFieldCode());
            equipmentDTO.setId(equipment.getStaff().getId());
            return equipmentDTO;
        }).collect(Collectors.toList());
    }

    //MonitoringLog mappings
    public MonitoringLogDTO convertToMonitoringLogDTO(MonitoringLog monitoringLog) {
        return modelMapper.map(monitoringLog, MonitoringLogDTO.class);
    }

    public MonitoringLog convertToMonitoringLog(MonitoringLogDTO monitoringLogDTO) {
        return modelMapper.map(monitoringLogDTO, MonitoringLog.class);
    }

    public List<MonitoringLogDTO> convertToMonitoringLogListDTO(List<MonitoringLog> monitoringLogs) {
        return monitoringLogs.stream().map(log -> {
            MonitoringLogDTO monitoringLogDTO = modelMapper.map(log, MonitoringLogDTO.class);
            // Manually map nested properties
            monitoringLogDTO.setFieldCode(log.getField().getFieldCode());
            monitoringLogDTO.setCropCode(log.getCrop().getCropCode());
            monitoringLogDTO.setId(log.getStaff().getId());
            return monitoringLogDTO;
        }).collect(Collectors.toList());
    }

    //FieldAssigmentStaff mappings
    public List<FieldStaffAssignmentDTO> convertToFieldStaffAssignmentDTOList(List<FieldStaffAssignment> fieldStaffAssignments) {
        return fieldStaffAssignments.stream()
                .map(assignment -> {
                    FieldStaffAssignmentDTO fieldStaffAssignmentDTO = modelMapper.map(assignment, FieldStaffAssignmentDTO.class);
                    fieldStaffAssignmentDTO.setFieldCode(assignment.getField().getFieldCode());
                    fieldStaffAssignmentDTO.setStaffId(assignment.getStaff().getId().toString());
                    return fieldStaffAssignmentDTO;
                })
                .collect(Collectors.toList());
    }
}