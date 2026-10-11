package com.yusuf.qa.pages;
import javax.swing.JOptionPane;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;
import java.util.regex.*;

/** Live Trendyol DOM changes. Fail explicitly rather than silently choosing an unrelated item. */
public class TrendyolPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final By cards = By.xpath("//*[@data-testid='product-card' or contains(concat(' ',normalize-space(@class),' '),' p-card-wrppr ') or contains(concat(' ',normalize-space(@class),' '),' product-card ')]");
    private String chosenTitle;
    private BigDecimal chosenPrice;

    public TrendyolPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(25));
    }
    public void open() { driver.get("https://www.trendyol.com/"); }

    public void waitForManualLogin() {

        JOptionPane.showMessageDialog(
                null,
                "Trendyol hesabına giriş yap.\n" +
                        "SMS veya güvenlik doğrulamasını tamamla.\n\n" +
                        "Giriş tamamlandıktan sonra OK butonuna bas.",
                "Trendyol Manuel Giriş",
                JOptionPane.INFORMATION_MESSAGE
        );

        // Kullanıcı onayından sonra tarayıcı bağlantısını kontrol et.
        String currentUrl = driver.getCurrentUrl();

        System.out.println(
                "Manuel giriş onaylandı. Mevcut URL: " + currentUrl
        );
    }

    public void search(String text) {
        WebElement field = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@data-testid='suggestion' or contains(@placeholder,'Ara') or @type='search']")));
        field.clear(); field.sendKeys(text, Keys.ENTER);
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(cards));
    }

    /** XPath locators; confirm against current Trendyol DOM before live use. */
    public void sortCheapest() {
        WebElement sortMenu = firstClickable(
            "//*[@data-testid='sort-dropdown']", "//button[contains(@aria-label,'Sıralama')]",
            "//*[contains(@class,'sort')]//button", "//*[contains(@class,'sort')]//*[@role='button']"
        );
        sortMenu.click();
        WebElement ascending = firstClickable(
            "//*[@data-testid='sort-price-asc']", "//*[@data-value='PRICE_ASC']",
            "//*[@value='PRICE_ASC']", "//*[@data-sort='price_asc']",
            "//*[contains(@class,'sort')]//*[contains(@data-testid,'price')]"
        );
        String label = ascending.getText().toLowerCase(Locale.ROOT);
        String marker = (ascending.getAttribute("outerHTML") == null ? "" : ascending.getAttribute("outerHTML")).toLowerCase(Locale.ROOT);
        if (!(label.contains("düşük") || label.contains("artan") || label.contains("ucuz")
                || marker.contains("price_asc") || marker.contains("price-asc"))) {
            throw new AssertionError("Artan fiyat sıralaması XPath locator'ı doğrulanamadı: " + label);
        }
        ascending.click();
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(cards));
    }

    private WebElement firstClickable(String... selectors) {
        for (String selector : selectors) {
            for (WebElement element : driver.findElements(By.xpath(selector))) {
                try {
                    if (element.isDisplayed() && element.isEnabled()) return element;
                } catch (StaleElementReferenceException ignored) { }
            }
        }
        throw new org.openqa.selenium.NoSuchElementException("XPath seçicilerinden hiçbiri tıklanabilir element bulamadı: " + Arrays.toString(selectors));
    }

    public String addCheapestToCart() {
        List<WebElement> visible = driver.findElements(cards);
        WebElement cheapestCard = null;
        BigDecimal min = null;
        List<BigDecimal> prices = new ArrayList<>();
        for (WebElement card : visible) {
            if (!card.isDisplayed()) continue;
            String title = card.getText();
            if (!isPhone(title)) continue;
            BigDecimal price = extractPrice(card);
            if (price == null) throw new AssertionError("Samsung S20 kartında okunabilir fiyat yok: " + title);
            prices.add(price);
            if (min == null || price.compareTo(min) < 0) {
                min = price; cheapestCard = card; chosenTitle = title; chosenPrice = price;
            }
        }
        if (cheapestCard == null) throw new AssertionError("Görünür sonuçlarda fiyatı okunabilen Samsung S20 telefon bulunamadı. Stok veya locator değişmiş olabilir.");
        // Assert visible phone prices are sorted ascending; fail if the UI sort did not take effect.
        for (int i = 1; i < prices.size(); i++) {
            if (prices.get(i).compareTo(prices.get(i-1)) < 0)
                throw new AssertionError("Görünür Samsung S20 fiyatları artan sırada değil: " + prices);
        }
        System.out.println("Seçilen ürün: " + chosenTitle + " | fiyat: " + chosenPrice + " TL");
        cheapestCard.click();
        String original = driver.getWindowHandle();
        wait.until(d -> d.getWindowHandles().size() > 1 || !d.getCurrentUrl().contains("sr?"));
        for (String handle : driver.getWindowHandles()) if (!handle.equals(original)) driver.switchTo().window(handle);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class,'add-to-basket') or @data-testid='add-to-basket'] | //*[contains(@class,'add-to-basket-button-text')]"))).click();
        return chosenTitle;
    }

    public boolean cartShowsItem() {
        driver.get("https://www.trendyol.com/sepetim");
        WebElement body = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body")));
        String text = body.getText().toLowerCase(Locale.ROOT);
        return text.contains("samsung") && (text.contains("s20") || text.contains("s 20"));
    }

    private static boolean isPhone(String text) {
        String s = text.toLowerCase(Locale.ROOT);
        return s.contains("samsung") && (s.contains("s20") || s.contains("s 20"))
            && !s.matches("(?s).*(kılıf|kilif|ekran koruyucu|şarj kablosu|sarj kablosu|kapak|cam koruyucu).*");
    }

    /** Parses a displayed Turkish price such as 12.499,90 TL. */
    static BigDecimal parseTurkishPrice(String raw) {
        if (raw == null) return null;
        Matcher m = Pattern.compile("([0-9][0-9.,\\s]*[0-9]|[0-9])\\s*(?:TL|₺)", Pattern.CASE_INSENSITIVE).matcher(raw);
        if (!m.find()) return null;
        String s = m.group(1).replaceAll("\\s", "");
        int comma = s.lastIndexOf(',');
        if (comma >= 0) s = s.replace(".", "").replace(',', '.');
        else if (s.matches("\\d{1,3}(\\.\\d{3})+")) s = s.replace(".", "");
        try { return new BigDecimal(s); } catch (NumberFormatException e) { return null; }
    }

    private BigDecimal extractPrice(WebElement card) {
        for (By selector : List.of(By.xpath(".//*[contains(concat(' ',normalize-space(@class),' '),' prc-dsc ')]"), By.xpath(".//*[contains(concat(' ',normalize-space(@class),' '),' price-promotion-container ')]"),
                By.xpath(".//*[@data-testid='price-current-price']"), By.xpath(".//*[contains(concat(' ',normalize-space(@class),' '),' price ')]"))) {
            for (WebElement el : card.findElements(selector)) {
                BigDecimal price = parseTurkishPrice(el.getText());
                if (price != null) return price;
            }
        }
        return parseTurkishPrice(card.getText());
    }
}
