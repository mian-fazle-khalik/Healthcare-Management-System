package model;

public class ReferralManager {
    private static ReferralManager instance;
    private int counter = 1;

    private ReferralManager() {}

    public static synchronized ReferralManager getInstance() {
        if (instance == null) instance = new ReferralManager();
        return instance;
    }

    public String generateReferralId() {
        return "R" + (counter++);
    }
}
