package org.example.cropmonitoringsystembackend.service;

import org.example.cropmonitoringsystembackend.dto.impl.FieldStaffAssignmentDTO;

import java.util.List;

public interface FieldAssignmentStaffService {
    void saveAssignment(FieldStaffAssignmentDTO fieldStaffAssignmentDTO);
    List<FieldStaffAssignmentDTO> getAllFieldStaffAssignments();

}
