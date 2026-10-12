package com.yusuf.qa.support;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class TestContext {
    public static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
    public static WebDriver driver() { return DRIVER.get(); }
    public static void open() {
        WebDriver driver = new ChromeDriver(); driver.manage().window().maximize(); DRIVER.set(driver);
    }
}
