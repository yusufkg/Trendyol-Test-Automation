package com.yusuf.qa.support;

import org.junit.Test;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ScenarioCleanupTest {
    private final List<String> events = new ArrayList<>();
    private final byte[] screenshot = {1, 2, 3};

    private WebDriver driver(boolean screenshotFails, boolean quitFails) {
        return (WebDriver) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class<?>[]{WebDriver.class, TakesScreenshot.class}, (proxy, method, args) -> {
                    switch (method.getName()) {
                        case "getScreenshotAs" -> {
                            events.add("capture");
                            if (screenshotFails) throw new WebDriverException("capture failed");
                            return screenshot;
                        }
                        case "quit" -> {
                            events.add("quit");
                            if (quitFails) throw new WebDriverException("quit failed");
                            return null;
                        }
                        default -> throw new UnsupportedOperationException(method.getName());
                    }
                });
    }

    private void attach(byte[] data, String mediaType, String name) {
        assertArrayEquals(screenshot, data);
        assertEquals("image/png", mediaType);
        assertEquals("failure-screenshot", name);
        events.add("attach");
    }

    @Test
    public void failedScenarioAttachesScreenshotBeforeClosingBrowser() {
        ScenarioCleanup.finish(driver(false, false), true, this::attach, message -> events.add("log"));
        assertEquals(List.of("capture", "attach", "quit"), events);
    }

    @Test
    public void passedScenarioDoesNotCaptureScreenshot() {
        ScenarioCleanup.finish(driver(false, false), false, this::attach, message -> events.add("log"));
        assertEquals(List.of("quit"), events);
    }

    @Test
    public void failedCaptureDoesNotPreventCleanup() {
        ScenarioCleanup.finish(driver(true, false), true, this::attach, message -> events.add("log"));
        assertEquals(List.of("capture", "log", "quit"), events);
    }

    @Test
    public void attachmentFailureStillClosesBrowser() {
        ScenarioCleanup.finish(driver(false, false), true,
                (data, type, name) -> { throw new IllegalStateException("attachment failed"); },
                message -> events.add("log"));
        assertEquals(List.of("capture", "log", "quit"), events);
    }

    @Test
    public void quitFailureDoesNotReplaceExistingScenarioFailure() {
        ScenarioCleanup.finish(driver(false, true), true, this::attach, message -> events.add("log"));
        assertEquals(List.of("capture", "attach", "quit", "log"), events);
    }

    @Test
    public void quitFailureAfterPassedScenarioIsReported() {
        assertThrows(WebDriverException.class, () -> ScenarioCleanup.finish(
                driver(false, true), false, this::attach, message -> events.add("log")));
        assertEquals(List.of("quit"), events);
    }
}
