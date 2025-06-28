package BAMS;

import java.sql.*;
import java.util.*;

public class BankDAO {
    private final String URL = "jdbc:mysql://localhost:3306/bankdb";
    private final String USER = "root";
    private final String PASSWORD = "sachin1610";

    // Runtime collection 
    private Map<Integer, User> userCache;

    //Default constructor
    public BankDAO() {
        this.userCache = new HashMap<>();
    }

    //Constructor 
    public BankDAO(Map<Integer, User> userCache) {
        this.userCache = userCache;
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Create User
    public boolean createUser(User user) {
        try (Connection conn = connect()) {
            String sql = "INSERT INTO users(uid, name, pin, balance) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, user.getUid());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getPin());
            stmt.setDouble(4, user.getBalance());
            boolean status = stmt.executeUpdate() > 0;
            if (status) userCache.put(user.getUid(), user);
            return status;
        } catch (SQLException e) {
            System.out.println("Error while creating user: " + e.getMessage());
            return false;
        }
    }

    // Get user by uid and pin
    public User getUser(int uid, String pin) {
        try (Connection conn = connect()) {
            String sql = "SELECT * FROM users WHERE uid = ? AND pin = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, uid);
            stmt.setString(2, pin);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User u = new User(uid, rs.getString("name"), pin, rs.getDouble("balance"));
                userCache.put(uid, u);
                return u;
            }
        } catch (SQLException e) {
            System.out.println("Error while fetching user: " + e.getMessage());
        }
        return null;
    }

    // Update Balance
    public boolean updateBalance(int uid, double amount) {
        try (Connection conn = connect()) {
            String sql = "UPDATE users SET balance = ? WHERE uid = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, amount);
            stmt.setInt(2, uid);
            boolean updated = stmt.executeUpdate() > 0;
            if (updated && userCache.containsKey(uid)) {
                userCache.get(uid).setBalance(amount);
            }
            return updated;
        } catch (SQLException e) {
            System.out.println("Error while updating balance: " + e.getMessage());
            return false;
        }
    }

    // Delete user
    public boolean deleteUser(int uid) {
        try (Connection conn = connect()) {
            String sql = "DELETE FROM users WHERE uid = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, uid);
            boolean deleted = stmt.executeUpdate() > 0;
            if (deleted) userCache.remove(uid);
            return deleted;
        } catch (SQLException e) {
            System.out.println("Error while deleting user: " + e.getMessage());
            return false;
        }
    }

    // Get all users
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        try (Connection conn = connect()) {
            String sql = "SELECT * FROM users";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User u = new User(
                        rs.getInt("uid"),
                        rs.getString("name"),
                        rs.getString("pin"),
                        rs.getDouble("balance")
                );
                list.add(u);
                userCache.put(u.getUid(), u);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching users: " + e.getMessage());
        }
        return list;
    }
}
