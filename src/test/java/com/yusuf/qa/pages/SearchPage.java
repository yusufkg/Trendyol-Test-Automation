package com.yusuf.qa.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.Locale;
import java.util.Set;
import com.yusuf.qa.pricing.TurkishPrice;

/** Guest search only; each site's selectors are kept together. */
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
                searchInput = By.cssSelector("input[data-testid='browsing-search-input'], input[data-testid='suggestion'], input[type='search']");
                productCards = By.cssSelector("[data-testid='product-card'], .p-card-wrppr");
            }
            case "Hepsiburada" -> {
                home = "https://www.hepsiburada.com/";
                searchInput = By.cssSelector("input[data-test-id='search-bar-input']:not([class*='initialComponent'])");
                productCards = By.cssSelector("main article");
            }
            default -> throw new IllegalArgumentException("Desteklenmeyen site: " + site);
        }
    }

    public void open() { driver.get(home); assertAccessible(); dismissOverlays(); }

    public void clickSearchField() { searchField().click(); }

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
                for (WebElement button : d.findElements(By.cssSelector(
                        "button[data-testid='suggestion-placeholder'], input[data-test-id='search-bar-input'][class*='initialComponent']"))) {
                    // The placeholder input is not editable; its wrapper activates the real search component.
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
                for (WebElement placeholder : d.findElements(By.cssSelector(
                        "button[data-testid='suggestion-placeholder'], input[data-test-id='search-bar-input'][class*='initialComponent']"))) {
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
                boolean queryShown = d.findElements(By.tagName("h1")).stream().anyMatch(e ->
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
        return wait.until(d -> {
            assertAccessible(); dismissOverlays();
            try {
                for (WebElement card : d.findElements(productCards)) {
                    if (!card.isDisplayed()) continue;
                    var headings = card.findElements(By.cssSelector("h2"));
                    if (headings.isEmpty()) continue;
                    String name = headings.get(0).getText();
                    var brands = card.findElements(By.cssSelector(".product-brand"));
                    var names = card.findElements(By.cssSelector(".product-name"));
                    if (!brands.isEmpty() && !names.isEmpty()) name = brands.get(0).getText() + " " + names.get(0).getText();
                    WebElement link = card.getTagName().equalsIgnoreCase("a") ? card :
                        card.findElement(By.cssSelector("h2 a[href]"));
                    String url = link.getAttribute("href");
                    for (WebElement priceElement : card.findElements(By.cssSelector(
                            "[data-testid='sale-price'], [data-testid='price-value'], [data-test-id^='final-price-'], .prc-dsc"))) {
                        if (!priceElement.isDisplayed()) continue;
                        var price = TurkishPrice.parse(priceElement.getText());
                        if (price != null && price.signum() > 0 && !name.isBlank())
                            return new SelectedProduct(SelectedProduct.idFromUrl(url), name, price, url);
                    }
                }
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
            for (WebElement candidate : d.findElements(By.cssSelector("a[href]"))) {
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

    private void dismissOverlays() {
        for (WebElement button : driver.findElements(By.cssSelector(
                "#onetrust-reject-all-handler, .gender-modal-section .modal-section-close, [data-testid='onboarding-overlay'][aria-label='Dismiss']"))) {
            try { if (button.isDisplayed() && button.isEnabled()) button.click(); }
            catch (StaleElementReferenceException | ElementClickInterceptedException ignored) { }
        }
    }

    private void assertAccessible() {
        for (WebElement body : driver.findElements(By.tagName("body"))) {
            String text = body.getText();
            if (text.contains("Bağlantınız güvensiz") && text.contains("erişemiyorsunuz"))
                throw new AssertionError("Siteye erişim güvenlik kontrolü tarafından engellendi.");
        }
    }
}
