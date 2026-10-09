package com.example.opd.controller;

import com.example.opd.entity.Patient;
import com.example.opd.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {
    private final PatientService service;
    public PatientController(PatientService service) { this.service = service; }

    @GetMapping public List<Patient> list(@RequestParam(required = false) String search) { return service.findAll(search); }
    @GetMapping("/{id}") public Patient get(@PathVariable Long id) { return service.findById(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public Patient create(@Valid @RequestBody Patient patient) { return service.create(patient); }
}
