package org.example.cropmonitoringsystembackend.dto.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.cropmonitoringsystembackend.dto.FieldStaffAssigmentStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FieldStaffAssignmentDTO implements FieldStaffAssigmentStatus {
    private Long id;
    private String fieldCode;
    private String staffId;
    private String assignedRole;
    private String assignmentDate;
}