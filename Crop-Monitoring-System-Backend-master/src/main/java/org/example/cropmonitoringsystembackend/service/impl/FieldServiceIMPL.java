package org.example.cropmonitoringsystembackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.cropmonitoringsystembackend.customObj.FieldErrorResponse;
import org.example.cropmonitoringsystembackend.customObj.FieldResponse;
import org.example.cropmonitoringsystembackend.dao.FieldDAO;
import org.example.cropmonitoringsystembackend.dto.impl.CropDTO;
import org.example.cropmonitoringsystembackend.dto.impl.FieldDTO;
import org.example.cropmonitoringsystembackend.entity.impl.Crop;
import org.example.cropmonitoringsystembackend.entity.impl.Field;
import org.example.cropmonitoringsystembackend.exception.DataPersistException;
import org.example.cropmonitoringsystembackend.exception.FieldNotFoundException;
import org.example.cropmonitoringsystembackend.service.FieldService;
import org.example.cropmonitoringsystembackend.util.Mapping;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FieldServiceIMPL implements FieldService {
    private final FieldDAO fieldDAO;
    private final Mapping mapping;

    @Override
    public void saveField(FieldDTO fieldDTO) {
        Field saveField = fieldDAO.save(mapping.convertToField(fieldDTO));
        try {
            if (saveField == null) {
                throw new DataPersistException("Can't save field");
            }
        } catch (DataPersistException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<FieldDTO> getAllFields() {
        List<Field> getAllFields = fieldDAO.findAll();
        return mapping.convertToFieldListDTO(getAllFields);
    }

    @Override
    public List<FieldDTO> getSelectedField(String searchTerm) {
        List<Field> fields = fieldDAO.findByFieldCodeOrFieldName(searchTerm, searchTerm);
        return mapping.convertToFieldListDTO(fields);
    }

    @Override
    public void deleteField(String fieldCode) {
        Optional<Field> selectedField = fieldDAO.findById(fieldCode);
        if (!selectedField.isPresent()) {
            throw new FieldNotFoundException(fieldCode);
        } else {
            fieldDAO.deleteById(fieldCode);
        }
    }

    @Override
    public void updateField(String fieldCode, FieldDTO fieldDTO) {
        Optional<Field> tmpField = fieldDAO.findById(fieldCode);
        if (!tmpField.isPresent()) {
            throw new FieldNotFoundException("Field not found");
        } else {
            Field existingField = tmpField.get();

            if (fieldDTO.getFieldName() != null) {
                existingField.setFieldName(fieldDTO.getFieldName());
            }
            if (fieldDTO.getFieldLocation() != null) {
                existingField.setFieldLocation(fieldDTO.getFieldLocation());
            }
            if (fieldDTO.getExtentSize() != null) {
                existingField.setExtentSize(fieldDTO.getExtentSize());
            }
            if (fieldDTO.getFieldImage1() != null) {
                existingField.setFieldImage1(fieldDTO.getFieldImage1());
            }
            if (fieldDTO.getFieldImage2() != null) {
                existingField.setFieldImage2(fieldDTO.getFieldImage2());
            }
            fieldDAO.save(existingField);
        }
    }
}
