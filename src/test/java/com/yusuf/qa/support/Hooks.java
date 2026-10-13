package com.yusuf.qa.support;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import org.openqa.selenium.WebDriverException;
public class Hooks {
    @After public void after(Scenario scenario) {
        try {
            if (TestContext.driver() != null) TestContext.driver().quit();
        } catch (WebDriverException e) {
            if (!scenario.isFailed()) throw e;
            scenario.log("Tarayıcı kapatılırken hata: " + e.getMessage());
        } finally {
            TestContext.DRIVER.remove();
        }
    }
}