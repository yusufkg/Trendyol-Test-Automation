package com.yusuf.qa.playwright;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class SearchSmokeTest {
    @Test
    public void displaysSearchQueryAfterClick() {
        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.chromium().launch()) {
            Page page = browser.newPage();
            page.setContent("""
                <label for="search">Search</label>
                <input id="search">
                <button id="search-button">Search</button>
                <output id="result"></output>
                <script>
                    document.querySelector('#search-button').addEventListener('click', () => {
                        const query = document.querySelector('#search').value;
                        document.querySelector('#result').textContent = 'Search: ' + query;
                    });
                </script>
                """);

            page.locator("xpath=//input[@id='search']").fill("mouse");
            page.locator("xpath=//button[@id='search-button']").click();
            assertThat(page.locator("xpath=//output[@id='result']")).hasText("Search: mouse");
        }
    }
}
