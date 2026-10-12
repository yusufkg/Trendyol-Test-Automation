package com.yusuf.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class HomePage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final SearchPage search;
    private final String homeUrl;
    private final By logo;
    private final By searchField;
    private final By categoryMenu;

    // XPath seçicileri bu sınıfta tutulur. Site değişirse burada güncelle.
    private final By errorMessage = By.xpath(
        "//*[@role='alert' or @data-testid='error-message'][" +
        "contains(., 'Bir hata oluştu') or contains(., 'Something went wrong') " +
        "or contains(., 'Beklenmeyen bir hata')]");

    public HomePage(WebDriver driver, String site) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        search = new SearchPage(driver, site);
        if (site.equals("Trendyol")) {
            homeUrl = "https://www.trendyol.com/";
            logo = By.xpath("//a[@data-testid='navigation-logo-component']");
            searchField = By.xpath("//input[@data-testid='browsing-search-input' or @data-testid='suggestion' or @type='search']");
            categoryMenu = By.xpath("//nav//a | //*[@data-testid='navigation']//a | //*[@id='navigation']//a");
        } else if (site.equals("Hepsiburada")) {
            homeUrl = "https://www.hepsiburada.com/";
            logo = By.xpath("//a[@href='/']//*[local-name()='svg' or self::img] | //img[contains(@alt, 'Hepsiburada')]");
            searchField = By.xpath("//input[@data-test-id='search-bar-input'] | //button[@data-testid='suggestion-placeholder']");
            categoryMenu = By.xpath("//nav//a | //button[contains(., 'Kategoriler')] | //*[@role='button'][contains(., 'Kategoriler')]");
        } else {
            throw new IllegalArgumentException("Desteklenmeyen site: " + site);
        }
    }

    public void open() {
        driver.get(homeUrl);
        search.assertAccessible();
        search.dismissOverlays();
    }

    public boolean isReady() {
        return wait.until(d -> {
            search.assertAccessible();
            return "complete".equals(((JavascriptExecutor) d).executeScript("return document.readyState"))
                && d.findElements(By.xpath("//body")).stream().anyMatch(WebElement::isDisplayed)
                && java.net.URI.create(homeUrl).getHost().equals(java.net.URI.create(d.getCurrentUrl()).getHost());
        });
    }

    public boolean isLogoVisible() { return isVisible(logo); }
    public boolean isSearchFieldVisible() { return isVisible(searchField); }
    public boolean isCategoryMenuVisible() { return isVisible(categoryMenu); }

    public boolean hasNoApplicationError() {
        search.assertAccessible();
        return driver.findElements(errorMessage).stream().noneMatch(WebElement::isDisplayed);
    }

    private boolean isVisible(By locator) {
        return wait.until(d -> {
            search.dismissOverlays();
            return d.findElements(locator).stream().anyMatch(WebElement::isDisplayed);
        });
    }
}
