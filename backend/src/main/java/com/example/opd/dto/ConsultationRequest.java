package com.example.opd.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ConsultationRequest {
    @NotBlank @Size(max = 40) private String bloodPressure;
    @NotBlank @Size(max = 40) private String temperature;
    @Size(max = 2000) private String notes;
    public String getBloodPressure() { return bloodPressure; }
    public void setBloodPressure(String bloodPressure) { this.bloodPressure = bloodPressure; }
    public String getTemperature() { return temperature; }
    public void setTemperature(String temperature) { this.temperature = temperature; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
