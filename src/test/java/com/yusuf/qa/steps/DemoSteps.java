package com.yusuf.qa.steps;

import com.yusuf.qa.pages.DemoLoginPage;
import com.yusuf.qa.support.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import static org.junit.Assert.assertTrue;

public class DemoSteps {
    @Given("I open the demo login page") public void open() { TestContext.open(); new DemoLoginPage(TestContext.driver()).open(); }
    @When("I sign in with the valid demo account") public void login() { new DemoLoginPage(TestContext.driver()).login("tomsmith", "SuperSecretPassword!"); }
    @Then("I verify the login success message is displayed") public void verify() { assertTrue(new DemoLoginPage(TestContext.driver()).success()); }
}
