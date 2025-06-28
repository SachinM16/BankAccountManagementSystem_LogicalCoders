package BAMS;

import javax.swing.*;
import java.awt.*;

public class BankApp {
    private JFrame frame;
    private BankDAO dao = new BankDAO();
    public BankApp() {
        frame = new JFrame("Bank Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        showMainMenu();
        frame.setVisible(true);
    }
    private void showMainMenu() {
        String[] options = {"Admin Login", "User Login", "Exit"};
        int choice = JOptionPane.showOptionDialog(frame,
                "Welcome to the Bank Management System",
                "Main Menu",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);
        switch (choice) {
            case 0 -> showAdminLogin();
            case 1 -> showUserLoginScreen();
            case 2 -> {
                JOptionPane.showMessageDialog(frame, "Thank you for using BMS. Goodbye!");
                System.exit(0);
            }
            default -> showMainMenu();
        }
    }
    private void showAdminLogin() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        JPasswordField adminPass = new JPasswordField();
        panel.add(new JLabel("Enter Admin Password:"));
        panel.add(adminPass);
        int option = JOptionPane.showConfirmDialog(frame, panel, "Admin Login", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String password = new String(adminPass.getPassword());
            if (password.equals("1234")) {
                showAdminPanel();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid Admin Password.");
                showMainMenu();
            }
        } else {
            showMainMenu();
        }
    }
    private void showAdminPanel() {
        JFrame adminFrame = new JFrame("Admin Panel");
        adminFrame.setSize(400, 400);
        adminFrame.setLayout(new GridLayout(4, 1, 10, 10));
        JButton viewUsersBtn = new JButton("View All Users");
        JButton addUserBtn = new JButton("Create New User");
        JButton deleteUserBtn = new JButton("Delete User"); 
        JButton logoutBtn = new JButton("Logout");
        viewUsersBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            for (User user : dao.getAllUsers()) {
                sb.append("UID: ").append(user.getUid())
                        .append(", Name: ").append(user.getName())
                        .append(", Balance: ₹").append(user.getBalance())
                        .append("\n");
            }
            JTextArea area = new JTextArea(sb.toString());
            area.setEditable(false);
            JScrollPane scroll = new JScrollPane(area);
            JOptionPane.showMessageDialog(adminFrame, scroll, "User List", JOptionPane.INFORMATION_MESSAGE);
        });
        addUserBtn.addActionListener(e -> {
            JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
            JTextField uidField = new JTextField();
            JTextField nameField = new JTextField();
            JTextField pinField = new JTextField();
            JTextField balanceField = new JTextField();
            panel.add(new JLabel("UID:"));
            panel.add(uidField);
            panel.add(new JLabel("Name:"));
            panel.add(nameField);
            panel.add(new JLabel("PIN:"));
            panel.add(pinField);
            panel.add(new JLabel("Initial Balance:"));
            panel.add(balanceField);
            int option = JOptionPane.showConfirmDialog(adminFrame, panel, "Add New User", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    int uid = Integer.parseInt(uidField.getText());
                    String name = nameField.getText();
                    String pin = pinField.getText();
                    double balance = Double.parseDouble(balanceField.getText());
                    User user = new User(uid, name, pin, balance);
                    boolean success = dao.createUser(user);
                    if (success) {
                        JOptionPane.showMessageDialog(adminFrame, "User added successfully.");
                    } else {
                        JOptionPane.showMessageDialog(adminFrame, "Failed to add user. UID may already exist.");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(adminFrame, "Invalid input. Please check your data.");
                }
            }
        });
        deleteUserBtn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(adminFrame, "Enter UID of user to delete:");
            try {
                int uid = Integer.parseInt(input);
                int confirm = JOptionPane.showConfirmDialog(adminFrame,
                        "Are you sure you want to delete user UID " + uid + "?",
                        "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    boolean deleted = dao.deleteUser(uid);
                    if (deleted) {
                        JOptionPane.showMessageDialog(adminFrame, "User deleted successfully.");
                    } else {
                        JOptionPane.showMessageDialog(adminFrame, "User not found or deletion failed.");
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(adminFrame, "Invalid UID. Please enter a number.");
            }
        });
        logoutBtn.addActionListener(e -> {
            adminFrame.dispose();
            showMainMenu();
        });
        adminFrame.add(viewUsersBtn);
        adminFrame.add(addUserBtn);
        adminFrame.add(deleteUserBtn); 
        adminFrame.add(logoutBtn);
        adminFrame.setVisible(true);
    }
    private void showUserLoginScreen() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        JTextField uidField = new JTextField();
        JPasswordField pinField = new JPasswordField();
        panel.add(new JLabel("Enter UID:"));
        panel.add(uidField);
        panel.add(new JLabel("Enter PIN:"));
        panel.add(pinField);
        int option = JOptionPane.showConfirmDialog(frame, panel, "User Login", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                int uid = Integer.parseInt(uidField.getText());
                String pin = new String(pinField.getPassword());

                User user = dao.getUser(uid, pin);
                if (user != null) {
                    showUserDashboard(user);
                } else {
                    JOptionPane.showMessageDialog(frame, "Login failed. Invalid UID or PIN.");
                    showMainMenu();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "UID must be a number.");
                showMainMenu();
            }
        } else {
            showMainMenu();
        }
    }
    private void showUserDashboard(User user) {
        JFrame dashboard = new JFrame("Welcome, " + user.getName());
        dashboard.setSize(400, 300);
        dashboard.setLayout(new GridLayout(4, 1, 10, 10));
        JButton balanceBtn = new JButton("Check Balance");
        JButton depositBtn = new JButton("Deposit");
        JButton withdrawBtn = new JButton("Withdraw");
        JButton logoutBtn = new JButton("Logout");
        balanceBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(dashboard, "Balance: ₹" + user.getBalance()));
        depositBtn.addActionListener(e -> {
            String amtStr = JOptionPane.showInputDialog(dashboard, "Enter amount to deposit:");
            try {
                double amt = Double.parseDouble(amtStr);
                double newBal = user.getBalance() + amt;
                dao.updateBalance(user.getUid(), newBal);
                user.setBalance(newBal);
                JOptionPane.showMessageDialog(dashboard, "Deposit successful.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dashboard, "Invalid input.");
            }
        });
        withdrawBtn.addActionListener(e -> {
            String amtStr = JOptionPane.showInputDialog(dashboard, "Enter amount to withdraw:");
            try {
                double amt = Double.parseDouble(amtStr);
                if (amt > user.getBalance()) {
                    JOptionPane.showMessageDialog(dashboard, "Insufficient balance.");
                } else {
                    double newBal = user.getBalance() - amt;
                    dao.updateBalance(user.getUid(), newBal);
                    user.setBalance(newBal);
                    JOptionPane.showMessageDialog(dashboard, "Withdrawal successful.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dashboard, "Invalid input.");
            }
        });
        logoutBtn.addActionListener(e -> {
            dashboard.dispose();
            showMainMenu();
        });
        dashboard.add(balanceBtn);
        dashboard.add(depositBtn);
        dashboard.add(withdrawBtn);
        dashboard.add(logoutBtn);
        dashboard.setVisible(true);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(BankApp::new);
    }
}
