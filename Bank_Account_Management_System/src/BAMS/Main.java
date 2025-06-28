package BAMS;

import java.util.List;
import java.util.Scanner;

public class Main {
	    static Scanner sc = new Scanner(System.in);
	    static BankDAO dao = new BankDAO();

	    public static void main(String[] args) {
	        while (true) {
	            try {
	                System.out.println("\n=== Bank Management System ===");
	                System.out.println("1. Admin Login\n2. User Login\n3. Exit");
	                int choice = Integer.parseInt(sc.nextLine());

	                switch (choice) {
	                    case 1 -> adminMenu();
	                    case 2 -> userLogin();
	                    case 3 -> {
	                    	 System.out.println("Thank you for using the Bank Account Management System. Goodbye!");
	                    	System.exit(0);           	
	                    }
	                    default -> System.out.println("Invalid option. Try again.");
	                }
	            } catch (Exception e) {
	                System.out.println("Invalid input. Please enter a number.");
	            }
	        }
	    }

	    static void adminMenu() {
	        System.out.print("Enter Admin PIN: ");
	        String pin = sc.nextLine();
	        if (!pin.equals("1234")) {
	            System.out.println("Wrong PIN!");
	            return;
	        }

	        while (true) {
	            try {
	                System.out.println("\n--- Admin Menu ---");
	                System.out.println("1. Create User\n2. View Users\n3. Delete User\n4. Exit");
	                int choice = Integer.parseInt(sc.nextLine());

	                switch (choice) {
	                    case 1 -> {
	                        System.out.print("UID: ");
	                        int uid = Integer.parseInt(sc.nextLine());
	                        System.out.print("Name: ");
	                        String name = sc.nextLine();
	                        System.out.print("PIN: ");
	                        String upin = sc.nextLine();
	                        System.out.print("Initial Balance: ");
	                        double bal = Double.parseDouble(sc.nextLine());

	                        if (bal < 0) {
	                            System.out.println("Balance can't be negative.");
	                            break;
	                        }

	                        boolean status = dao.createUser(new User(uid, name, upin, bal));
	                        System.out.println(status ? "User created." : "Failed to create user.");
	                    }

	                    case 2 -> {
	                        List<User> users = dao.getAllUsers();
	                        if (users.isEmpty()) {
	                            System.out.println("No users found.");
	                        } else {
	                            for (User u : users) {
	                                System.out.println("UID: " + u.getUid() + " | Name: " + u.getName() + " | Balance: " + u.getBalance());
	                            }
	                        }
	                    }

	                    case 3 -> {
	                        System.out.print("Enter UID to delete: ");
	                        int uid = Integer.parseInt(sc.nextLine());
	                        boolean deleted = dao.deleteUser(uid);
	                        System.out.println(deleted ? "User deleted." : "User not found.");
	                    }

	                    case 4 -> {
	                        return;
	                    }

	                    default -> System.out.println("Invalid option.");
	                }
	            } catch (Exception e) {
	                System.out.println("Error: Invalid input. Try again.");
	            }
	        }
	    }

	    static void userLogin() {
	        try {
	            System.out.print("Enter UID: ");
	            int uid = Integer.parseInt(sc.nextLine());
	            System.out.print("Enter PIN: ");
	            String pin = sc.nextLine();

	            User user = dao.getUser(uid, pin);
	            if (user == null) {
	                System.out.println("Login failed.");
	                return;
	            }

	            while (true) {
	                System.out.println("\n--- User Menu ---");
	                System.out.println("1. Withdraw\n2. Deposit\n3. View Balance\n4. Logout");
	                int choice = Integer.parseInt(sc.nextLine());

	                switch (choice) {
	                    case 1 -> {
	                        System.out.print("Enter amount to withdraw: ");
	                        double amt = Double.parseDouble(sc.nextLine());
	                        if (amt < 0) {
	                            System.out.println("Invalid amount.");
	                        } else if (amt > user.getBalance()) {
	                            System.out.println("Insufficient balance.");
	                        } else {
	                            user.setBalance(user.getBalance() - amt);
	                            dao.updateBalance(user.getUid(), user.getBalance());
	                            System.out.println("Withdrawn: " + amt);
	                        }
	                    }

	                    case 2 -> {
	                        System.out.print("Enter amount to deposit: ");
	                        double amt = Double.parseDouble(sc.nextLine());
	                        if (amt < 0) {
	                            System.out.println("Invalid amount.");
	                        } else {
	                            user.setBalance(user.getBalance() + amt);
	                            dao.updateBalance(user.getUid(), user.getBalance());
	                            System.out.println("Deposited: " + amt);
	                        }
	                    }

	                    case 3 -> System.out.println("Balance: " + user.getBalance());

	                    case 4 -> {
	                        System.out.println("Logged out.");
	                        return;
	                    }

	                    default -> System.out.println("Invalid choice.");
	                }
	            }
	        } catch (Exception e) {
	            System.out.println("Login failed. Invalid input.");
	        }
	    }
	}

