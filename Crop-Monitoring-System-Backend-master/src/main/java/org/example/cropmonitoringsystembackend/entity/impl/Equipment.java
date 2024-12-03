package org.example.cropmonitoringsystembackend.entity.impl;

import jakarta.persistence.*;
import lombok.*;
import org.example.cropmonitoringsystembackend.entity.SuperEntity;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"field","staff"})
@Entity
@Table(name = "equipments")
public class Equipment implements SuperEntity {
    @Id
    private String equipmentId;
    private String equipmentName;
    private String equipmentType;
    private String equipmentStatus;
    @ManyToOne
    @JoinColumn(name = "field_Code", nullable = false)
    private Field field;
    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private Staff staff;
}
