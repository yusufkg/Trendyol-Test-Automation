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
        return DriverManager.getConnection(url,
            System.getenv().getOrDefault("QA_DB_USER", ""),
            System.getenv().getOrDefault("QA_DB_PASSWORD", ""));
    }
}
