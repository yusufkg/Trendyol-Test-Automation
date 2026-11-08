package com.yusuf.qa.playwright;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class HomePageIT {
    @Test
    public void tc001HomePageDisplaysNavigationAndSearch() throws IOException {
        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.chromium().launch()) {
            Page page = browser.newPage();
            page.setDefaultTimeout(30000);
            page.navigate("https://www.trendyol.com/");
            Path evidenceDirectory = Files.createDirectories(Path.of("target/playwright-live"));
            Files.writeString(evidenceDirectory.resolve("home.html"), page.content());
            page.screenshot(new Page.ScreenshotOptions().setPath(evidenceDirectory.resolve("home.png")));
            assertThat(page).hasURL("https://www.trendyol.com/");
            assertThat(page.locator("xpath=//a[@data-testid='navigation-logo-component']").first()).isVisible();
            assertThat(page.locator("xpath=//button[@data-testid='suggestion-placeholder'] | //input[@data-testid='browsing-search-input' or @data-testid='suggestion' or @type='search']").first()).isVisible();
            assertThat(page.locator("xpath=//*[@data-testid='sections-wrapper']//a[@data-testid='section-item']").first()).isVisible();
            assertThat(page.locator("xpath=//*[@role='alert' or @data-testid='error-message'][contains(., 'Bir hata oluştu') or contains(., 'Something went wrong') or contains(., 'Beklenmeyen bir hata')]")).hasCount(0);
        }
    }
}
