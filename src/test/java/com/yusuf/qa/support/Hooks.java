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
    @Before public void before() { TestContext.START.set(Instant.now()); }
    @After public void after(Scenario scenario) {
        WebDriver driver = TestContext.driver();
        Instant start = TestContext.START.get();
        Instant end = Instant.now();
        String screenshot = null;
        try {
            if (scenario.isFailed() && driver instanceof TakesScreenshot ts) {
                byte[] bytes = ts.getScreenshotAs(OutputType.BYTES);
                Path dir = Path.of("target", "screenshots");
                Files.createDirectories(dir);
                Path path = dir.resolve(UUID.randomUUID() + ".png");
                Files.write(path, bytes);
                screenshot = path.toString();
                scenario.attach(bytes, "image/png", "Hata ekran görüntüsü");
            }
            long elapsed = Duration.between(start, end).toMillis();
            String error = scenario.isFailed() ? "Senaryo başarısız; ayrıntılı istisna Cucumber HTML/JSON raporunda" : null;
            // Dashboard is always generated, even if SQL Server is not configured.
            HtmlDashboard.record(scenario.getName(), scenario.getStatus().name(), elapsed,
                screenshot == null ? null : Path.of("screenshots", Path.of(screenshot).getFileName().toString()).toString());
            SqlTestLogger.log(scenario.getName(), scenario.getStatus().name(), start, end, error, screenshot);
        } catch (java.io.IOException e) {
            throw new IllegalStateException("Rapor veya ekran görüntüsü yazılamadı", e);
        } finally {
            if (driver != null) driver.quit();
            TestContext.DRIVER.remove(); TestContext.START.remove(); TestContext.SELECTED_PRODUCT.remove();
        }
    }
}
