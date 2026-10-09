# OPD Mini Module — Spring Boot + JPA + MySQL + Angular

A beginner-friendly full-stack project based on the uploaded assignment:
- Patient registration, listing, and search by name/phone
- Appointment booking and today's appointment list
- Consultation notes, two vital fields (blood pressure and temperature), completion status, and patient consultation history
- Sample reports: registered patients, today’s appointments, booked appointments, and completed consultations

## Tech stack
- Backend: Java 17, Spring Boot 3.3.5, Spring Web, Spring Data JPA, Bean Validation
- Database: MySQL 8
- Frontend: Angular 18 standalone components, TypeScript, HTML, CSS

## 1. Install these tools
1. **JDK 17** (or newer compatible LTS): https://adoptium.net/
2. **Node.js LTS and npm**: https://nodejs.org/
3. **Angular CLI** (after installing Node): `npm install -g @angular/cli`
4. **MySQL Community Server**: https://dev.mysql.com/downloads/mysql/
5. **IntelliJ IDEA Community** or **VS Code**: https://code.visualstudio.com/
6. Optional API testing: Postman or use the browser/curl.

Verify installations:
```bash
java -version
mvn -version
node -v
npm -v
ng version
```
If `mvn` is not installed, install Apache Maven or use IntelliJ's Maven support. The project includes `mvnw` only if you add the Maven Wrapper yourself; regular Maven is the simplest setup.

## 2. Create the database
Start MySQL, then open MySQL Workbench or a MySQL terminal and run:
```sql
CREATE DATABASE opd_db;
```
The application is configured for local MySQL user `root` and password `change_me`.
**Change these credentials** in `backend/src/main/resources/application.properties` to match your local MySQL setup. Never publish a real password.

## 3. Start the backend
Open a terminal in `backend/`:
```bash
mvn spring-boot:run
```
Backend base URL: `http://localhost:8080`
Health check: `http://localhost:8080/api/health`

The JPA configuration creates/updates tables automatically for local development. For a real deployment, use database migrations and `ddl-auto=validate`.

## 4. Start the Angular frontend
Open a second terminal in `frontend/`:
```bash
npm install
npm start
```
Open `http://localhost:4200`.

## 5. Test the full flow
1. Add a patient from **Patients**.
2. Search by patient name or phone number in the patient directory.
3. Go to **Appointments**, choose a patient, doctor, date/time, and book.
4. Confirm the appointment appears in **Today’s appointments** when its date is today.
5. Open **Consultation**, select a booked appointment, enter blood pressure and temperature (two vital fields), add short notes, and mark it complete.
6. Select the patient in **Consultation history** to view completed consultations.
7. Open **Reports** to see live sample counts for patients and appointment statuses.

## API overview
| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/health` | Check backend |
| POST | `/api/patients` | Register patient |
| GET | `/api/patients?search=` | List/search patients |
| GET | `/api/patients/{id}` | Patient detail |
| POST | `/api/appointments` | Book appointment |
| GET | `/api/appointments/today` | Today's appointments |
| GET | `/api/appointments` | List all appointments |
| GET | `/api/appointments/{id}` | Appointment detail |
| POST | `/api/appointments/{id}/consultation` | Save consultation and mark complete |
| GET | `/api/patients/{id}/consultations` | Completed consultation history |

## How the code works
- **Angular component** captures form values and calls an API service.
- **Angular service** uses `HttpClient` to send HTTP requests to Spring Boot.
- **Controller** maps an HTTP route to a Java method.
- **Service layer** contains validation and business rules.
- **Repository** uses Spring Data JPA to query MySQL without writing basic SQL manually.
- **Entity** maps a Java class to a database table.
- **MySQL** stores patients, appointments, and consultation summaries.

Typical request:
`Patient form → PatientApiService → POST /api/patients → PatientController → PatientService → PatientRepository → MySQL`.

## Scope notes
- The required patient, appointment, and consultation flows are included, along with an optional sample reports screen.
- Basic login was optional in the assignment and is not implemented in this version; there is no authentication or role-based access control.
- This is an educational demo, not a production clinical system. Audit logging, encryption, backups, and clinical safety review are not implemented.
- Do not enter real patient/medical data.
- Appointment date/time is stored as a local date-time for a simple local demo. A production app should define its timezone policy.
