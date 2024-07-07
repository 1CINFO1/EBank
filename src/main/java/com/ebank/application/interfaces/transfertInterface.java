package com.ebank.application.interfaces;

import com.ebank.application.models.User;

import java.sql.SQLException;

public interface transfertInterface  {


    void deposit(double amount, User currentUser) throws SQLException;
    void withdraw(double amount, User currentUser) throws SQLException;
    void transfer(double amount, String receiverAccNumber, User currentUser) throws SQLException;
    void transfer2(double amount, String receiverAccNumber, User currentUser) throws SQLException;
}
