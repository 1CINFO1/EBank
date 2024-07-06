package com.ebank.application.controllers;

import com.ebank.application.models.Transfer;
import com.ebank.application.models.User;
import com.ebank.application.services.TransferService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class TransferController {
    

    private final static TransferService transferService = new TransferService();

    // Method to perform a transfer
    public void transfer(double amount, String receiverAccNumber, User currentUser) throws SQLException {
        try {
            Transfer transfer = new Transfer();
            transfer.setIdSender(currentUser.getAcc_num()); // Assuming currentUser has account number
            transfer.setIdReceiver(Integer.parseInt(receiverAccNumber)); // Assuming receiverAccNumber is String representation of account number
            transfer.setMontant(amount);
            transfer.setType("Transfer"); // Assuming type is set to "Transfer"

            transferService.add(transfer); // Invoke service method to add transfer
            System.out.println("Transfer successful!");

            // Optionally, update UI or perform additional actions after successful transfer
        } catch (NumberFormatException e) {
            System.err.println("Invalid receiver account number format: " + receiverAccNumber);
        } catch (IllegalArgumentException e) {
            System.err.println("Error performing transfer: " + e.getMessage());
            // Handle or log IllegalArgumentException appropriately
        }
    }

    // Method to retrieve all transfers (optional)
    public List<Transfer> getAllTransfers() {
        return transferService.getAll();
    }

public static List<Double> getTransferStatistics(int userId, LocalDate startDate, LocalDate endDate) throws SQLException {
        return transferService.getTransferStatistics(userId, startDate, endDate);
    }
    
}
