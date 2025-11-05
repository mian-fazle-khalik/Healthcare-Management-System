//package model;
//
//public class Referral {
//    private String referralId;
//    private String patientId;
//    private String referringClinicianId;
//    private String referredToClinicianId;
//    private String referringFacilityId;
//    private String referredToFacilityId;
//    private String referralDate;
//    private String urgencyLevel;
//    private String referralReason;
//    private String clinicalSummary;
//    private String requestedInvestigations;
//    private String status;
//    private String appointmentId;
//    private String notes;
//    private String createdDate;
//    private String lastUpdated;
//
//    public Referral(String referralId, String patientId, String referringClinicianId, String referredToClinicianId,
//                    String referringFacilityId, String referredToFacilityId, String referralDate, String urgencyLevel,
//                    String referralReason, String clinicalSummary, String requestedInvestigations, String status,
//                    String appointmentId, String notes, String createdDate, String lastUpdated) {
//        this.referralId = referralId;
//        this.patientId = patientId;
//        this.referringClinicianId = referringClinicianId;
//        this.referredToClinicianId = referredToClinicianId;
//        this.referringFacilityId = referringFacilityId;
//        this.referredToFacilityId = referredToFacilityId;
//        this.referralDate = referralDate;
//        this.urgencyLevel = urgencyLevel;
//        this.referralReason = referralReason;
//        this.clinicalSummary = clinicalSummary;
//        this.requestedInvestigations = requestedInvestigations;
//        this.status = status;
//        this.appointmentId = appointmentId;
//        this.notes = notes;
//        this.createdDate = createdDate;
//        this.lastUpdated = lastUpdated;
//    }
//
//    public static Referral fromCSVRow(String[] r) {
//        return new Referral(
//                safe(r, 0), safe(r, 1), safe(r, 2), safe(r, 3), safe(r, 4),
//                safe(r, 5), safe(r, 6), safe(r, 7), safe(r, 8), safe(r, 9),
//                safe(r, 10), safe(r, 11), safe(r, 12), safe(r, 13), safe(r, 14), safe(r, 15)
//        );
//    }
//
//    public String[] toCSVRow() {
//        return new String[]{
//                referralId, patientId, referringClinicianId, referredToClinicianId,
//                referringFacilityId, referredToFacilityId, referralDate, urgencyLevel,
//                referralReason, clinicalSummary, requestedInvestigations, status,
//                appointmentId, notes, createdDate, lastUpdated
//        };
//    }
//
//    private static String safe(String[] arr, int i) {
//        return (i < arr.length && arr[i] != null) ? arr[i] : "";
//    }
//}



package model;

public class Referral {
    private String referralId;
    private String patientId;
    private String referringClinicianId;
    private String referredToClinicianId;
    private String referringFacilityId;
    private String referredToFacilityId;
    private String referralDate;
    private String urgencyLevel;
    private String referralReason;
    private String clinicalSummary;
    private String requestedInvestigations;
    private String status;
    private String appointmentId;
    private String notes;
    private String createdDate;
    private String lastUpdated;

    public Referral(String referralId, String patientId, String referringClinicianId, String referredToClinicianId,
                    String referringFacilityId, String referredToFacilityId, String referralDate, String urgencyLevel,
                    String referralReason, String clinicalSummary, String requestedInvestigations, String status,
                    String appointmentId, String notes, String createdDate, String lastUpdated) {
        this.referralId = referralId;
        this.patientId = patientId;
        this.referringClinicianId = referringClinicianId;
        this.referredToClinicianId = referredToClinicianId;
        this.referringFacilityId = referringFacilityId;
        this.referredToFacilityId = referredToFacilityId;
        this.referralDate = referralDate;
        this.urgencyLevel = urgencyLevel;
        this.referralReason = referralReason;
        this.clinicalSummary = clinicalSummary;
        this.requestedInvestigations = requestedInvestigations;
        this.status = status;
        this.appointmentId = appointmentId;
        this.notes = notes;
        this.createdDate = createdDate;
        this.lastUpdated = lastUpdated;
    }

    // ===================================================
    // ✅ GETTERS (needed for ReferralManager)
    // ===================================================
    public String getReferralId() { return referralId; }
    public String getPatientId() { return patientId; }
    public String getReferringClinicianId() { return referringClinicianId; }
    public String getReferredToClinicianId() { return referredToClinicianId; }
    public String getReferringFacilityId() { return referringFacilityId; }
    public String getReferredToFacilityId() { return referredToFacilityId; }
    public String getReferralDate() { return referralDate; }
    public String getUrgencyLevel() { return urgencyLevel; }
    public String getReferralReason() { return referralReason; }
    public String getClinicalSummary() { return clinicalSummary; }
    public String getRequestedInvestigations() { return requestedInvestigations; }
    public String getStatus() { return status; }
    public String getAppointmentId() { return appointmentId; }
    public String getNotes() { return notes; }
    public String getCreatedDate() { return createdDate; }
    public String getLastUpdated() { return lastUpdated; }

    // ===================================================
    // EXISTING METHODS
    // ===================================================
    public static Referral fromCSVRow(String[] r) {
        return new Referral(
                safe(r, 0), safe(r, 1), safe(r, 2), safe(r, 3), safe(r, 4),
                safe(r, 5), safe(r, 6), safe(r, 7), safe(r, 8), safe(r, 9),
                safe(r, 10), safe(r, 11), safe(r, 12), safe(r, 13), safe(r, 14), safe(r, 15)
        );
    }

    public String[] toCSVRow() {
        return new String[]{
                referralId, patientId, referringClinicianId, referredToClinicianId,
                referringFacilityId, referredToFacilityId, referralDate, urgencyLevel,
                referralReason, clinicalSummary, requestedInvestigations, status,
                appointmentId, notes, createdDate, lastUpdated
        };
    }

    private static String safe(String[] arr, int i) {
        return (i < arr.length && arr[i] != null) ? arr[i] : "";
    }
}
