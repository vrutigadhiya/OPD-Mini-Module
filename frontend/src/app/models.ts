export interface Patient {
  id: number;
  name: string;
  gender: string;
  age: number;
  phone: string;
}
export interface Appointment {
  id: number;
  patient: Patient;
  doctorName: string;
  appointmentDateTime: string;
  status: 'BOOKED' | 'COMPLETED';
}
export interface Consultation {
  id: number;
  appointment: Appointment;
  bloodPressure: string;
  temperature: string;
  notes: string;
  completedAt: string;
}
