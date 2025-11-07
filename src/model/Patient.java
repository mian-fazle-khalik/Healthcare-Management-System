package model;

public class Patient {
    private String patientId; //patients id
    private String firstName; //patients name
    private String lastName; //patients last name
    private String dateOfBirth; //patients date of birthd
    private String nhsNumber; //patients nhs number
    private String gender; //patients gender
    private String phoneNumber; //patients phone
    private String email; //patients email
    private String address; //patinets address
    private String postcode; //patients postcode
    private String emergencyContactName; //patients contact name
    private String emergencyContactPhone; //patients contact phone
    private String registrationDate; //patients reg date
    private String gpSurgeryId; //patients surgery id

//    Constructor
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

//    CSV to text
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
