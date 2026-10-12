package com.yusuf.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class DemoLoginPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    public DemoLoginPage(WebDriver driver) { this.driver = driver; wait = new WebDriverWait(driver, Duration.ofSeconds(15)); }
    public void open() { driver.get("https://the-internet.herokuapp.com/login"); }
    public void login(String user, String pass) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='username']"))).sendKeys(user);
        driver.findElement(By.xpath("//*[@id='password']")).sendKeys(pass);
        driver.findElement(By.xpath("//button[@type='submit']")).click();
    }
    public boolean success() { return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='flash']"))).getText().contains("You logged into a secure area!"); }
}
