
import com.thoughtworks.gauge.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebSteps {

    private WebDriver driver;

    @Step("Chrome tarayıcısını aç")
    public void openChrome() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Step("Example sitesine git")
    public void openExample() {
        driver.get("https://example.com");
    }

    @Step("Sayfa başlığını doğrula")
    public void verifyTitle() {
        String actualTitle = driver.getTitle();

        if (!actualTitle.equals("Example Domain")) {
            throw new AssertionError(
                    "Beklenen: Example Domain, Gelen: " + actualTitle
            );
        }
    }

    @Step("Tarayıcıyı kapat")
    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }
}
