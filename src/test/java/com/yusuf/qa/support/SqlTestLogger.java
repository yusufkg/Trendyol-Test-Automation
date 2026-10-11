package com.yusuf.qa.support;

import com.yusuf.qa.db.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.Instant;

public final class SqlTestLogger {
    private SqlTestLogger() {}
    public static String log(String scenario, String status, Instant start, Instant end, String error, String screenshot) {
        return log(scenario, status, start, end, error, screenshot, "Chrome");
    }
    public static String log(String scenario, String status, Instant start, Instant end, String error, String screenshot, String browser) {
        if (!Database.configured()) {
            System.err.println("SQL logging skipped: QA_DB_URL is not configured");
            if (Boolean.getBoolean("qa.db.required")) throw new IllegalStateException("SQL logging requires QA_DB_URL");
            return "NOT_CONFIGURED";
        }
        String sql = "INSERT INTO dbo.TestResults (Scenario,Status,StartedAt,FinishedAt,DurationMs,ErrorMessage,ScreenshotPath,Browser) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection c = Database.connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, scenario); ps.setString(2, status);
            java.util.Calendar utc = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"));
            ps.setTimestamp(3, java.sql.Timestamp.from(start), utc); ps.setTimestamp(4, java.sql.Timestamp.from(end), utc);
            ps.setLong(5, java.time.Duration.between(start,end).toMillis());
            ps.setString(6, error); ps.setString(7, screenshot); ps.setString(8, browser);
            ps.executeUpdate();
            return "SAVED";
        } catch (Exception e) {
            System.err.println("SQL logging failed: " + e.getMessage());
            throw new IllegalStateException("Could not persist test result to SQL Server", e);
        }
    }
}
