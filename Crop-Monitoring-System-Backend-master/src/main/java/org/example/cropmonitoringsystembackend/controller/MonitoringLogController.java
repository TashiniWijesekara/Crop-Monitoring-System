package org.example.cropmonitoringsystembackend.controller;

import lombok.RequiredArgsConstructor;
import org.example.cropmonitoringsystembackend.customObj.FieldErrorResponse;
import org.example.cropmonitoringsystembackend.dto.impl.MonitoringLogDTO;
import org.example.cropmonitoringsystembackend.exception.DataPersistException;
import org.example.cropmonitoringsystembackend.exception.MonitoringLogNotFoundException;
import org.example.cropmonitoringsystembackend.service.MonitoringLogService;
import org.example.cropmonitoringsystembackend.util.AppUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/monitoringLog")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class MonitoringLogController {
    private final MonitoringLogService monitoringLogService;

    @PreAuthorize("hasAnyRole('MANAGER', 'SCIENTIST')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveMonitoringLog(
            @RequestParam("logCode") String logCode,
            @RequestParam("logDate") String logDate,
            @RequestParam("observation") String observation,
            @RequestParam("logImage") MultipartFile logImage,
            @RequestParam("fieldCode") String fieldCode,
            @RequestParam("cropCode") String cropCode,
            @RequestParam("staffId") String id
    ) {
        try {
            String base64Image = AppUtil.toBase64(logImage.getBytes());

            MonitoringLogDTO logDTO = new MonitoringLogDTO();
            logDTO.setLog_code(logCode);
            logDTO.setLog_date(logDate);
            logDTO.setObservation(observation);
            logDTO.setLog_image(base64Image);
            logDTO.setFieldCode(fieldCode);
            logDTO.setCropCode(cropCode);
            logDTO.setId(id);

            monitoringLogService.saveMonitoringLog(logDTO);

            return new ResponseEntity<>(new FieldErrorResponse(0, "Monitoring Log saved successfully"), HttpStatus.CREATED);

        } catch (DataPersistException e) {
            return new ResponseEntity<>(new FieldErrorResponse(0, "Can't save: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new FieldErrorResponse(0, "Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "allLogs", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MonitoringLogDTO> getAllMonitoringLogs() {
        return monitoringLogService.getAllMonitoringLog();
    }

    @GetMapping()
    public ResponseEntity<List<MonitoringLogDTO>> searchMonitoringLogs(@RequestParam("searchTerm") String searchTerm) {
        List<MonitoringLogDTO> logs = monitoringLogService.searchMonitoringLog(searchTerm);
        return new ResponseEntity<>(logs, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'SCIENTIST')")
    @PatchMapping(value = "/{logCode}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateMonitoringLog(
            @PathVariable("logCode") String logCode,
            @RequestParam(value = "logDate", required = false) String logDate,
            @RequestParam(value = "observation", required = false) String observation,
            @RequestParam(value = "logImage", required = false) MultipartFile logImage,
            @RequestParam(value = "fieldCode", required = false) String fieldCode,
            @RequestParam(value = "cropCode", required = false) String cropCode,
            @RequestParam(value = "staffId", required = false) String id
    ) {
        try {
            MonitoringLogDTO logDTO = new MonitoringLogDTO();

            if (logDate != null) logDTO.setLog_date(logDate);
            if (observation != null) logDTO.setObservation(observation);
            if (logImage != null && !logImage.isEmpty()) {
                logDTO.setLog_image(AppUtil.toBase64(logImage.getBytes()));
            }
            if (fieldCode != null) logDTO.setFieldCode(fieldCode);
            if (cropCode != null) logDTO.setCropCode(cropCode);
            if (id != null) logDTO.setId(id);

            monitoringLogService.updateMonitoringLog(logCode, logDTO);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'SCIENTIST')")
    @DeleteMapping(value = "/{logCode}")
    public ResponseEntity<Void> deleteMonitoringLog(@PathVariable("logCode") String logCode) {
        try {
            monitoringLogService.deleteMonitoringLog(logCode);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (MonitoringLogNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
