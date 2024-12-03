package org.example.cropmonitoringsystembackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.cropmonitoringsystembackend.customObj.FieldErrorResponse;
import org.example.cropmonitoringsystembackend.dto.impl.CropDTO;
import org.example.cropmonitoringsystembackend.exception.CropNotFoundException;
import org.example.cropmonitoringsystembackend.exception.DataPersistException;
import org.example.cropmonitoringsystembackend.service.CropService;
import org.example.cropmonitoringsystembackend.util.AppUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/crops")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class CropController {

    private final CropService cropService;
    @PreAuthorize("hasAnyRole('MANAGER', 'SCIENTIST')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveCrop(
            @RequestParam("cropCode") String cropCode,
            @RequestParam("cropCommonName") String cropCommonName,
            @RequestParam("cropScientificName") String cropScientificName,
            @RequestParam("category") String category,
            @RequestParam("cropSeason") String cropSeason,
            @RequestParam("cropImage") MultipartFile cropImage,
            @RequestParam("fieldCode") String fieldCode,
            HttpServletRequest request

    ) {
        System.out.println("Content-Type: " + request.getContentType());
        request.getHeaderNames().asIterator().forEachRemaining(header ->
                System.out.println(header + ": " + request.getHeader(header)));

        try {
            byte[] byteFieldImage1 = cropImage.getBytes();
            String base64Image = AppUtil.toBase64(byteFieldImage1);

            CropDTO cropDTO = new CropDTO();
            cropDTO.setCropCode(cropCode);
            cropDTO.setCropCommonName(cropCommonName);
            cropDTO.setCropScientificName(cropScientificName);
            cropDTO.setCategory(category);
            cropDTO.setCropSeason(cropSeason);
            cropDTO.setCropImage(base64Image);
            cropDTO.setFieldCode(fieldCode);

            cropService.saveCrop(cropDTO);

            return new ResponseEntity<>(new FieldErrorResponse(0,
                    "Crop saved successfully"), HttpStatus.CREATED);

        } catch (DataPersistException e) {
            return new ResponseEntity<>(new FieldErrorResponse(0,
                    "Can't save: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new FieldErrorResponse(0,
                    "Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "allcrops", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CropDTO> getAllCrops() {
        return cropService.getAllCrops();
    }

    @GetMapping()
    public ResponseEntity<List<CropDTO>> searchCrops(@RequestParam("searchTerm") String searchTerm) {
        List<CropDTO> crops = cropService.searchCrops(searchTerm);
        return new ResponseEntity<>(crops, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'SCIENTIST')")
    @DeleteMapping(value = "/{code}")
    public ResponseEntity<Void> deleteSelectedCrop(@PathVariable("code") String code) {
        try {
            cropService.deleteCrop(code);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (CropNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'SCIENTIST')")
    @PatchMapping(value = "/{cropCode}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateSelectedCrop(
            @PathVariable("cropCode") String cropCode,
            @RequestParam(value = "cropCommonName", required = false) String cropCommonName,
            @RequestParam(value = "cropScientificName", required = false) String cropScientificName,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "cropSeason", required = false) String cropSeason,
            @RequestParam(value = "cropImage", required = false) MultipartFile cropImage,
            @RequestParam(value = "fieldCode", required = false) String fieldCode
    ) {
        try {
            CropDTO cropDTO = new CropDTO();

            if (cropCommonName != null) cropDTO.setCropCommonName(cropCommonName);
            if (cropScientificName != null) cropDTO.setCropScientificName(cropScientificName);
            if (category != null) cropDTO.setCategory(category);
            if (cropSeason != null) cropDTO.setCropSeason(cropSeason);

            if (cropImage != null && !cropImage.isEmpty()) {
                cropDTO.setCropImage(AppUtil.toBase64(cropImage.getBytes()));
            }

            if (fieldCode != null) cropDTO.setFieldCode(fieldCode);

            cropService.updateCrop(cropCode, cropDTO);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
