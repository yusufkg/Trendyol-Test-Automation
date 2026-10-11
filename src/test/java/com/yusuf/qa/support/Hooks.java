package com.yusuf.qa.support;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.Duration;
import java.util.UUID;

public class Hooks {
    @Before public void before() { TestContext.START.set(Instant.now()); FailureCapture.ERROR.remove(); }
    @After public void after(Scenario scenario) {
        WebDriver driver = TestContext.driver();
        Instant start = TestContext.START.get();
        Instant end = Instant.now();
        String screenshot = null;
        try {
            if (scenario.isFailed() && driver instanceof TakesScreenshot ts) {
                try {
                byte[] bytes = ts.getScreenshotAs(OutputType.BYTES);
                Path dir = Path.of("target", "screenshots");
                Files.createDirectories(dir);
                Path path = dir.resolve(UUID.randomUUID() + ".png");
                Files.write(path, bytes);
                screenshot = path.toString();
                scenario.attach(bytes, "image/png", "Hata ekran görüntüsü");
                } catch (org.openqa.selenium.WebDriverException | java.io.IOException e) {
                    scenario.log("Ekran görüntüsü alınamadı: " + e.getMessage());
                }
            }
            long elapsed = Duration.between(start, end).toMillis();
            String error = FailureCapture.ERROR.get();
            String sqlStatus = "FAILED";
            RuntimeException sqlFailure = null;
            try { sqlStatus = SqlTestLogger.log(scenario.getName(), scenario.getStatus().name(), start, end, error, screenshot); }
            catch (RuntimeException e) {
                sqlFailure = e; scenario.log("SQL kayıt hatası: " + e.getMessage());
                error = (error == null ? "" : error + "\n") + FailureCapture.stackTrace(e);
            }
            // Dashboard is always generated, even if SQL Server is not configured.
            HtmlDashboard.record(scenario.getName(), scenario.getStatus().name(), elapsed,
                screenshot == null ? null : Path.of("screenshots", Path.of(screenshot).getFileName().toString()).toString(), error, sqlStatus);
            if (sqlFailure != null) throw sqlFailure;
        } catch (java.io.IOException e) {
            throw new IllegalStateException("Rapor veya ekran görüntüsü yazılamadı", e);
        } finally {
            try { if (driver != null) driver.quit(); }
            catch (org.openqa.selenium.NoSuchSessionException e) {
                scenario.log("Test tarayıcısının oturumu zaten kapanmış.");
            }
            catch (org.openqa.selenium.WebDriverException e) {
                if (!scenario.isFailed()) throw e;
                scenario.log("Tarayıcı kapatma sırasında ek hata: " + e.getMessage());
            }
            finally { TestContext.DRIVER.remove(); TestContext.START.remove(); FailureCapture.ERROR.remove(); }
        }
    }
}
