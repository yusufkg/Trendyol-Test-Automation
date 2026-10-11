package com.yusuf.qa.support;

import com.yusuf.qa.db.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.Instant;

public final class SqlTestLogger {
    private SqlTestLogger() {}
    public static void log(String scenario, String status, Instant start, Instant end, String error, String screenshot) {
        if (!Database.configured()) {
            System.err.println("SQL logging skipped: QA_DB_URL is not configured");
            return;
        }
        String sql = "INSERT INTO dbo.TestResults (Scenario,Status,StartedAt,FinishedAt,DurationMs,ErrorMessage,ScreenshotPath,Browser) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection c = Database.connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, scenario); ps.setString(2, status);
            ps.setTimestamp(3, java.sql.Timestamp.from(start)); ps.setTimestamp(4, java.sql.Timestamp.from(end));
            ps.setLong(5, java.time.Duration.between(start,end).toMillis());
            ps.setString(6, error); ps.setString(7, screenshot); ps.setString(8, "Chrome");
            ps.executeUpdate();
        } catch (Exception e) {
            System.err.println("SQL logging failed: " + e.getMessage());
            throw new IllegalStateException("Could not persist test result to SQL Server", e);
        }
    }
}
