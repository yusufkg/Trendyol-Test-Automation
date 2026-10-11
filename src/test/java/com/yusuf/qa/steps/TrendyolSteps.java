package com.yusuf.qa.steps;

import com.yusuf.qa.pages.TrendyolPage;
import com.yusuf.qa.support.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import static org.junit.Assert.assertTrue;

public class TrendyolSteps {
    private TrendyolPage page;
    private TrendyolPage page() {
        if (page == null) page = new TrendyolPage(TestContext.driver());
        return page;
    }
    @Given("Trendyol ana sayfası açılır") public void open() { TestContext.open(); page().open(); }
    @When("kullanıcı tarayıcıda hesabına girişini tamamlar") public void login() { page().waitForManualLogin(); }
    @When("{string} ürünü aranır") public void search(String name) { page().search(name); }
    @When("ürünler en düşük fiyata sıralanır") public void sort() { page().sortCheapest(); }
    @When("en düşük fiyatlı uygun ürün sepete eklenir") public void add() { TestContext.SELECTED_PRODUCT.set(page().addCheapestToCart()); }
    @Then("sepette Samsung S20 ürünü görünür") public void verify() { assertTrue("Samsung S20 sepette bulunamadı", page().cartShowsItem()); }
}
