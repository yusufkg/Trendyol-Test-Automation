package com.yusuf.qa.pages;

import com.yusuf.qa.pricing.TurkishPrice;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ProductDetailPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final By title = By.xpath("//h1");
    private final By price = By.xpath("//*[@data-testid='lowest-price']//*[contains(concat(' ', normalize-space(@class), ' '), ' discounted ')] | //*[@data-testid='current-price' or contains(concat(' ', normalize-space(@class), ' '), ' prc-dsc ') or @id='offering-price' or @data-test-id='price-current-price']");
    private final By images = By.xpath("//*[contains(concat(' ', normalize-space(@class), ' '), ' product-image-gallery-carousel ')]//img[@data-testid='image'] | //*[@id='product-image']//img | //*[@id='productImage'] | //*[contains(concat(' ', normalize-space(@class), ' '), ' product-image ')]//img | //img[@data-test-id='product-image']");

    public ProductDetailPage(WebDriver driver) {
        this.driver = driver; wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }
    public boolean isDisplayed(SelectedProduct selected) {
        return wait.until(d -> productUrlMatches(selected)
            && d.findElements(title).stream().anyMatch(e -> e.isDisplayed() && !e.getText().isBlank()));
    }
    public boolean identityAndNameMatch(SelectedProduct selected) {
        return wait.until(d -> productUrlMatches(selected)
            && d.findElements(title).stream().anyMatch(e -> e.isDisplayed()
                && SelectedProduct.normalizedName(e.getText()).equals(SelectedProduct.normalizedName(selected.name()))));
    }
    public boolean imageAndPriceVisible() {
        return wait.until(d -> {
            boolean imageLoaded = d.findElements(images).stream().anyMatch(e -> e.isDisplayed()
                && "true".equals(e.getDomProperty("complete")) && loadedWidth(e));
            boolean priceVisible = d.findElements(price).stream().anyMatch(e -> {
                var value = TurkishPrice.parse(e.getText());
                return e.isDisplayed() && value != null && value.signum() > 0;
            });
            return imageLoaded && priceVisible;
        });
    }
    private boolean productUrlMatches(SelectedProduct selected) {
        try { return SelectedProduct.idFromUrl(driver.getCurrentUrl()).equals(selected.id()); }
        catch (IllegalArgumentException ignored) { return false; }
    }
    private boolean loadedWidth(WebElement image) {
        String width = image.getDomProperty("naturalWidth");
        return width != null && width.matches("[0-9]+") && Integer.parseInt(width) > 0;
    }
}
