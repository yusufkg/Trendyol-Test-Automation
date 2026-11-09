package com.yusuf.qa.playwright;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.assertions.LocatorAssertions;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class HomePageIT {
    @Test
    public void tc001HomePageDisplaysNavigationAndSearch() {
        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.chromium().launch()) {
            Page page = browser.newPage();
            page.setDefaultTimeout(30000);
            String navigationStatus = "Navigation did not return an HTTP response";
            try {
                Response response = page.navigate("https://www.trendyol.com/");
                assertNotNull(navigationStatus, response);
                navigationStatus = "HTTP " + response.status();
                assertTrue("Home page returned " + navigationStatus, response.ok());
                assertThat(page).hasURL("https://www.trendyol.com/");
                assertThat(page.locator("xpath=//a[@data-testid='navigation-logo-component']").first())
                    .isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(30000));
                assertThat(page.locator("xpath=//button[@data-testid='suggestion-placeholder'] | //input[@data-testid='browsing-search-input' or @data-testid='suggestion' or @type='search']").first()).isVisible();
                assertThat(page.locator("xpath=//*[@data-testid='sections-wrapper']//a[@data-testid='section-item']").first()).isVisible();
                assertThat(page.locator("xpath=//*[@role='alert' or @data-testid='error-message'][contains(., 'Bir hata oluştu') or contains(., 'Something went wrong') or contains(., 'Beklenmeyen bir hata')]")).hasCount(0);
            } catch (AssertionError | RuntimeException failure) {
                try {
                    Path evidenceDirectory = Files.createDirectories(Path.of("target/playwright-live"));
                    Files.writeString(evidenceDirectory.resolve("navigation.txt"),
                        navigationStatus + System.lineSeparator() + "URL: " + page.url());
                    Files.writeString(evidenceDirectory.resolve("home.html"), page.content());
                    page.screenshot(new Page.ScreenshotOptions()
                        .setPath(evidenceDirectory.resolve("home.png")).setTimeout(5000));
                } catch (IOException | RuntimeException evidenceFailure) {
                    failure.addSuppressed(evidenceFailure);
                }
                throw failure;
            }
        }
    }
}
