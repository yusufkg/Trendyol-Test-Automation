package com.yusuf.qa.steps;

import com.yusuf.qa.pages.CategoryMenuPage;
import com.yusuf.qa.support.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.assertTrue;

public class NavigationSteps {
    private CategoryMenuPage category;

    @Given("I have selected a menu with subcategories in the test data")
    public void selectMenu() {
        category = new CategoryMenuPage(TestContext.driver(), System.getProperty("qa.site", "Trendyol"));
    }

    @When("I open the category menu using its supported interaction")
    public void openMenu() {
        category.openMenu();
    }

    @Then("I verify the subcategory options are visible")
    public void verifyOptions() {
        assertTrue("Elbise alt kategorisi görünmüyor", category.subcategoryVisible());
    }

    @When("I click an existing subcategory")
    public void clickSubcategory() {
        category.clickSubcategory();
    }

    @Then("I verify the selected subcategory page is displayed")
    public void verifyCategory() {
        assertTrue("Alt kategori yolu, başlığı veya ürünleri doğrulanamadı", category.selectedCategoryDisplayed());
    }
}
