package model;

public class Facility {
    private String facilityId; //facility id
    private String facilityName; //facility name
    private String facilityType; //facility type
    private String address; //facility address
    private String postcode; //facility postcode
    private String phoneNumber; //facility number
    private String email; //facility email
    private String openingHours; //facility hours
    private String managerName; //manager name
    private String capacity; //capacity 
    private String specialitiesOffered; //specialtied offered

//    Contrructor
    public Facility(String facilityId, String facilityName, String facilityType, String address, String postcode,
                    String phoneNumber, String email, String openingHours, String managerName, String capacity,
                    String specialitiesOffered) {
        this.facilityId = facilityId;
        this.facilityName = facilityName;
        this.facilityType = facilityType;
        this.address = address;
        this.postcode = postcode;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.openingHours = openingHours;
        this.managerName = managerName;
        this.capacity = capacity;
        this.specialitiesOffered = specialitiesOffered;
    }

//    Enters the CSV to a list
    public static Facility fromCSVRow(String[] r) {
        return new Facility(
                safe(r, 0), safe(r, 1), safe(r, 2), safe(r, 3), safe(r, 4),
                safe(r, 5), safe(r, 6), safe(r, 7), safe(r, 8), safe(r, 9), safe(r, 10)
        );
    }

    public String[] toCSVRow() {
        return new String[]{facilityId, facilityName, facilityType, address, postcode, phoneNumber,
                email, openingHours, managerName, capacity, specialitiesOffered};
    }

    private static String safe(String[] arr, int i) {
        return (i < arr.length && arr[i] != null) ? arr[i] : "";
    }
}
