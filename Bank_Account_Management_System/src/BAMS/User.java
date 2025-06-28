package BAMS;

public class User {
    private int uid;
    private String name;
    private String pin;
    private double balance;

    // Default constructor
    public User() {}

    // Constructor with all fields
    public User(int uid, String name, String pin, double balance) {
        this.uid = uid;
        this.name = name;
        this.pin = pin;
        this.balance = balance;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
