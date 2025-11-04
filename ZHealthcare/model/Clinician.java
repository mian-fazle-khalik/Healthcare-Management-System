package model;

public class Clinician {
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
    private String startDate;

    public Clinician(String clinicianId, String firstName, String lastName, String title, String speciality,
                     String gmcNumber, String phoneNumber, String email, String workplaceId, String workplaceType,
                     String employmentStatus, String startDate) {
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

    public static Clinician fromCSVRow(String[] r) {
        return new Clinician(
                safe(r, 0), safe(r, 1), safe(r, 2), safe(r, 3), safe(r, 4),
                safe(r, 5), safe(r, 6), safe(r, 7), safe(r, 8), safe(r, 9),
                safe(r, 10), safe(r, 11)
        );
    }

    public String[] toCSVRow() {
        return new String[]{clinicianId, firstName, lastName, title, speciality, gmcNumber,
                phoneNumber, email, workplaceId, workplaceType, employmentStatus, startDate};
    }

    private static String safe(String[] arr, int i) {
        return (i < arr.length && arr[i] != null) ? arr[i] : "";
    }
}
