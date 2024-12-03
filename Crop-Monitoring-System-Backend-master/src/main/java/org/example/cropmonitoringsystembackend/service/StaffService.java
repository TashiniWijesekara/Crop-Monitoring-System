package org.example.cropmonitoringsystembackend.service;

import org.example.cropmonitoringsystembackend.dto.impl.StaffDTO;

import java.util.List;

public interface StaffService {
    void saveStaff(StaffDTO staffDTO);
    List<StaffDTO> getAllStaffs();
    void deleteStaff(String id);
    void updateStaff(String id, StaffDTO staffDTO);
    List<StaffDTO> searchStaff  (String searchTerm);
    void returnVehicle(String staffId);
}
