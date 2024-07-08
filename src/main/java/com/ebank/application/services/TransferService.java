package com.ebank.application.services;

import com.ebank.application.interfaces.transfertInterface;
import com.ebank.application.models.CharityCampaignModel;
import com.ebank.application.models.Transfer;
import com.ebank.application.models.User;
import com.ebank.application.utils.MaConnexion;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransferService implements transfertInterface<Transfer> {
    Connection conn = MaConnexion.getInstance().getCnx();

    // Method to fetch transactions by reclamation ID
    public List<Transfer> getByReclamationId(int reclamationId) {
        List<Transfer> transactions = new ArrayList<>();
        String req = "SELECT * FROM `Transaction` WHERE `id_reclamation` = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(req);
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

    @Override
    public void deposit(double amount, User currentUser) throws SQLException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Please enter a positive value");
        }

        double total = amount + currentUser.getBalance();
        currentUser.setBalance(total);

        String sql = "UPDATE users SET balance = ? WHERE email = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setDouble(1, total);
        pst.setString(2, currentUser.getEmail());
        pst.executeUpdate();
    }

    @Override
    public void withdraw(double amount, User currentUser) throws SQLException {
        if ((currentUser.getBalance() - amount) < 0 || amount <= 0) {
            throw new IllegalArgumentException("Insufficient balance or invalid amount");
        }

        double total = currentUser.getBalance() - amount;
        currentUser.setBalance(total);

        String sql = "UPDATE users SET balance = ? WHERE email = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setDouble(1, total);
        pst.setString(2, currentUser.getEmail());
        pst.executeUpdate();
    }

    public void transfer(double amount, String receiverAccNumber, User currentUser) throws SQLException {
        if (receiverAccNumber.isBlank() || receiverAccNumber.equals(String.valueOf(currentUser.getAcc_num()))) {
            throw new IllegalArgumentException("Invalid receiver account number");
        }

        int receiverAccNum = Integer.parseInt(receiverAccNumber);
        String sql = "SELECT * FROM users WHERE acc_num = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, receiverAccNum);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            User receiver = new User(

                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getDate("dob").toLocalDate(),
                    rs.getInt("acc_num"),
                    rs.getDouble("balance"),
                    rs.getString("id"));

            int receiverId = rs.getInt("id");
            System.out.println(receiverId);
            if (amount <= 0 || amount > currentUser.getBalance()) {
                throw new IllegalArgumentException("Invalid amount or insufficient balance");
            }

            // Withdraw amount from the sender's account
            withdraw(amount, currentUser);

            // Deposit amount to the receiver's account
            receiver.setBalance(receiver.getBalance() + amount);
            String updateReceiverSql = "UPDATE users SET balance = ? WHERE acc_num = ?";
            PreparedStatement updateReceiverPst = conn.prepareStatement(updateReceiverSql);
            updateReceiverPst.setDouble(1, receiver.getBalance());
            updateReceiverPst.setInt(2, receiver.getAcc_num());
            updateReceiverPst.executeUpdate();

            // Insert transaction log
            String insertTransactionSql = "INSERT INTO transactions(idReceiver, idSender, montant, Date, type) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement insertTransactionPst = conn.prepareStatement(insertTransactionSql);
            insertTransactionPst.setInt(1, receiverId);
            insertTransactionPst.setInt(2, currentUser.getId());
            insertTransactionPst.setDouble(3, amount);
            insertTransactionPst.setDate(4, new java.sql.Date(System.currentTimeMillis())); // Current date
            insertTransactionPst.setString(5, "TRANSFER");
            insertTransactionPst.executeUpdate();

            System.out.println("Transaction added successfully!");
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    @Override
    public String add(Transfer transfer) {
        return "";
    }

    @Override
    public void delete(int id) {
        String req = "DELETE FROM `Transaction` WHERE `id` = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(req);
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
            PreparedStatement ps = conn.prepareStatement(req);
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
            Statement st = conn.createStatement();
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

    public List<Double> getTransferStatistics(int userId, LocalDate startDate, LocalDate endDate) throws SQLException {
        String sql = "SELECT date, SUM(montant) as total FROM transaction WHERE idSender = ? AND date BETWEEN ? AND ? GROUP BY date ORDER BY date";
        PreparedStatement pst = conn.prepareStatement(sql);
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

    @Override
    public void transfer2(double amount, String receiverAccNumber, User currentUser) throws SQLException {
        if (receiverAccNumber.isBlank() || receiverAccNumber.equals(String.valueOf(currentUser.getAcc_num()))) {
            throw new IllegalArgumentException("Invalid receiver account number");
        }

        int receiverAccNum = Integer.parseInt(receiverAccNumber);
        String sql = "SELECT * FROM charitycampaignmodel WHERE acc_num = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, receiverAccNum);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            CharityCampaignModel receiver = new CharityCampaignModel(
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getDate("dob").toLocalDate(),
                    rs.getInt("acc_num"),
                    rs.getDouble("balance"));

            if (amount <= 0 || amount > currentUser.getBalance()) {
                throw new IllegalArgumentException("Invalid amount or insufficient balance");
            }

            // Withdraw amount from the sender's account
            withdraw(amount, currentUser);

            // Deposit amount to the receiver's account
            receiver.setBalance(receiver.getBalance() + amount);
            String updateReceiverSql = "UPDATE charitycampaignmodel SET balance = ? WHERE acc_num = ?";
            PreparedStatement updateReceiverPst = conn.prepareStatement(updateReceiverSql);
            updateReceiverPst.setDouble(1, receiver.getBalance());
            updateReceiverPst.setInt(2, receiver.getAcc_num());
            updateReceiverPst.executeUpdate();
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

}
