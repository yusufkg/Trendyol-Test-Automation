package com.yusuf.qa.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Database {
    private Database() {}
    public static boolean configured() {
        return System.getenv("QA_DB_URL") != null && !System.getenv("QA_DB_URL").isBlank();
    }
    public static Connection connect() throws SQLException {
        String url = System.getenv("QA_DB_URL");
        if (url == null || url.isBlank()) throw new SQLException("QA_DB_URL is not set");
        java.util.Properties properties = new java.util.Properties();
        String user = System.getenv("QA_DB_USER");
        String password = System.getenv("QA_DB_PASSWORD");
        if (user != null && !user.isBlank()) properties.setProperty("user", user);
        if (password != null) properties.setProperty("password", password);
        return DriverManager.getConnection(url, properties);
    }
}
