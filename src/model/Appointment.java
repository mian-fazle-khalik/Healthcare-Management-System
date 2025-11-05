package model;

public class Appointment {
    private String appointmentId;
    private String patientId;
    private String clinicianId;
    private String facilityId;
    private String appointmentDate;
    private String appointmentTime;
    private String durationMinutes;
    private String appointmentType;
    private String status;
    private String reasonForVisit;
    private String notes;
    private String createdDate;
    private String lastModified;

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

    public String[] toCSVRow() {
        return new String[]{appointmentId, patientId, clinicianId, facilityId, appointmentDate,
                appointmentTime, durationMinutes, appointmentType, status, reasonForVisit,
                notes, createdDate, lastModified};
    }

    private static String safe(String[] arr, int i) {
        return (i < arr.length && arr[i] != null) ? arr[i] : "";
    }
}
