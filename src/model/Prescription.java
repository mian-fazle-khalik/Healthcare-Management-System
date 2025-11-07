package model;

public class Prescription {
    private String prescriptionId; //Prescription is
    private String patientId; //patient is
    private String clinicianId; //clinician id
    private String appointmentId; //appointmrent is
    private String prescriptionDate; 
    private String medicationName;
    private String dosage;
    private String frequency;
    private String durationDays;
    private String quantity;
    private String instructions;
    private String pharmacyName;
    private String status;
    private String issueDate;
    private String collectionDate;

//    Constructor
    public Prescription(String prescriptionId, String patientId, String clinicianId, String appointmentId,
                        String prescriptionDate, String medicationName, String dosage, String frequency,
                        String durationDays, String quantity, String instructions, String pharmacyName,
                        String status, String issueDate, String collectionDate) {
        this.prescriptionId = prescriptionId;
        this.patientId = patientId;
        this.clinicianId = clinicianId;
        this.appointmentId = appointmentId;
        this.prescriptionDate = prescriptionDate;
        this.medicationName = medicationName;
        this.dosage = dosage;
        this.frequency = frequency;
        this.durationDays = durationDays;
        this.quantity = quantity;
        this.instructions = instructions;
        this.pharmacyName = pharmacyName;
        this.status = status;
        this.issueDate = issueDate;
        this.collectionDate = collectionDate;
    }

//    CSV Perscription to list
    public static Prescription fromCSVRow(String[] r) {
        return new Prescription(
                safe(r, 0), safe(r, 1), safe(r, 2), safe(r, 3), safe(r, 4),
                safe(r, 5), safe(r, 6), safe(r, 7), safe(r, 8), safe(r, 9),
                safe(r, 10), safe(r, 11), safe(r, 12), safe(r, 13), safe(r, 14)
        );
    }

    public String[] toCSVRow() {
        return new String[]{prescriptionId, patientId, clinicianId, appointmentId, prescriptionDate,
                medicationName, dosage, frequency, durationDays, quantity,
                instructions, pharmacyName, status, issueDate, collectionDate};
    }

    private static String safe(String[] arr, int i) {
        return (i < arr.length && arr[i] != null) ? arr[i] : "";
    }
}
