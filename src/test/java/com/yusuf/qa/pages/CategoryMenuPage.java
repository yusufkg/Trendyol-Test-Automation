package com.yusuf.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URI;
import java.time.Duration;
import java.util.Locale;

public class CategoryMenuPage {
    private final WebDriverWait wait;
    private final SearchPage search;
    private final By menu = By.xpath("//button[contains(concat(' ', normalize-space(@class), ' '), ' category-tab-wrapper ')]");
    private final By subcategory = By.xpath("//*[contains(concat(' ', normalize-space(@class), ' '), ' category-content-panel ')]//a[normalize-space(.)='Elbise' and @href='/elbise-x-c56']");
    private final String expectedPath = "/elbise-x-c56";

    public CategoryMenuPage(WebDriver driver, String site) {
        if (!site.equals("Trendyol")) {
            throw new IllegalArgumentException("Kategori menüsü senaryosu yalnızca Trendyol için uygulandı");
        }
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        search = new SearchPage(driver, site);
    }

    public void openMenu() {
        wait.until(d -> {
            search.assertAccessible();
            search.dismissOverlays();
            try {
                for (WebElement button : d.findElements(menu)) {
                    if (!button.isDisplayed() || !button.isEnabled()) continue;
                    if ("true".equals(button.getDomAttribute("aria-expanded"))) return true;
                    button.click();
                }
            } catch (StaleElementReferenceException | ElementClickInterceptedException ignored) { }
            return false;
        });
    }

    public boolean subcategoryVisible() {
        return wait.until(d -> {
            search.dismissOverlays();
            try {
                return d.findElements(subcategory).stream().anyMatch(WebElement::isDisplayed);
            } catch (StaleElementReferenceException ignored) {
                return false;
            }
        });
    }

    public void clickSubcategory() {
        wait.until(d -> {
            search.dismissOverlays();
            try {
                for (WebElement link : d.findElements(subcategory)) {
                    if (!link.isDisplayed() || !link.isEnabled()) continue;
                    if (!expectedPath.equals(link.getDomAttribute("href"))) {
                        throw new IllegalStateException("Alt kategori bağlantısı beklenen yolla eşleşmiyor");
                    }
                    link.click();
                    return true;
                }
            } catch (StaleElementReferenceException | ElementClickInterceptedException ignored) { }
            return false;
        });
    }

    public boolean selectedCategoryDisplayed() {
        wait.until(d -> {
            search.assertAccessible();
            try {
                URI actual = URI.create(d.getCurrentUrl());
                return "www.trendyol.com".equals(actual.getHost()) && expectedPath.equals(actual.getPath())
                        && d.findElements(By.xpath("//h1")).stream().anyMatch(heading -> heading.isDisplayed()
                        && heading.getText().toLowerCase(Locale.forLanguageTag("tr")).contains("elbise"));
            } catch (StaleElementReferenceException ignored) {
                return false;
            }
        });
        return !search.visibleProductNames().isEmpty();
    }
}
