package com.yusuf.qa.pages;

import com.yusuf.qa.pricing.TurkishPrice;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ProductDetailPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final By title = By.cssSelector("h1[data-testid='product-title'], h1#product-name, h1");
    private final By price = By.cssSelector("[data-testid='lowest-price'] .discounted, [data-testid='current-price'], .prc-dsc, #offering-price, [data-test-id='price-current-price']");
    private final By images = By.cssSelector(".product-image-gallery-carousel img[data-testid='image'], #product-image img, #productImage, .product-image img, img[data-test-id='product-image']");

    public ProductDetailPage(WebDriver driver) {
        this.driver = driver; wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }
    public boolean isDisplayed(SelectedProduct selected) {
        return wait.until(d -> SelectedProduct.idFromUrl(d.getCurrentUrl()).equals(selected.id())
            && d.findElements(title).stream().anyMatch(e -> e.isDisplayed() && !e.getText().isBlank()));
    }
    public boolean identityAndNameMatch(SelectedProduct selected) {
        return wait.until(d -> SelectedProduct.idFromUrl(d.getCurrentUrl()).equals(selected.id())
            && d.findElements(title).stream().anyMatch(e -> e.isDisplayed()
                && SelectedProduct.normalizedName(e.getText()).equals(SelectedProduct.normalizedName(selected.name()))));
    }
    public boolean imageAndPriceVisible() {
        return wait.until(d -> {
            boolean imageLoaded = d.findElements(images).stream().anyMatch(e -> e.isDisplayed()
                && "true".equals(e.getDomProperty("complete")) && Integer.parseInt(e.getDomProperty("naturalWidth")) > 0);
            boolean priceVisible = d.findElements(price).stream().anyMatch(e -> {
                var value = TurkishPrice.parse(e.getText());
                return e.isDisplayed() && value != null && value.signum() > 0;
            });
            return imageLoaded && priceVisible;
        });
    }
}
