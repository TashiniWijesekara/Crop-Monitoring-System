package org.example.cropmonitoringsystembackend.service;

import org.example.cropmonitoringsystembackend.dto.impl.CropDTO;

import java.util.List;

public interface CropService {
    void saveCrop(CropDTO cropDTO);
    List<CropDTO> searchCrops(String searchTerm);
    List<CropDTO> getAllCrops();
    void deleteCrop(String cropCode);
    void updateCrop(String cropCode, CropDTO cropDTO);
}
