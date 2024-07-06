package com.ebank.application.services;

import com.ebank.application.models.AdminUser;
import com.ebank.application.models.CharityCampaignModel;
import com.ebank.application.models.User;
import com.ebank.application.utils.MaConnexion;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class LoginService {

    private final Connection conn = MaConnexion.getInstance().getCnx();
    private PreparedStatement pst;
    private ResultSet rs;

    private final String errorStyle = "-fx-border-color: RED;";
    private final String successStyle = "-fx-border-color: #A9A9A9;";

    public boolean emailAlreadyExists(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ?";
        pst = conn.prepareStatement(sql);
        pst.setString(1, email);
        rs = pst.executeQuery();
        return rs.next();
    }

    public boolean accountNumberAlreadyExists(String accountNumber) throws SQLException {
        String sql = "SELECT * FROM users WHERE acc_num = ?";
        pst = conn.prepareStatement(sql);
        pst.setString(1, accountNumber);
        rs = pst.executeQuery();
        return rs.next();
    }

    public boolean isValid(String name, String email, String accountNumber, String password, LocalDate dob,String acountyType) throws SQLException {
        boolean isValid = true;
        if (name.isBlank() ) {
            isValid = false;
        }
        if (email.isBlank() || emailAlreadyExists(email)) {
            isValid = false;
        }
        if (accountNumber.isBlank() || accountNumber.length() < 8 || accountNumberAlreadyExists(accountNumber)) {
            isValid = false;
        }
        if (password.isBlank() || password.length() < 8) {
            isValid = false;
        }
        if (dob.toString().isBlank()) {
            isValid = false;
        }
        if (acountyType.isBlank()){
        isValid = false;
        }
        return isValid;
    }

    public void addUser(String name, String email, String accountNumber, LocalDate dob, String password) throws SQLException {
        String sql = "INSERT INTO users (name, email, acc_num, balance, dob, password,) VALUES (?, ?, ?, ?, ?, ?)";
        User newUser = new User(name, email, dob, Integer.parseInt(accountNumber));
        pst = conn.prepareStatement(sql);
        pst.setString(1, newUser.getName());
        pst.setString(2, newUser.getEmail());
        pst.setInt(3, newUser.getAcc_num());
        pst.setDouble(4, newUser.getBalance());
        pst.setDate(5, Date.valueOf(newUser.getDob()));
        pst.setString(6, password);
        pst.execute();
    }
    public void addUser2(String name, String email, String accountNumber, LocalDate dob, String password) throws SQLException {
        String sql = "INSERT INTO charitycampaignmodel (name, email, acc_num, balance, dob, password) VALUES (?, ?, ?, ?, ?, ?)";
        User newUser = new User(name, email, dob, Integer.parseInt(accountNumber));
        pst = conn.prepareStatement(sql);
        pst.setString(1, newUser.getName());
        pst.setString(2, newUser.getEmail());
        pst.setInt(3, newUser.getAcc_num());
        pst.setDouble(4, newUser.getBalance());
        pst.setDate(5, Date.valueOf(newUser.getDob()));
        pst.setString(6, password);
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
            String name = rs.getString("name");
            String userEmail = rs.getString("email");
            LocalDate dob = rs.getDate("dob").toLocalDate();
            int accNum = rs.getInt("acc_num");
            double balance = rs.getDouble("balance");

            return new User(name, userEmail, dob, accNum, balance, password);
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
            String name = rs.getString("name");
            String userEmail = rs.getString("email");
            LocalDate dob = rs.getDate("dob").toLocalDate();
            int accNum = rs.getInt("acc_num");
            double balance = rs.getDouble("balance");

            return new AdminUser(name, userEmail, dob, accNum, balance, password);
        }
        return null;
    }
}