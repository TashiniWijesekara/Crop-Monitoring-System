package org.example.cropmonitoringsystembackend.customObj;

import java.io.Serializable;

public class CropErrorResponse implements Serializable,CropResponse {
    private int errorCode;
    private String errorMessage;
}
