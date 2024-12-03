package org.example.cropmonitoringsystembackend.dto.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.cropmonitoringsystembackend.customObj.FieldResponse;
import org.example.cropmonitoringsystembackend.dto.FieldStatus;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FieldDTO implements FieldStatus, FieldResponse {
    private String fieldCode;
    private String fieldName;
    private String fieldLocation;
    private Double extentSize;
    private String fieldImage1;
    private String fieldImage2;
    private List<CropDTO> crops = new ArrayList<>();
    private List<StaffDTO> staffList = new ArrayList<>();
    private List<EquipmentDTO> equipmentList = new ArrayList<>();
    private List<MonitoringLogDTO> monitoringLogList = new ArrayList<>();
}
