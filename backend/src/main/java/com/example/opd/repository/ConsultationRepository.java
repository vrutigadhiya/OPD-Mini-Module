package com.example.opd.repository;

import com.example.opd.entity.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    List<Consultation> findByAppointmentPatientIdOrderByCompletedAtDesc(Long patientId);
}
