package org.example.cropmonitoringsystembackend.dto.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.cropmonitoringsystembackend.customObj.MonitoringLogResponse;
import org.example.cropmonitoringsystembackend.dto.SuperDTO;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MonitoringLogDTO implements SuperDTO, MonitoringLogResponse {
    private String log_code;
    private String log_date;
    private String Observation;
    private String log_image;
    private String fieldCode;
    private String cropCode;
    private String id;
}
