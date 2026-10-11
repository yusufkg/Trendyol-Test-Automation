package com.yusuf.qa.steps;

import com.yusuf.qa.pages.DemoLoginPage;
import com.yusuf.qa.support.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import static org.junit.Assert.assertTrue;

public class DemoSteps {
    @Given("demo giriş sayfası açılır") public void open() { TestContext.open(); new DemoLoginPage(TestContext.driver()).open(); }
    @When("geçerli demo hesabıyla giriş yapılır") public void login() { new DemoLoginPage(TestContext.driver()).login("tomsmith", "SuperSecretPassword!"); }
    @Then("giriş başarılı mesajı görüntülenir") public void verify() { assertTrue(new DemoLoginPage(TestContext.driver()).success()); }
}
