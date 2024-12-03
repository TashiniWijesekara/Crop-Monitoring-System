package org.example.cropmonitoringsystembackend.util;

import java.util.Base64;

public class AppUtil {
    public static String toBase64(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }
}
