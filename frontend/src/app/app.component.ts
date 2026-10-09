import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { OpdApiService } from './opd-api.service';
import { Appointment, Consultation, Patient } from './models';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.component.html'
})
export class AppComponent implements OnInit {
  private api = inject(OpdApiService);
  activeTab: 'patients' | 'appointments' | 'consultation' | 'reports' = 'patients';
  patients: Patient[] = [];
  appointments: Appointment[] = [];
  todayAppointments: Appointment[] = [];
  history: Consultation[] = [];
  search = '';
  historyPatientId = '';
  selectedAppointmentId = '';
  message = '';
  error = '';
  loading = false;

  patientForm = { name: '', gender: 'Female', age: 18, phone: '' };
  appointmentForm = { patientId: '', doctorName: '', appointmentDateTime: '' };
  consultationForm = { bloodPressure: '', temperature: '', notes: '' };

  ngOnInit(): void {
    this.loadPatients();
    this.loadAppointments();
  }

  setTab(tab: 'patients' | 'appointments' | 'consultation' | 'reports'): void {
    this.activeTab = tab;
    this.clearMessages();
    if (tab === 'patients') this.loadPatients();
    if (tab === 'appointments') this.loadAppointments();
    if (tab === 'consultation' || tab === 'reports') { this.loadAppointments(); this.loadPatients(); }
  }

  clearMessages(): void { this.message = ''; this.error = ''; }
  showError(err: unknown): void {
    this.loading = false;
    this.error = err instanceof HttpErrorResponse
      ? (err.error?.message || `Request failed (${err.status}). Is the backend running?`)
      : 'Something went wrong.';
  }

  loadPatients(): void {
    this.api.getPatients(this.search).subscribe({
      next: data => this.patients = data,
      error: err => this.showError(err)
    });
  }
  submitPatient(): void {
    this.clearMessages();
    if (!this.patientForm.name.trim() || !this.patientForm.phone.trim()) {
      this.error = 'Please enter the patient name and phone number.'; return;
    }
    this.loading = true;
    this.api.addPatient({ ...this.patientForm, name: this.patientForm.name.trim(), phone: this.patientForm.phone.trim() }).subscribe({
      next: patient => {
        this.loading = false; this.message = `Patient ${patient.name} registered successfully.`;
        this.patientForm = { name: '', gender: 'Female', age: 18, phone: '' };
        this.loadPatients();
      },
      error: err => this.showError(err)
    });
  }

  loadAppointments(): void {
    this.api.getAppointments().subscribe({ next: data => this.appointments = data, error: err => this.showError(err) });
    this.api.getAppointments(true).subscribe({ next: data => this.todayAppointments = data, error: err => this.showError(err) });
  }
  bookAppointment(): void {
    this.clearMessages();
    if (!this.appointmentForm.patientId || !this.appointmentForm.doctorName.trim() || !this.appointmentForm.appointmentDateTime) {
      this.error = 'Choose a patient, enter the doctor, and select a date/time.'; return;
    }
    this.loading = true;
    this.api.bookAppointment({
      patientId: Number(this.appointmentForm.patientId),
      doctorName: this.appointmentForm.doctorName.trim(),
      appointmentDateTime: this.appointmentForm.appointmentDateTime
    }).subscribe({
      next: appointment => {
        this.loading = false; this.message = `Appointment #${appointment.id} booked.`;
        this.appointmentForm = { patientId: '', doctorName: '', appointmentDateTime: '' };
        this.loadAppointments();
      }, error: err => this.showError(err)
    });
  }

  completeConsultation(): void {
    this.clearMessages();
    if (!this.selectedAppointmentId || !this.consultationForm.bloodPressure.trim() || !this.consultationForm.temperature.trim()) {
      this.error = 'Select an appointment and fill in both vital fields.'; return;
    }
    this.loading = true;
    this.api.completeConsultation(Number(this.selectedAppointmentId), this.consultationForm).subscribe({
      next: () => {
        this.loading = false; this.message = 'Consultation saved and appointment marked complete.';
        this.consultationForm = { bloodPressure: '', temperature: '', notes: '' };
        this.selectedAppointmentId = '';
        this.loadAppointments();
        if (this.historyPatientId) this.loadHistory();
      }, error: err => this.showError(err)
    });
  }
  loadHistory(): void {
    this.clearMessages();
    if (!this.historyPatientId) { this.error = 'Choose a patient to view history.'; return; }
    this.api.getHistory(Number(this.historyPatientId)).subscribe({
      next: data => this.history = data,
      error: err => this.showError(err)
    });
  }
  get bookableAppointments(): Appointment[] { return this.appointments.filter(a => a.status === 'BOOKED'); }
  get completedAppointmentsCount(): number { return this.appointments.filter(a => a.status === 'COMPLETED').length; }
  get bookedAppointmentsCount(): number { return this.appointments.filter(a => a.status === 'BOOKED').length; }
  patientName(id: number): string { return this.patients.find(p => p.id === id)?.name || `Patient #${id}`; }
}
