package org.example.cropmonitoringsystembackend.service;

import org.example.cropmonitoringsystembackend.dto.impl.FieldStaffAssignmentDTO;

public interface FieldAssignmentStaffService {
    void saveAssignment(FieldStaffAssignmentDTO fieldStaffAssignmentDTO);
    List<> getAllFieldStaffAssignments();

}
