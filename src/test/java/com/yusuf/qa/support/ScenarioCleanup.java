package com.yusuf.qa.support;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import java.util.function.Consumer;

public final class ScenarioCleanup {
    @FunctionalInterface
    public interface AttachmentSink {
        void attach(byte[] data, String mediaType, String name);
    }

    private ScenarioCleanup() { }

    public static void finish(WebDriver driver, boolean failed, AttachmentSink sink, Consumer<String> log) {
        if (driver == null) return;
        try {
            if (failed && driver instanceof TakesScreenshot screenshot) {
                try {
                    sink.attach(screenshot.getScreenshotAs(OutputType.BYTES), "image/png", "failure-screenshot");
                } catch (RuntimeException e) {
                    log.accept("Hata ekran görüntüsü eklenemedi: " + e.getClass().getSimpleName());
                }
            }
        } finally {
            try {
                driver.quit();
            } catch (WebDriverException e) {
                if (!failed) throw e;
                log.accept("Tarayıcı kapatılırken hata: " + e.getClass().getSimpleName());
            }
        }
    }
}
