package com.example.opd.repository;

import com.example.opd.entity.Appointment;
import com.example.opd.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByAppointmentDateTimeGreaterThanEqualAndAppointmentDateTimeLessThanOrderByAppointmentDateTimeAsc(LocalDateTime start, LocalDateTime end);
    List<Appointment> findAllByOrderByAppointmentDateTimeDesc();
    List<Appointment> findByPatientIdAndStatusOrderByAppointmentDateTimeDesc(Long patientId, AppointmentStatus status);
}
