package com.example.opd.service;

import com.example.opd.entity.Patient;
import com.example.opd.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class PatientService {
    private final PatientRepository repository;
    public PatientService(PatientRepository repository) { this.repository = repository; }

    public List<Patient> findAll(String search) {
        return search == null || search.isBlank()
                ? repository.findAll().stream().sorted((a,b) -> b.getId().compareTo(a.getId())).toList()
                : repository.search(search.trim());
    }
    public Patient findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Patient not found: " + id));
    }
    @Transactional
    public Patient create(Patient patient) {
        patient.setId(null);
        return repository.save(patient);
    }
}
