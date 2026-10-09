import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Appointment, Consultation, Patient } from './models';

@Injectable({ providedIn: 'root' })
export class OpdApiService {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8080/api';

  getPatients(search = ''): Observable<Patient[]> {
    let params = new HttpParams();
    if (search.trim()) params = params.set('search', search.trim());
    return this.http.get<Patient[]>(`${this.baseUrl}/patients`, { params });
  }
  addPatient(patient: Omit<Patient, 'id'>): Observable<Patient> {
    return this.http.post<Patient>(`${this.baseUrl}/patients`, patient);
  }
  getAppointments(todayOnly = false): Observable<Appointment[]> {
    return this.http.get<Appointment[]>(`${this.baseUrl}/appointments${todayOnly ? '/today' : ''}`);
  }
  bookAppointment(data: { patientId: number; doctorName: string; appointmentDateTime: string }): Observable<Appointment> {
    return this.http.post<Appointment>(`${this.baseUrl}/appointments`, data);
  }
  completeConsultation(appointmentId: number, data: { bloodPressure: string; temperature: string; notes: string }): Observable<Consultation> {
    return this.http.post<Consultation>(`${this.baseUrl}/appointments/${appointmentId}/consultation`, data);
  }
  getHistory(patientId: number): Observable<Consultation[]> {
    return this.http.get<Consultation[]>(`${this.baseUrl}/patients/${patientId}/consultations`);
  }
}
