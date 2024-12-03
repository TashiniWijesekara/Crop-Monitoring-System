package org.example.cropmonitoringsystembackend.entity.impl;

import jakarta.persistence.*;
import lombok.*;
import org.example.cropmonitoringsystembackend.entity.SuperEntity;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"field","monitoringLogList"})
@Entity
@Table(name = "crops")
public class Crop implements SuperEntity {
    @Id
    private String cropCode;
    private String cropCommonName;
    private String cropScientificName;
    private String category;
    private String cropSeason;

    @Column(columnDefinition = "LONGTEXT")
    private String cropImage;

    @ManyToOne
    @JoinColumn(name = "field_Code", nullable = false)
    private Field field;

    @OneToMany(mappedBy = "crop")
    private List<MonitoringLog> monitoringLogList = new ArrayList<>();
}
