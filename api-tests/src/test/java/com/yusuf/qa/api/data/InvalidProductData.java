package com.yusuf.qa.api.data;

import org.testng.annotations.DataProvider;

public final class InvalidProductData {
    @DataProvider(name = "invalidProducts")
    public static Object[][] invalidProducts() {
        return new Object[][] {
                {"negative price", "{\"name\":\"Mouse\",\"price\":-10}"},
                {"zero price", "{\"name\":\"Mouse\",\"price\":0}"},
                {"blank name", "{\"name\":\"   \",\"price\":100}"},
                {"missing name", "{\"price\":100}"},
                {"missing price", "{\"name\":\"Mouse\"}"},
                {"text price", "{\"name\":\"Mouse\",\"price\":\"invalid\"}"}
        };
    }
}
