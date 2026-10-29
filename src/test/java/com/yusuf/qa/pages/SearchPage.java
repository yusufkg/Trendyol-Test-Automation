package com.yusuf.qa.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.util.Locale;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import com.yusuf.qa.pricing.TurkishPrice;

public class SearchPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final String home;
    private final By searchInput;
    private final By productCards;
    private String sourceWindow;
    private Set<String> sourceWindows;
    private String sourceUrl;

    public SearchPage(WebDriver driver, String site) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        switch (site) {
            case "Trendyol" -> {
                home = "https://www.trendyol.com/";
                searchInput = By.xpath("//input[@data-testid='browsing-search-input' or @data-testid='suggestion' or @type='search']");
                productCards = By.xpath("//*[@data-testid='product-card' or contains(concat(' ', normalize-space(@class), ' '), ' p-card-wrppr ')]");
            }
            case "Hepsiburada" -> {
                home = "https://www.hepsiburada.com/";
                searchInput = By.xpath("//input[@data-test-id='search-bar-input' and not(contains(@class, 'initialComponent'))]");
                productCards = By.xpath("//main//article");
            }
            default -> throw new IllegalArgumentException("Desteklenmeyen site: " + site);
        }
    }

    public void open() { driver.get(home); assertAccessible(); dismissOverlays(); }

    public void clickSearchField() {
        wait.until(d -> {
            try {
                searchField().click();
                return true;
            } catch (ElementClickInterceptedException | StaleElementReferenceException ignored) {
                return false;
            }
        });
    }

    public boolean searchFieldAvailable() {
        return searchField().isEnabled();
    }

    public boolean resultsPageDisplayed() {
        return wait.until(d -> {
            assertAccessible();
            return URI.create(d.getCurrentUrl()).getPath().equals(
                    home.contains("trendyol") ? "/sr" : "/ara");
        });
    }

    public boolean queryMatches(String expected) {
        return wait.until(d -> {
            String query = URI.create(d.getCurrentUrl()).getRawQuery();
            if (query == null) return false;
            return Arrays.stream(query.split("&"))
                    .map(part -> part.split("=", 2))
                    .anyMatch(pair -> pair.length == 2 && pair[0].equals("q")
                            && URLDecoder.decode(pair[1], StandardCharsets.UTF_8).equals(expected));
        });
    }

    public List<String> visibleProductNames() {
        return wait.until(d -> {
            assertAccessible();
            dismissOverlays();
            try {
                List<String> names = d.findElements(productCards).stream()
                        .filter(WebElement::isDisplayed)
                        .flatMap(card -> card.findElements(By.xpath(".//h2")).stream())
                        .filter(WebElement::isDisplayed)
                        .map(WebElement::getText)
                        .filter(name -> !name.isBlank())
                        .limit(5)
                        .toList();
                return names.isEmpty() ? null : names;
            } catch (StaleElementReferenceException ignored) {
                return null;
            }
        });
    }

    public void enterSearchText(String query) {
        WebElement input = wait.until(d -> {
            assertAccessible();
            dismissOverlays();
            try {
                for (WebElement element : d.findElements(searchInput)) {
                    if (!element.isDisplayed() || !element.isEnabled()) continue;
                    element.sendKeys(Keys.chord(Keys.CONTROL, "a"), query);
                    if (query.equals(element.getDomProperty("value"))) return element;
                }
                for (WebElement button : d.findElements(By.xpath(
                        "//button[@data-testid='suggestion-placeholder'] | //input[@data-test-id='search-bar-input' and contains(@class, 'initialComponent')]"))) {
                    if ("input".equalsIgnoreCase(button.getTagName())) button = button.findElement(By.xpath(".."));
                    if (button.isDisplayed() && button.isEnabled()) button.click();
                }
            } catch (StaleElementReferenceException | InvalidElementStateException ignored) { }
            return null;
        });
    }

    public void pressEnter() { searchField().sendKeys(Keys.ENTER); }

    private WebElement searchField() {
        return wait.until(d -> {
            assertAccessible(); dismissOverlays();
            try {
                for (WebElement field : d.findElements(searchInput))
                    if (field.isDisplayed() && field.isEnabled()) return field;
                for (WebElement placeholder : d.findElements(By.xpath(
                        "//button[@data-testid='suggestion-placeholder'] | //input[@data-test-id='search-bar-input' and contains(@class, 'initialComponent')]"))) {
                    if ("input".equalsIgnoreCase(placeholder.getTagName())) placeholder = placeholder.findElement(By.xpath(".."));
                    if (placeholder.isDisplayed() && placeholder.isEnabled()) placeholder.click();
                }
            } catch (StaleElementReferenceException | InvalidElementStateException ignored) { }
            return null;
        });
    }

    public boolean resultsVisible(String query) {
        String expected = query.toLowerCase(Locale.forLanguageTag("tr"));
        return wait.until(d -> {
            assertAccessible();
            try {
                boolean queryShown = d.findElements(By.xpath("//h1")).stream().anyMatch(e ->
                    e.isDisplayed() && e.getText().toLowerCase(Locale.forLanguageTag("tr")).contains(expected));
                return queryShown && d.findElements(productCards).stream().anyMatch(e -> e.isDisplayed() && !e.getText().isBlank());
            } catch (StaleElementReferenceException ignored) { return false; }
        });
    }

    public boolean guestUserVisible() {
        return wait.until(d -> d.findElements(By.xpath(
            "//*[self::a or self::span or self::button][normalize-space(.)='Giriş Yap' or @aria-label='Giriş Yap']"))
            .stream().anyMatch(WebElement::isDisplayed));
    }

    public SelectedProduct recordFirstProduct() {
        return visibleProducts().get(0);
    }

    public List<SelectedProduct> visibleProducts() {
        return wait.until(d -> {
            assertAccessible(); dismissOverlays();
            try {
                List<SelectedProduct> products = new ArrayList<>();
                for (WebElement card : d.findElements(productCards)) {
                    if (!card.isDisplayed()) continue;
                    var headings = card.findElements(By.xpath(".//h2"));
                    if (headings.isEmpty()) continue;
                    String name = headings.get(0).getText();
                    var brands = card.findElements(By.xpath(".//*[contains(concat(' ', normalize-space(@class), ' '), ' product-brand ')]"));
                    var names = card.findElements(By.xpath(".//*[contains(concat(' ', normalize-space(@class), ' '), ' product-name ')]"));
                    if (!brands.isEmpty() && !names.isEmpty()) name = brands.get(0).getText() + " " + names.get(0).getText();
                    WebElement link = card.getTagName().equalsIgnoreCase("a") ? card :
                        card.findElement(By.xpath(".//h2//a[@href]"));
                    String url = link.getAttribute("href");
                    for (WebElement priceElement : card.findElements(By.xpath(
                            ".//*[@data-testid='sale-price' or @data-testid='single-price' or @data-testid='price-value' or starts-with(@data-test-id, 'final-price-') or contains(concat(' ', normalize-space(@class), ' '), ' prc-dsc ')]"))) {
                        if (!priceElement.isDisplayed()) continue;
                        var price = TurkishPrice.parse(priceElement.getText());
                        if (price != null && price.signum() > 0 && !name.isBlank()) {
                            products.add(new SelectedProduct(SelectedProduct.idFromUrl(url), name, price, url));
                            break;
                        }
                    }
                }
                if (!products.isEmpty()) return products;
            } catch (StaleElementReferenceException ignored) { }
            return null;
        });
    }

    public void clickProduct(SelectedProduct product) {
        sourceWindow = driver.getWindowHandle();
        sourceWindows = Set.copyOf(driver.getWindowHandles());
        sourceUrl = driver.getCurrentUrl();
        WebElement link = wait.until(d -> {
            dismissOverlays();
            for (WebElement candidate : d.findElements(By.xpath("//a[@href]"))) {
                if (candidate.isDisplayed() && product.url().equals(candidate.getAttribute("href"))) return candidate;
            }
            return null;
        });
        link.click();
    }

    public void switchToProductTab() {
        if (sourceWindows == null) throw new IllegalStateException("Önce ürün kartı açılmalıdır.");
        wait.until(d -> d.getWindowHandles().size() > sourceWindows.size() || !sourceUrl.equals(d.getCurrentUrl()));
        for (String handle : driver.getWindowHandles()) {
            if (!sourceWindows.contains(handle)) { driver.switchTo().window(handle); return; }
        }
        driver.switchTo().window(sourceWindow);
    }

    public void dismissOverlays() {
        for (WebElement button : driver.findElements(By.xpath(
                "//*[@id='onetrust-reject-all-handler'] | //*[contains(concat(' ', normalize-space(@class), ' '), ' gender-modal-section ')]//*[contains(concat(' ', normalize-space(@class), ' '), ' modal-section-close ')] | //*[@data-testid='onboarding-overlay' and @aria-label='Dismiss'] | //button[contains(concat(' ', normalize-space(@class), ' '), ' onboarding__default-renderer-primary-button ')]"))) {
            try {
                if (button.isDisplayed() && button.isEnabled()) {
                    boolean detailTour = String.valueOf(button.getAttribute("class")).contains("onboarding__default-renderer");
                    button.click();
                    if (detailTour) new WebDriverWait(driver, Duration.ofSeconds(5)).until(
                        ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[contains(concat(' ', normalize-space(@class), ' '), ' onboarding-tour__overlay ')]")));
                }
            }
            catch (StaleElementReferenceException | ElementClickInterceptedException ignored) { }
        }
    }

    public void assertAccessible() {
        for (WebElement body : driver.findElements(By.xpath("//body"))) {
            String text = body.getText();
            if (text.contains("Bağlantınız güvensiz") && text.contains("erişemiyorsunuz"))
                throw new IllegalStateException("Siteye erişim güvenlik kontrolü tarafından engellendi.");
        }
    }
}
