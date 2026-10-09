package com.example.opd.service;

import com.example.opd.dto.AppointmentRequest;
import com.example.opd.dto.ConsultationRequest;
import com.example.opd.entity.*;
import com.example.opd.repository.AppointmentRepository;
import com.example.opd.repository.ConsultationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {
    private final AppointmentRepository appointments;
    private final ConsultationRepository consultations;
    private final PatientService patients;

    public AppointmentService(AppointmentRepository appointments, ConsultationRepository consultations, PatientService patients) {
        this.appointments = appointments; this.consultations = consultations; this.patients = patients;
    }

    public List<Appointment> all() { return appointments.findAllByOrderByAppointmentDateTimeDesc(); }
    public List<Appointment> today() {
        LocalDate today = LocalDate.now();
        return appointments.findByAppointmentDateTimeGreaterThanEqualAndAppointmentDateTimeLessThanOrderByAppointmentDateTimeAsc(
                today.atStartOfDay(), today.plusDays(1).atStartOfDay());
    }
    public Appointment findById(Long id) {
        return appointments.findById(id).orElseThrow(() -> new IllegalArgumentException("Appointment not found: " + id));
    }
    @Transactional
    public Appointment book(AppointmentRequest request) {
        Appointment appointment = new Appointment();
        appointment.setPatient(patients.findById(request.getPatientId()));
        appointment.setDoctorName(request.getDoctorName().trim());
        appointment.setAppointmentDateTime(request.getAppointmentDateTime());
        appointment.setStatus(AppointmentStatus.BOOKED);
        return appointments.save(appointment);
    }
    @Transactional
    public Consultation complete(Long appointmentId, ConsultationRequest request) {
        Appointment appointment = findById(appointmentId);
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new IllegalArgumentException("This appointment is already completed.");
        }
        Consultation consultation = new Consultation();
        consultation.setAppointment(appointment);
        consultation.setBloodPressure(request.getBloodPressure().trim());
        consultation.setTemperature(request.getTemperature().trim());
        consultation.setNotes(request.getNotes() == null ? "" : request.getNotes().trim());
        consultation.setCompletedAt(LocalDateTime.now());
        Consultation saved = consultations.save(consultation);
        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointments.save(appointment);
        return saved;
    }
    public List<Consultation> history(Long patientId) {
        patients.findById(patientId);
        return consultations.findByAppointmentPatientIdOrderByCompletedAtDesc(patientId);
    }
}
