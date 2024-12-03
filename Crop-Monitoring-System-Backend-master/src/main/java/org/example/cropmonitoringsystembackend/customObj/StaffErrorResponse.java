package org.example.cropmonitoringsystembackend.customObj;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StaffErrorResponse implements Serializable,StaffResponse {
    private int errorCode;
    private String errorMessage;
}
