package com.yusuf.qa.steps;

import com.yusuf.qa.pages.SearchPage;
import com.yusuf.qa.support.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import java.util.List;
import java.util.regex.Pattern;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SearchSteps {
    private SearchPage search;
    private String query;

    private SearchPage page() {
        if (search == null) {
            if (TestContext.driver() == null) throw new IllegalStateException("Tarayıcı başlatılmadı");
            search = new SearchPage(TestContext.driver(), System.getProperty("qa.site", "Trendyol"));
        }
        return search;
    }

    @Given("I have opened the home page")
    public void openHome() {
        page().open();
    }

    @Then("I verify the search field is visible and enabled")
    public void verifySearchField() {
        assertTrue("Arama alanı kullanılamıyor", page().searchFieldAvailable());
    }

    @When("I click the search field")
    public void clickSearch() {
        page().clickSearchField();
    }

    @When("I enter {string} into the search field")
    public void enterQuery(String query) {
        this.query = query;
        page().enterSearchText(query);
    }

    @When("I press Enter")
    public void submitSearch() {
        page().pressEnter();
    }

    @Then("I verify the search results page is displayed")
    public void verifyResultsPage() {
        assertTrue("Arama sonuç sayfası açılmadı", page().resultsPageDisplayed());
        assertTrue("Arama başlığı ve ürünler doğrulanamadı", page().resultsVisible(query));
    }

    @Then("I verify the search query is {string}")
    public void verifyQuery(String expected) {
        assertTrue("URL arama sorgusu eşleşmiyor: " + expected, page().queryMatches(expected));
    }

    @Then("I verify product cards are visible")
    public void verifyCards() {
        assertFalse("Görünür ürün kartı bulunamadı", page().visibleProductNames().isEmpty());
    }

    @Then("I verify the visible products satisfy the configured relevance criteria")
    public void verifyRelevance() {
        Pattern criteria = Pattern.compile(System.getProperty("qa.search.relevance", "(?iu)\\b(mouse|fare(?:si|ler(?:i)?)?)\\b"));
        List<String> sample = page().visibleProductNames();
        assertTrue("İlk görünür ürünler alaka ölçütünü karşılamıyor: " + sample,
                sample.stream().allMatch(name -> criteria.matcher(name).find()));
    }
}
