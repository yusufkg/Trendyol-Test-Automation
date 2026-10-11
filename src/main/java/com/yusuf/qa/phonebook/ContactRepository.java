package com.yusuf.qa.phonebook;

import com.yusuf.qa.db.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactRepository {
    public int add(String first, String last, String phone, String email) throws SQLException {
        validate(first, last, phone);
        String sql = "INSERT INTO dbo.Contacts (FirstName, LastName, Phone, Email) VALUES (?, ?, ?, ?)";
        try (Connection c = Database.connect(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, first.trim()); ps.setString(2, last.trim()); ps.setString(3, phone.trim()); ps.setString(4, email);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) { if (keys.next()) return keys.getInt(1); }
            throw new SQLException("Generated contact ID missing");
        }
    }
    public List<Contact> list() throws SQLException {
        List<Contact> contacts = new ArrayList<>();
        try (Connection c = Database.connect(); PreparedStatement ps = c.prepareStatement("SELECT Id, FirstName, LastName, Phone, Email FROM dbo.Contacts ORDER BY Id"); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) contacts.add(new Contact(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
        }
        return contacts;
    }
    public boolean update(int id, String first, String last, String phone, String email) throws SQLException {
        validate(first, last, phone);
        try (Connection c = Database.connect(); PreparedStatement ps = c.prepareStatement("UPDATE dbo.Contacts SET FirstName=?, LastName=?, Phone=?, Email=? WHERE Id=?")) {
            ps.setString(1, first.trim()); ps.setString(2, last.trim()); ps.setString(3, phone.trim()); ps.setString(4, email); ps.setInt(5, id);
            return ps.executeUpdate() > 0;
        }
    }
    public boolean delete(int id) throws SQLException {
        try (Connection c = Database.connect(); PreparedStatement ps = c.prepareStatement("DELETE FROM dbo.Contacts WHERE Id=?")) {
            ps.setInt(1, id); return ps.executeUpdate() > 0;
        }
    }
    private void validate(String first, String last, String phone) {
        if (first == null || first.isBlank() || last == null || last.isBlank() || phone == null || !phone.matches("[+0-9 ()-]{7,30}"))
            throw new IllegalArgumentException("First name, last name and a valid phone are required");
    }
}
