package com.example.medismart.server;

import java.util.List;

public class ApiResponse {
    private List<String> detected_classes;

    public List<String> getDetected_classes() {
        return detected_classes;
    }

    public void setDetected_classes(List<String> detected_classes) {
        this.detected_classes = detected_classes;
    }
}
