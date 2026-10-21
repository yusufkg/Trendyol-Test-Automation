package com.yusuf.qa.api.tests;

import com.yusuf.qa.api.fixture.LocalApiServer;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

public class ProductApiTest {
    private LocalApiServer server;
    private RequestSpecification request;

    @BeforeMethod
    public void startApi() throws IOException {
        server = new LocalApiServer();
        request = new RequestSpecBuilder()
                .setBaseUri("http://127.0.0.1")
                .setPort(server.port())
                .setAccept(ContentType.JSON)
                .build();
    }

    @AfterMethod(alwaysRun = true)
    public void stopApi() {
        if (server != null) {
            server.close();
        }
    }

    @Test(description = "API-001 - GET products returns the expected product list")
    public void listProducts() {
        given().spec(request)
                .when().get("/products")
                .then().log().ifValidationFails()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("total", equalTo(1))
                .body("products", hasSize(1))
                .body("products[0].id", equalTo(1))
                .body("products[0].name", equalTo("Kablosuz Mouse"))
                .body("products[0].price", equalTo(499.90f));
    }
}
