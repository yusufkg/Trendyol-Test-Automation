package com.yusuf.qa.steps;

import com.yusuf.qa.support.TestContext;
import com.yusuf.qa.pages.SearchPage;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class XPathSteps {
    private WebDriverWait waitForElement() {
        WebDriver driver = TestContext.driver();
        if (driver == null) throw new IllegalStateException("Önce tarayıcıyı ve hedef sayfayı açın.");
        return new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    @When("I click {string}")
    public void click(String xpath) {
        waitForElement().until(driver -> {
            new SearchPage(driver, System.getProperty("qa.site", "Trendyol")).dismissOverlays();
            try {
                for (var element : driver.findElements(By.xpath(xpath))) {
                    if (element.isDisplayed() && element.isEnabled()) {
                        element.click();
                        return true;
                    }
                }
            } catch (ElementClickInterceptedException | StaleElementReferenceException ignored) { }
            return false;
        });
    }

    @When("I enter {string} into xpath {string}")
    public void enter(String text, String xpath) {
        var element = waitForElement().until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        element.clear();
        element.sendKeys(text);
    }

    @Then("I verify {string} is visible")
    public void verifyVisible(String xpath) {
        waitForElement().until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
    }

    @Then("I verify the page has loaded")
    public void verifyPageLoaded() {
        waitForElement().until(driver -> "complete".equals(
            ((JavascriptExecutor) driver).executeScript("return document.readyState")));
        verifyVisible("//body");
    }

    @Then("I verify {string} is not visible")
    public void verifyNotVisible(String xpath) {
        waitForElement().until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpath)));
    }

    @Then("I verify the URL is {string}")
    public void verifyUrl(String expectedUrl) {
        waitForElement().until(ExpectedConditions.urlToBe(expectedUrl));
    }

    @Then("I verify the URL path is {string}")
    public void verifyUrlPath(String expectedPath) {
        waitForElement().until(driver -> expectedPath.equals(
            java.net.URI.create(driver.getCurrentUrl()).getPath()));
    }
}
