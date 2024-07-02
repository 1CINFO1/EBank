package com.abcbank.application.Interface;

import com.abcbank.application.Models.User;

import java.sql.SQLException;

public interface LoginInterface {
    User authenticate(String email, String password) throws SQLException;

}
