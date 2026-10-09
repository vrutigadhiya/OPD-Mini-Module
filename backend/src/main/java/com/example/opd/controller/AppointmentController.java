package com.example.opd.controller;

import com.example.opd.dto.AppointmentRequest;
import com.example.opd.dto.ConsultationRequest;
import com.example.opd.entity.Appointment;
import com.example.opd.entity.Consultation;
import com.example.opd.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AppointmentController {
    private final AppointmentService service;
    public AppointmentController(AppointmentService service) { this.service = service; }

    @GetMapping("/appointments") public List<Appointment> all() { return service.all(); }
    @GetMapping("/appointments/today") public List<Appointment> today() { return service.today(); }
    @GetMapping("/appointments/{id}") public Appointment get(@PathVariable Long id) { return service.findById(id); }
    @PostMapping("/appointments") @ResponseStatus(HttpStatus.CREATED)
    public Appointment book(@Valid @RequestBody AppointmentRequest request) { return service.book(request); }
    @PostMapping("/appointments/{id}/consultation") @ResponseStatus(HttpStatus.CREATED)
    public Consultation complete(@PathVariable Long id, @Valid @RequestBody ConsultationRequest request) {
        return service.complete(id, request);
    }
    @GetMapping("/patients/{patientId}/consultations")
    public List<Consultation> history(@PathVariable Long patientId) { return service.history(patientId); }
}
