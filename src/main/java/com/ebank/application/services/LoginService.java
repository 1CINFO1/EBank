package com.ebank.application.services;

import com.ebank.application.models.AdminUser;
import com.ebank.application.models.CharityCampaignModel;
import com.ebank.application.models.User;
import com.ebank.application.utils.MaConnexion;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginService {

    private final Connection conn = MaConnexion.getInstance().getCnx();
    private PreparedStatement pst;
    private PreparedStatement pst1;
    private PreparedStatement pst2;
    private PreparedStatement pst3;
    private ResultSet rs;
    private ResultSet rs1;
    private ResultSet rs2;
    private ResultSet rs3;

    public boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean validateEmail(String email) {

        if (email == null || email.isEmpty()) {
            return false;
        }

        if (!isValidEmail(email)) {
            return false;
        }

        return true;
    }

    public boolean emailAlreadyExists(String email, String role) throws SQLException {
        String sqlUsers = "SELECT * FROM users WHERE email = ?";
        String sqlCharity = "SELECT * FROM charitycampaignmodel WHERE email = ?";
        String sqlAdmin = "SELECT * FROM adminuser WHERE email = ?";

        // Prepare statements for both queries
        pst1 = conn.prepareStatement(sqlUsers);
        pst1.setString(1, email);
        pst2 = conn.prepareStatement(sqlCharity);
        pst2.setString(1, email);
        pst3 = conn.prepareStatement(sqlAdmin);
        pst3.setString(1, email);
        // Execute the queries
        rs1 = pst1.executeQuery();
        rs2 = pst2.executeQuery();
        rs3 = pst3.executeQuery();

        // Check if email exists in either table
        boolean emailExists = rs1.next() || rs2.next() || rs3.next();
        return emailExists;
    }

    public boolean accountNumberAlreadyExists(String accountNumber, String role) throws SQLException {
        String sql = role.equals("CHARITY") ? "SELECT * FROM charitycampaignmodel WHERE acc_num = ?"
                : "SELECT * FROM users WHERE acc_num = ?";
        pst = conn.prepareStatement(sql);
        pst.setString(1, accountNumber);
        rs = pst.executeQuery();
        return rs.next();
    }

    public boolean isValid(String name, String email, String accountNumber, String password, LocalDate dob, String role)
            throws SQLException {
        List<String> errors = new ArrayList<>();

        if (name.isBlank()) {
            errors.add("Name cannot be blank.");
        }
        if (!validateEmail(email)) {
            errors.add("Invalid Email .");
        } else if (emailAlreadyExists(email, email)) {
            errors.add("Email already exists.");
        }
        if (accountNumber.isBlank() || accountNumber.length() < 8) {
            errors.add("Account number must be at least 8 characters.");
        } else if (accountNumberAlreadyExists(accountNumber, role)) {
            errors.add("Account number already exists.");
        }
        if (password.isBlank() || password.length() < 8) {
            errors.add("Password must be at least 8 characters.");
        }
        if (dob == null) {
            errors.add("Date of Birth must be selected.");
        }
        if (role == null || role.isBlank()) {
            errors.add("Role must be selected.");
        }

        if (!errors.isEmpty()) {
            showAlerts(errors);
            return false;
        }

        return true;
    }

    private void showAlerts(List<String> errors) {
        StringBuilder errorMessage = new StringBuilder();
        for (String error : errors) {
            errorMessage.append(error).append("\n");
        }

        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText("Please correct the following errors:");
        alert.setContentText(errorMessage.toString());
        alert.showAndWait();
    }

    public void addUser(String name, String email, String accountNumber, LocalDate dob, String password, String role)
            throws SQLException {
        String sql = role.equals("USER")
                ? "INSERT INTO users (name, email, acc_num, balance, dob, password, role) VALUES (?, ?, ?, ?, ?, ?, ?)"
                : "INSERT INTO charitycampaignmodel (name, email, acc_num, balance, dob, password, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
        User newUser = new User(name, email, dob, Integer.parseInt(accountNumber));
        pst = conn.prepareStatement(sql);
        pst.setString(1, newUser.getName());
        pst.setString(2, newUser.getEmail());
        pst.setInt(3, newUser.getAcc_num());
        pst.setDouble(4, newUser.getBalance());
        pst.setDate(5, Date.valueOf(newUser.getDob()));
        pst.setString(6, password);
        pst.setString(7, role);
        pst.execute();
    }

    public CharityCampaignModel getCharityUser(String email, String password) throws SQLException {
        String sql = "SELECT * FROM charitycampaignmodel WHERE email = ? AND password = ?";
        pst = conn.prepareStatement(sql);
        pst.setString(1, email);
        pst.setString(2, password);
        rs = pst.executeQuery();

        if (rs.next()) {
            String name = rs.getString("name");
            String userEmail = rs.getString("email");
            LocalDate dob = rs.getDate("dob").toLocalDate();
            int accNum = rs.getInt("acc_num");
            double balance = rs.getDouble("balance");
            String compagnieDeDonPatente = rs.getString("compagnieDeDon_Patente");

            return new CharityCampaignModel(name, userEmail, dob, accNum, balance, password, compagnieDeDonPatente);
        }
        return null;
    }

    public User getRegularUser(String email, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        pst = conn.prepareStatement(sql);
        pst.setString(1, email);
        pst.setString(2, password);
        rs = pst.executeQuery();

        if (rs.next()) {
            int id = rs.getInt("id");

            String name = rs.getString("name");
            String userEmail = rs.getString("email");
            LocalDate dob = rs.getDate("dob").toLocalDate();
            int accNum = rs.getInt("acc_num");
            double balance = rs.getDouble("balance");

            return new User(id, name, userEmail, dob, accNum, balance, password);
        }
        return null;
    }

    public AdminUser getAdminUser(String email, String password) throws SQLException {
        String sql = "SELECT * FROM adminuser WHERE email = ? AND password = ?";
        pst = conn.prepareStatement(sql);
        pst.setString(1, email);
        pst.setString(2, password);
        rs = pst.executeQuery();

        if (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String userEmail = rs.getString("email");
            LocalDate dob = rs.getDate("dob").toLocalDate();
            int accNum = rs.getInt("acc_num");
            double balance = rs.getDouble("balance");

            return new AdminUser(id, name, userEmail, dob, accNum, balance, password);
        }
        return null;
    }
}