package org.example.cropmonitoringsystembackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.cropmonitoringsystembackend.dao.FieldDAO;
import org.example.cropmonitoringsystembackend.dao.FieldStaffAssignmentDAO;
import org.example.cropmonitoringsystembackend.dao.StaffDAO;
import org.example.cropmonitoringsystembackend.dto.impl.FieldStaffAssignmentDTO;
import org.example.cropmonitoringsystembackend.entity.impl.Field;
import org.example.cropmonitoringsystembackend.entity.impl.FieldStaffAssignment;
import org.example.cropmonitoringsystembackend.entity.impl.Staff;
import org.example.cropmonitoringsystembackend.service.FieldAssignmentStaffService;
import org.example.cropmonitoringsystembackend.util.Mapping;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FieldAssignmentStaffServiceIMPL implements FieldAssignmentStaffService {
    private final FieldStaffAssignmentDAO fieldStaffAssignmentDAO;
    private final FieldDAO fieldDAO;
    private final StaffDAO staffDAO;
    private final Mapping mapping;

    @Override
    public void saveAssignment(FieldStaffAssignmentDTO fieldStaffAssignmentDTO) {
        FieldStaffAssignment fieldStaffAssignment = new FieldStaffAssignment();
        Field field = fieldDAO.findById(fieldStaffAssignmentDTO.getFieldCode())
                .orElseThrow(() -> new RuntimeException("Field not found"));

        Staff staff = staffDAO.findById(fieldStaffAssignmentDTO.getStaffId())
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        fieldStaffAssignment.setField(field);
        fieldStaffAssignment.setStaff(staff);
        fieldStaffAssignment.setAssignedRole(fieldStaffAssignmentDTO.getAssignedRole());
        fieldStaffAssignment.setAssignmentDate(fieldStaffAssignmentDTO.getAssignmentDate());

        fieldStaffAssignmentDAO.save(fieldStaffAssignment);
    }

    @Override
    public List<FieldStaffAssignmentDTO> getAllFieldStaffAssignments() {
        List<FieldStaffAssignment> logs = fieldStaffAssignmentDAO.findAll();
        return mapping.convertToFieldStaffAssignmentDTOList(logs);
    }
}