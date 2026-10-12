package com.yusuf.qa.support;

import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

public class Hooks {
    @After public void after(Scenario scenario) {
        WebDriver driver = TestContext.driver();
        try {
            if (driver != null) driver.quit();
        } catch (NoSuchSessionException e) {
            scenario.log("Test tarayıcısının oturumu zaten kapanmış.");
        } catch (WebDriverException e) {
            if (!scenario.isFailed()) throw e;
            scenario.log("Tarayıcı kapatma sırasında ek hata: " + e.getMessage());
        } finally {
            TestContext.DRIVER.remove();
        }
    }
}