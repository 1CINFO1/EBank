package com.ebank.application.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.ebank.application.models.AdminUser;
import com.ebank.application.utils.MaConnexion;

public class AdminUserService {
    private final Connection conn = MaConnexion.getInstance().getCnx();

    public List<AdminUser> getAllAdminUsers() throws SQLException {
        List<AdminUser> adminUsers = new ArrayList<>();
        String sql = "SELECT * FROM adminuser";
        try (PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                adminUsers.add(createAdminUserFromResultSet(rs));
            }
        }
        return adminUsers;
    }

    public List<AdminUser> getAllExcept(int excludeUserId) throws SQLException {
        List<AdminUser> adminUsers = new ArrayList<>();
        String sql = "SELECT * FROM adminuser WHERE id != ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, excludeUserId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    adminUsers.add(createAdminUserFromResultSet(rs));
                }
            }
        }
        return adminUsers;
    }

    private AdminUser createAdminUserFromResultSet(ResultSet rs) throws SQLException {
        return new AdminUser(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getDate("dob").toLocalDate(),
                rs.getInt("acc_num"),
                rs.getDouble("balance"),
                rs.getString("password"));
    }
}