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
import static org.hamcrest.Matchers.greaterThan;

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

    @Test(description = "API-004 - Invalid product is rejected without changing stored products")
    public void rejectInvalidProduct() {
        Object productsBefore = given().spec(request)
                .when().get("/products")
                .then().statusCode(200)
                .extract().jsonPath().getMap("$");

        given().spec(request)
                .contentType(ContentType.JSON)
                .body("""
                        {"name":"Geçersiz Ürün","price":-10}
                        """)
                .when().post("/products")
                .then().log().ifValidationFails()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .body("error", equalTo("INVALID_PRODUCT"));

        given().spec(request)
                .when().get("/products")
                .then().log().ifValidationFails()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", equalTo(productsBefore));

        given().spec(request).pathParam("id", 2)
                .when().get("/products/{id}")
                .then().log().ifValidationFails()
                .statusCode(404)
                .body("error", equalTo("NOT_FOUND"));
    }

    @Test(description = "API-003 - POST product persists it for subsequent GET requests")
    public void createProduct() {
        int id = given().spec(request)
                .contentType(ContentType.JSON)
                .body("""
                        {"name":"Türkçe Klavye","price":899.50}
                        """)
                .when().post("/products")
                .then().log().ifValidationFails()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .body("id", greaterThan(1))
                .body("name", equalTo("Türkçe Klavye"))
                .body("price", equalTo(899.50f))
                .extract().path("id");

        given().spec(request).pathParam("id", id)
                .when().get("/products/{id}")
                .then().log().ifValidationFails()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(id))
                .body("name", equalTo("Türkçe Klavye"))
                .body("price", equalTo(899.50f));

        given().spec(request)
                .when().get("/products")
                .then().log().ifValidationFails()
                .statusCode(200)
                .body("total", equalTo(2))
                .body("products", hasSize(2))
                .body("products.find { it.id == " + id + " }.name", equalTo("Türkçe Klavye"));
    }

    @Test(description = "API-002 - GET product by ID returns the expected product")
    public void getProductById() {
        given().spec(request)
                .pathParam("id", 1)
                .when().get("/products/{id}")
                .then().log().ifValidationFails()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(1))
                .body("name", equalTo("Kablosuz Mouse"))
                .body("price", equalTo(499.90f));
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
