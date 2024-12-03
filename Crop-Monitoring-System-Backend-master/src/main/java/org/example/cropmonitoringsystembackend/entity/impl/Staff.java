package org.example.cropmonitoringsystembackend.entity.impl;

import jakarta.persistence.*;
import lombok.*;
import org.example.cropmonitoringsystembackend.entity.SuperEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"vehicle", "assignments", "equipmentList","monitoringLogList"})
@Entity
@Table(name = "staffs")
public class Staff implements SuperEntity {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String designation;
    private String gender;
    private Date joinedDate;
    private Date dob;
    private String role;
    private String addressLine01;
    private String addressLine02;
    private String addressLine03;
    private String addressLine04;
    private String addressLine05;
    private String contactNo;
    private String email;
    @ManyToOne
    @JoinColumn(name = "vehicle_Code",nullable = true)
    private Vehicle vehicle;
    @OneToMany(mappedBy = "staff", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<FieldStaffAssignment> assignments = new ArrayList<>();
    @OneToMany(mappedBy = "staff")
    private List<Equipment> equipmentList = new ArrayList<>();
    @OneToMany(mappedBy = "staff")
    private List<MonitoringLog> monitoringLogList = new ArrayList<>();
}
