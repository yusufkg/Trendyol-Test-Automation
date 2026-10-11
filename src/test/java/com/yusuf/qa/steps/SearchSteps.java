package com.yusuf.qa.steps;

import com.yusuf.qa.pages.SearchPage;
import com.yusuf.qa.support.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import static org.junit.Assert.assertTrue;

public class SearchSteps {
    private SearchPage page;
    @Given("I open the {string} home page") public void open(String site) {
        TestContext.open(); page = new SearchPage(TestContext.driver(), site); page.open();
    }
    @When("I click the search field") public void clickSearchField() { page.clickSearchField(); }
    @When("I enter {string} into the search field") public void enterSearchText(String query) { page.enterSearchText(query); }
    @When("I press Enter") public void pressEnter() { page.pressEnter(); }
    @Then("I verify search results for {string} are displayed") public void verify(String query) {
        assertTrue("Arama sorgusu ve ürün sonuçları görünmüyor", page.resultsVisible(query));
    }
}
