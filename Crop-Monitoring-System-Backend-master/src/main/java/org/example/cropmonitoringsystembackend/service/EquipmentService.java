package org.example.cropmonitoringsystembackend.service;


import org.example.cropmonitoringsystembackend.dto.impl.EquipmentDTO;

import java.util.List;

public interface EquipmentService {
    void saveEquipment(EquipmentDTO equipmentDTO);
    List<EquipmentDTO> getAllEquipment();
    void deleteEquipment(String id);
    void updateEquipment(String id, EquipmentDTO equipmentDTO);
    List<EquipmentDTO> searchEquipment  (String searchTerm);
}
