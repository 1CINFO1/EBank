package com.ebank.application.models;

import com.ebank.application.enumeration.Role;

import java.time.LocalDate;

public class User {
    int id;
    String name;
    int acc_num;
    double balance;
    LocalDate dob;
    String password;
    String email;
    private final String role = "USER";

    public User() {
    }

    public User(String name, String email, LocalDate dob, int acc_num) {
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.acc_num = acc_num;
        this.balance = 0;
    }
    public User(int id,String name, String email, LocalDate dob, int acc_num, double balance,String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.acc_num = acc_num;
        this.balance = balance;
        this.password=password;
    
    }
    public User(String name, String email, LocalDate dob, int acc_num, double balance) {
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.acc_num = acc_num;
        this.balance = balance;
    
    }

    public User(String name, String email, LocalDate dob, int acc_num, double balance, String password) {
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.acc_num = acc_num;
        this.balance = balance;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAcc_num() {
        return acc_num;
    }

    public void setAcc_num(int acc_num) {
        this.acc_num = acc_num;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }


    public int getId() {
        return id;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }
}