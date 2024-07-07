package com.ebank.application.interfaces;

import com.ebank.application.models.User;

import java.sql.SQLException;
import java.util.List;

public interface transfertInterface<T> {

    public String add(T t);

    public void delete(int t);

    public void update(T t, int id);

    public List<T> getAll();

    void deposit(double amount, User currentUser) throws SQLException;

    void withdraw(double amount, User currentUser) throws SQLException;

    void transfer(double amount, String receiverAccNumber, User currentUser) throws SQLException;

    void transfer2(double amount, String receiverAccNumber, User currentUser) throws SQLException;

}
