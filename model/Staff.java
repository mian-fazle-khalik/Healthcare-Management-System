package model;

public class Staff {
    private String staffId;
    private String firstName;
    private String lastName;
    private String role;
    private String department;
    private String facilityId;
    private String phoneNumber;
    private String email;
    private String employmentStatus;
    private String startDate;
    private String lineManager;
    private String accessLevel;

//    Constructor
    public Staff(String staffId, String firstName, String lastName, String role, String department,
                 String facilityId, String phoneNumber, String email, String employmentStatus,
                 String startDate, String lineManager, String accessLevel) {
        this.staffId = staffId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.department = department;
        this.facilityId = facilityId;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.employmentStatus = employmentStatus;
        this.startDate = startDate;
        this.lineManager = lineManager;
        this.accessLevel = accessLevel;
    }

//    CSV to table list
    public static Staff fromCSVRow(String[] r) {
        return new Staff(
                safe(r, 0), safe(r, 1), safe(r, 2), safe(r, 3), safe(r, 4),
                safe(r, 5), safe(r, 6), safe(r, 7), safe(r, 8), safe(r, 9),
                safe(r, 10), safe(r, 11)
        );
    }

    public String[] toCSVRow() {
        return new String[]{staffId, firstName, lastName, role, department,
                facilityId, phoneNumber, email, employmentStatus, startDate, lineManager, accessLevel};
    }

    private static String safe(String[] arr, int i) {
        return (i < arr.length && arr[i] != null) ? arr[i] : "";
    }
}
