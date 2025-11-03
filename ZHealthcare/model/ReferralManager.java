package model;

public class ReferralManager {
    private static ReferralManager instance;
    private long counter = System.currentTimeMillis();

    private ReferralManager() {}

    public static ReferralManager getInstance() {
        if (instance == null) instance = new ReferralManager();
        return instance;
    }

    public synchronized String generateReferralId() {
        counter++;
        return "R" + counter;
    }
}
