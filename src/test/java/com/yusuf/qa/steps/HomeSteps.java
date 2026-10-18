package com.yusuf.qa.steps;

import com.yusuf.qa.pages.HomePage;
import com.yusuf.qa.support.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import static org.junit.Assert.assertTrue;

public class HomeSteps {
    private HomePage homePage;

    @Given("I start a new guest session in a supported browser")
    public void startGuestSession() {
        if (TestContext.driver() == null) TestContext.open();
        String site = System.getProperty("qa.site", "Trendyol");
        homePage = new HomePage(TestContext.driver(), site);
    }

    @When("I open the home page")
    public void openHomePage() { homePage.open(); }

    @Then("I verify the home page is ready to use")
    public void verifyHomePageReady() {
        assertTrue("Ana sayfa yüklenmedi", homePage.isReady());
    }

    @Then("I verify the site logo is visible")
    public void verifyLogo() {
        assertTrue("Site logosu görünmüyor", homePage.isLogoVisible());
    }

    @Then("I verify the search field is visible")
    public void verifySearchField() {
        assertTrue("Arama alanı görünmüyor", homePage.isSearchFieldVisible());
    }

    @Then("I verify the main category menu is visible")
    public void verifyCategoryMenu() {
        assertTrue("Kategori menüsü görünmüyor", homePage.isCategoryMenuVisible());
    }

    @Then("I verify no application error message is displayed")
    public void verifyNoApplicationError() {
        assertTrue("Uygulama hata mesajı görüntülendi", homePage.hasNoApplicationError());
    }
}
