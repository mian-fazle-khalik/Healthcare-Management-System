package model;

public class Appointment {
	private String appointmentId;     // Unique identifier for the appointment
    private String patientId;         // ID of the patient associated with the appointment
    private String clinicianId;       // ID of the clinician handling the appointment
    private String facilityId;        // ID of the healthcare facility where the appointment is scheduled
    private String appointmentDate;   // Date of the appointment (e.g., "2025-11-07")
    private String appointmentTime;   // Time of the appointment (e.g., "09:30 AM")
    private String durationMinutes;   // Duration of the appointment in minutes
    private String appointmentType;   // Type of appointment (e.g., "Consultation", "Follow-up")
    private String status;            // Current status (e.g., "Scheduled", "Completed", "Cancelled")
    private String reasonForVisit;    // Reason or purpose of the visit
    private String notes;             // Additional notes entered by the clinician
    private String createdDate;       // Date the record was created
    private String lastModified;      // Date the record was last modified


//    Constructor
    public Appointment(String appointmentId, String patientId, String clinicianId, String facilityId,
                       String appointmentDate, String appointmentTime, String durationMinutes, String appointmentType,
                       String status, String reasonForVisit, String notes, String createdDate, String lastModified) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.clinicianId = clinicianId;
        this.facilityId = facilityId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.durationMinutes = durationMinutes;
        this.appointmentType = appointmentType;
        this.status = status;
        this.reasonForVisit = reasonForVisit;
        this.notes = notes;
        this.createdDate = createdDate;
        this.lastModified = lastModified;
    }

    public static Appointment fromCSVRow(String[] r) {
        return new Appointment(
                safe(r, 0), safe(r, 1), safe(r, 2), safe(r, 3), safe(r, 4),
                safe(r, 5), safe(r, 6), safe(r, 7), safe(r, 8), safe(r, 9),
                safe(r, 10), safe(r, 11), safe(r, 12)
        );
    }

//    Converts CSV to the table
    public String[] toCSVRow() {
        return new String[]{appointmentId, patientId, clinicianId, facilityId, appointmentDate,
                appointmentTime, durationMinutes, appointmentType, status, reasonForVisit,
                notes, createdDate, lastModified};
    }

    private static String safe(String[] arr, int i) {
        return (i < arr.length && arr[i] != null) ? arr[i] : "";
    }
}
