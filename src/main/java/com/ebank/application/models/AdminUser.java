package com.ebank.application.models;

import java.time.LocalDate;

public class AdminUser extends User {

    private final String role = "ADMIN";

    public AdminUser(String name, String email, LocalDate dob, int acc_num, double balance, String password) {
        super(name, email, dob, acc_num, balance, password);

    }

    public AdminUser(String name, String email, LocalDate dob, int acc_num, double balance) {
        super(name, email, dob, acc_num, balance);

    }

    public AdminUser(String name, String email, LocalDate dob, int acc_num, String role) {
        super(name, email, dob, acc_num);

    }

    public AdminUser() {
    }

    public AdminUser(int id, String name, String email, LocalDate dob, int acc_num, double balance, String password) {
        super(id, name, email, dob, acc_num, balance, password);

    }

    public String getRole() {
        return role;
    }

}
