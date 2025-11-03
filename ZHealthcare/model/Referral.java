package model;

public class Referral {
    private static Referral instance;
    private String referralId;
    private String patientId;
    private String clinicianId;
    private String notes;

    private Referral() {} // private constructor

    public static Referral getInstance() {
        if (instance == null) {
            instance = new Referral();
        }
        return instance;
    }

    // getters/setters
    public void setReferralDetails(String referralId, String patientId, String clinicianId, String notes) {
        this.referralId = referralId;
        this.patientId = patientId;
        this.clinicianId = clinicianId;
        this.notes = notes;
    }

    public String[] toCSVRow() {
        return new String[]{referralId, patientId, clinicianId, notes};
    }
}
