package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Clinician {

    // Attributes based on your CSV columns
    private String clinicianId;
    private String firstName;
    private String lastName;
    private String title;
    private String speciality;
    private String gmcNumber;
    private String phoneNumber;
    private String email;
    private String workplaceId;
    private String workplaceType;
    private String employmentStatus;
    private LocalDate startDate;  // Use LocalDate for date handling

    // --- Constructors ---
    public Clinician() {}

    public Clinician(String clinicianId, String firstName, String lastName, String title,
                     String speciality, String gmcNumber, String phoneNumber, String email,
                     String workplaceId, String workplaceType, String employmentStatus,
                     LocalDate startDate) {
        this.clinicianId = clinicianId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
        this.speciality = speciality;
        this.gmcNumber = gmcNumber;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.workplaceId = workplaceId;
        this.workplaceType = workplaceType;
        this.employmentStatus = employmentStatus;
        this.startDate = startDate;
    }

    // --- Getters and Setters ---
    public String getClinicianId() { return clinicianId; }
    public void setClinicianId(String clinicianId) { this.clinicianId = clinicianId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSpeciality() { return speciality; }
    public void setSpeciality(String speciality) { this.speciality = speciality; }

    public String getGmcNumber() { return gmcNumber; }
    public void setGmcNumber(String gmcNumber) { this.gmcNumber = gmcNumber; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getWorkplaceId() { return workplaceId; }
    public void setWorkplaceId(String workplaceId) { this.workplaceId = workplaceId; }

    public String getWorkplaceType() { return workplaceType; }
    public void setWorkplaceType(String workplaceType) { this.workplaceType = workplaceType; }

    public String getEmploymentStatus() { return employmentStatus; }
    public void setEmploymentStatus(String employmentStatus) { this.employmentStatus = employmentStatus; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public String getStartDateFormatted() {
        if (startDate == null) return "";
        return startDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    @Override
    public String toString() {
        return clinicianId + " - " + firstName + " " + lastName + " (" + speciality + ")";
    }

    // =================================================
    // CSV Conversion
    // =================================================
    public static Clinician fromCSVRow(String[] row) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate start = (row.length > 11 && !row[11].isEmpty()) ? LocalDate.parse(row[11], fmt) : null;
        return new Clinician(
                row.length > 0 ? row[0] : "",
                row.length > 1 ? row[1] : "",
                row.length > 2 ? row[2] : "",
                row.length > 3 ? row[3] : "",
                row.length > 4 ? row[4] : "",
                row.length > 5 ? row[5] : "",
                row.length > 6 ? row[6] : "",
                row.length > 7 ? row[7] : "",
                row.length > 8 ? row[8] : "",
                row.length > 9 ? row[9] : "",
                row.length > 10 ? row[10] : "",
                start
        );
    }

    public String[] toCSVRow() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return new String[] {
                clinicianId,
                firstName,
                lastName,
                title,
                speciality,
                gmcNumber,
                phoneNumber,
                email,
                workplaceId,
                workplaceType,
                employmentStatus,
                startDate != null ? startDate.format(fmt) : ""
        };
    }
}
