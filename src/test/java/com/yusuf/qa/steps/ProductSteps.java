package com.yusuf.qa.steps;

import com.yusuf.qa.pages.*;
import com.yusuf.qa.support.TestContext;
import io.cucumber.java.en.*;
import static org.junit.Assert.*;

public class ProductSteps {
    private SearchPage search;
    private SelectedProduct selected;
    private ProductDetailPage detail;

    @Given("I have configured the target site and scenario test data")
    public void configure() {
        String site = System.getProperty("qa.site", "Trendyol");
        if (!site.equals("Trendyol") && !site.equals("Hepsiburada")) throw new IllegalArgumentException("Desteklenmeyen site: " + site);
        TestContext.open(); search = new SearchPage(TestContext.driver(), site); search.open();
        System.out.println("Hedef site: " + site);
    }
    @Given("I am not signed in") public void verifyGuest() { assertTrue("Misafir giriş durumu doğrulanamadı", search.guestUserVisible()); }
    @Given("I have searched for {string}") public void search(String query) {
        search.clickSearchField(); search.enterSearchText(query); search.pressEnter();
        assertTrue(search.resultsVisible(query));
    }
    @Given("I have recorded a suitable product card identity name and price") public void record() {
        selected = search.recordFirstProduct();
        System.out.println("Seçilen ürün: " + selected.id() + " | " + selected.name() + " | " + selected.price() + " TL");
    }
    @When("I click that product card") public void click() { search.clickProduct(selected); }
    @When("I switch to its product tab if a new tab opens") public void switchTab() {
        search.switchToProductTab(); detail = new ProductDetailPage(TestContext.driver());
    }
    @Then("I verify the selected product detail page is displayed") public void verifyDetail() { assertTrue(detail.isDisplayed(selected)); }
    @Then("I verify the product identity and name match the selected card") public void verifyIdentity() { assertTrue("Ürün kimliği veya adı eşleşmedi", detail.identityAndNameMatch(selected)); }
    @Then("I verify the product image and price are visible") public void verifyFields() { assertTrue("Yüklenmiş ürün görseli veya fiyat görünmüyor", detail.imageAndPriceVisible()); }
}
