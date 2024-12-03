package org.example.cropmonitoringsystembackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.cropmonitoringsystembackend.customObj.FieldErrorResponse;
import org.example.cropmonitoringsystembackend.customObj.FieldResponse;
import org.example.cropmonitoringsystembackend.dto.impl.CropDTO;
import org.example.cropmonitoringsystembackend.dto.impl.FieldDTO;
import org.example.cropmonitoringsystembackend.exception.DataPersistException;
import org.example.cropmonitoringsystembackend.exception.FieldNotFoundException;
import org.example.cropmonitoringsystembackend.service.FieldService;
import org.example.cropmonitoringsystembackend.util.AppUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/fields")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class FieldController {
    private final FieldService fieldService;

    @PreAuthorize("hasAnyRole('MANAGER', 'SCIENTIST')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FieldErrorResponse> saveField(@RequestParam("fieldCode") String fieldCode, @RequestParam("fieldName") String fieldName, @RequestParam("fieldLocation") String fieldLocation, @RequestParam("extentSize") Double extentSize, @RequestParam("fieldImage1") MultipartFile fieldImage1, @RequestParam("fieldImage2") MultipartFile fieldImage2, HttpServletRequest request // Add this to log the request content type

    ) {
        System.out.println("Content-Type: " + request.getContentType());
        request.getHeaderNames().asIterator().forEachRemaining(header -> System.out.println(header + ": " + request.getHeader(header)));

        try {
            // Log incoming data
            System.out.println("FieldCode: " + fieldCode);
            System.out.println("FieldName: " + fieldName);
            System.out.println("FieldLocation: " + fieldLocation);
            System.out.println("ExtentSize: " + extentSize);
            System.out.println("FieldImage1: " + fieldImage1.getOriginalFilename());
            System.out.println("FieldImage2: " + fieldImage2.getOriginalFilename());

            // Convert images to Base64
            byte[] byteFieldImage1 = fieldImage1.getBytes();
            String base64Image1 = AppUtil.toBase64(byteFieldImage1);

            byte[] byteFieldImage2 = fieldImage2.getBytes();
            String base64Image2 = AppUtil.toBase64(byteFieldImage2);

            // Build DTO
            FieldDTO fieldDTO = new FieldDTO();
            fieldDTO.setFieldCode(fieldCode);
            fieldDTO.setFieldName(fieldName);
            fieldDTO.setFieldLocation(fieldLocation);
            fieldDTO.setExtentSize(extentSize);
            fieldDTO.setFieldImage1(base64Image1);
            fieldDTO.setFieldImage2(base64Image2);

            // Save field via service
            fieldService.saveField(fieldDTO);

            // Return success response
            return new ResponseEntity<>(new FieldErrorResponse(0, "Field saved successfully"), HttpStatus.CREATED);

        } catch (DataPersistException e) {
            // Handle specific persistence error
            return new ResponseEntity<>(new FieldErrorResponse(0, "Failed to save field: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Handle general exceptions
            e.printStackTrace(); // Log the error
            return new ResponseEntity<>(new FieldErrorResponse(0, "Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "allFields", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<FieldDTO> getAllFields() {
        return fieldService.getAllFields();
    }

    @GetMapping()
    public ResponseEntity<List<FieldDTO>> getSelectedField(@RequestParam("searchTerm") String searchTerm) {
        List<FieldDTO> fields = fieldService.getSelectedField(searchTerm);
        return new ResponseEntity<>(fields, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'SCIENTIST')")
    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, value = "/{fieldCode}")
    public ResponseEntity<Void> updateSelectedField(@PathVariable("fieldCode") String fieldCode, @RequestParam(value = "fieldName", required = false) String fieldName, @RequestParam(value = "fieldLocation", required = false) String fieldLocation, @RequestParam(value = "extentSize", required = false) Double extentSize, @RequestParam(value = "fieldImage1", required = false) MultipartFile fieldImage1, @RequestParam(value = "fieldImage2", required = false) MultipartFile fieldImage2) {
        try {
            FieldDTO fieldDTO = new FieldDTO();

            if (fieldName != null) fieldDTO.setFieldName(fieldName);
            if (fieldLocation != null) fieldDTO.setFieldLocation(fieldLocation);
            if (extentSize != null) fieldDTO.setExtentSize(extentSize);

            if (fieldImage1 != null && !fieldImage1.isEmpty()) {
                fieldDTO.setFieldImage1(AppUtil.toBase64(fieldImage1.getBytes()));
            }
            if (fieldImage2 != null && !fieldImage2.isEmpty()) {
                fieldDTO.setFieldImage2(AppUtil.toBase64(fieldImage2.getBytes()));
            }

            fieldService.updateField(fieldCode, fieldDTO);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'SCIENTIST')")
    @DeleteMapping(value = "/{code}")
    public ResponseEntity<Void> deleteSelectedField(@PathVariable("code") String code) {
        try {
            fieldService.deleteField(code);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (FieldNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
