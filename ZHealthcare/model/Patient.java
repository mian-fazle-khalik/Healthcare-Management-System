package model;

public class Patient {
    private String patientId;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String nhsNumber;
    private String gender;
    private String phoneNumber;
    private String email;
    private String address;
    private String postcode;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String registrationDate;
    private String gpSurgeryId;

    public Patient(String patientId, String firstName, String lastName, String dateOfBirth, String nhsNumber,
                   String gender, String phoneNumber, String email, String address, String postcode,
                   String emergencyContactName, String emergencyContactPhone, String registrationDate, String gpSurgeryId) {
        this.patientId = patientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.nhsNumber = nhsNumber;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.postcode = postcode;
        this.emergencyContactName = emergencyContactName;
        this.emergencyContactPhone = emergencyContactPhone;
        this.registrationDate = registrationDate;
        this.gpSurgeryId = gpSurgeryId;
    }

    public static Patient fromCSVRow(String[] r) {
        return new Patient(
                safe(r, 0), safe(r, 1), safe(r, 2), safe(r, 3), safe(r, 4),
                safe(r, 5), safe(r, 6), safe(r, 7), safe(r, 8), safe(r, 9),
                safe(r, 10), safe(r, 11), safe(r, 12), safe(r, 13)
        );
    }

    public String[] toCSVRow() {
        return new String[]{patientId, firstName, lastName, dateOfBirth, nhsNumber,
                gender, phoneNumber, email, address, postcode,
                emergencyContactName, emergencyContactPhone, registrationDate, gpSurgeryId};
    }

    private static String safe(String[] arr, int i) {
        return (i < arr.length && arr[i] != null) ? arr[i] : "";
    }
}
