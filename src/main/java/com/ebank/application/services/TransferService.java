package com.ebank.application.services;

import com.ebank.application.EmailUtil;
import com.ebank.application.interfaces.InterfaceCRUD;
import com.ebank.application.models.Transfer;
import com.ebank.application.models.User;
import com.ebank.application.utils.MaConnexion;

import javafx.scene.chart.XYChart;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class TransferService implements InterfaceCRUD<Transfer> {
    private Connection cnx;

    // Constructor to initialize the connection
    public TransferService() {
        this.cnx = MaConnexion.getInstance().getCnx();
    }

    @Override
    public String add(Transfer t) {
        validateTransaction(t);
        String req = "INSERT INTO `Transaction`(`idReceiver`, `idSender`, `montant`, `Date`, `type`) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, t.getIdReceiver());
            ps.setInt(2, t.getIdSender());
            ps.setDouble(3, t.getMontant());
            ps.setDate(4, t.getDate());
            ps.setString(5, t.getType());
            ps.executeUpdate();
            String subject = "New Transaction Created";
            String body = "A new transaction has been created with the following details:\n" + t.toString();
            EmailUtil.sendEmail("recipient@example.com", subject, body);
            System.out.println("Transaction added successfully!");
            return("saif rabii yehdik");
        } catch (SQLException e) {
            throw new RuntimeException("Error while adding the transaction", e);
        }
    }

    @Override
    public void delete(int id) {
        String req = "DELETE FROM `Transaction` WHERE `id` = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            int rowsDeleted = ps.executeUpdate();
            System.out.println("Transaction deleted successfully! Rows affected: " + rowsDeleted);
        } catch (SQLException e) {
            throw new RuntimeException("Error while deleting the transaction", e);
        }
    }

    @Override
    public void update(Transfer t, int id) {
        validateTransaction(t);
        String req = "UPDATE `Transaction` SET `idReceiver` = ?, `idSender` = ?, `montant` = ?, `Date` = ?, `type` = ? WHERE `id` = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, t.getIdReceiver());
            ps.setInt(2, t.getIdSender());
            ps.setDouble(3, t.getMontant());
            ps.setDate(4, t.getDate());
            ps.setString(5, t.getType());
            ps.setInt(6, id);
            ps.executeUpdate();
            System.out.println("Transaction updated successfully!");
        } catch (SQLException e) {
            throw new RuntimeException("Error while updating the transaction", e);
        }
    }

    @Override
    public List<Transfer> getAll() {
        List<Transfer> transactions = new ArrayList<>();
        String req = "SELECT * FROM `Transaction`";
        try {
            Statement st = cnx.createStatement();
            ResultSet res = st.executeQuery(req);
            while (res.next()) {
                Transfer t = new Transfer();
                t.setId(res.getInt("id"));
                t.setIdReceiver(res.getInt("idReceiver"));
                t.setIdSender(res.getInt("idSender"));
                t.setMontant(res.getDouble("montant"));
                t.setType(res.getString("type"));
                t.setDate(res.getDate("Date"));
                transactions.add(t);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while retrieving transactions", e);
        }
        return transactions;
    }

    // Method to fetch transactions by reclamation ID
    public List<Transfer> getByReclamationId(int reclamationId) {
        List<Transfer> transactions = new ArrayList<>();
        String req = "SELECT * FROM `Transaction` WHERE `id_reclamation` = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, reclamationId);
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                Transfer t = new Transfer();
                t.setId(res.getInt("id"));
                t.setIdReceiver(res.getInt("idReceiver"));
                t.setIdSender(res.getInt("idSender"));
                t.setMontant(res.getDouble("montant"));
                t.setType(res.getString("type"));
                t.setDate(res.getDate("Date"));
                transactions.add(t);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while retrieving transactions for reclamation ID: " + reclamationId, e);
        }
        return transactions;
    }

    // Method to validate a transaction
    private void validateTransaction(Transfer t) {
        if (t.getIdReceiver() <= 0) {
            throw new IllegalArgumentException("Invalid receiver ID");
        }
        if (t.getIdSender() <= 0) {
            throw new IllegalArgumentException("Invalid sender ID");
        }
        if (t.getMontant() <= 0) {
            throw new IllegalArgumentException("Transaction amount must be greater than zero");
        }
    }

    // Method to deposit money into a user's account
    public void deposit(double amount, User currentUser) throws SQLException {
        if(amount <= 0) {
            throw new IllegalArgumentException("Please enter a positive value");
        }

        double total = amount + currentUser.getBalance();
        currentUser.setBalance(total);

        String sql = "UPDATE users SET balance = ? WHERE email = ?";
        PreparedStatement pst = cnx.prepareStatement(sql);
        pst.setDouble(1, total);
        pst.setString(2, currentUser.getEmail());
        pst.executeUpdate();
    }

    // Method to withdraw money from a user's account
    public void withdraw(double amount, User currentUser) throws SQLException {
        if((currentUser.getBalance() - amount) < 0 || amount <= 0) {JOptionPane.showMessageDialog(null, "Email or Password is empty");

            // throw new IllegalArgumentException("Insufficient balance or invalid amount");
        }

        double total = currentUser.getBalance() - amount;
        currentUser.setBalance(total);

        String sql = "UPDATE users SET balance = ? WHERE email = ?";
        PreparedStatement pst = cnx.prepareStatement(sql);
        pst.setDouble(1, total);
        pst.setString(2, currentUser.getEmail());
        pst.executeUpdate();
    }

    // Method to transfer money from one user's account to another
    public void transfer(double amount, String receiverAccNumber, User currentUser) throws SQLException {
        if(receiverAccNumber.isBlank() || receiverAccNumber.equals(String.valueOf(currentUser.getAcc_num()))) {
            throw new IllegalArgumentException("Invalid receiver account number");
        }

        int receiverAccNum = Integer.parseInt(receiverAccNumber);
        String sql = "SELECT * FROM users WHERE acc_num = ?";
        PreparedStatement pst = cnx.prepareStatement(sql);
        pst.setInt(1, receiverAccNum);
        ResultSet rs = pst.executeQuery();

        if(rs.next()) {
            User receiver = new User(
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getDate("dob").toLocalDate(),
                    rs.getInt("acc_num"),
                    rs.getDouble("balance")
            );

            if(amount <= 0 || amount > currentUser.getBalance()) {JOptionPane.showMessageDialog(null, "Invalid amount or insufficient balance");

                // throw new IllegalArgumentException("Invalid amount or insufficient balance");
            }

            // Withdraw amount from the sender's account
            withdraw(amount, currentUser);

            // Deposit amount to the receiver's account
            receiver.setBalance(receiver.getBalance() + amount);
            String updateReceiverSql = "UPDATE users SET balance = ? WHERE acc_num = ?";
            PreparedStatement updateReceiverPst = cnx.prepareStatement(updateReceiverSql);
            updateReceiverPst.setDouble(1, receiver.getBalance());
            updateReceiverPst.setInt(2, receiver.getAcc_num());
            updateReceiverPst.executeUpdate();
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    public List<Double> getTransferStatistics(int userId, LocalDate startDate, LocalDate endDate) throws SQLException {
        String sql = "SELECT date, SUM(montant) as total FROM transaction WHERE idSender = ? AND date BETWEEN ? AND ? GROUP BY date ORDER BY date";
        PreparedStatement pst = cnx.prepareStatement(sql);
        pst.setInt(1, userId);
        pst.setDate(2, java.sql.Date.valueOf(startDate));
        pst.setDate(3, java.sql.Date.valueOf(endDate));
        ResultSet rs = pst.executeQuery();

        List<Double> statistics = new ArrayList<>();
        while (rs.next()) {
            statistics.add(rs.getDouble("total"));
        }

        return statistics;
    }

}
