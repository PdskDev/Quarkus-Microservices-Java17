package com.nadetdev.qrksmicrosvc.auth;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.emptyString;

@QuarkusTest
class AuthResourceTest {

    @Test
    void loginValidCredentials() {
        RestAssured.given()
                .body("{\"name\": \"admin\", \"password\": \"quarkus\"}")
                .contentType(ContentType.JSON)
                .when()
                .post("/api/v1/auth/login")
                .then()
                .statusCode(200)
                .body(not(emptyString()));
    }

    @Test
    void loginInvalidCredentials() {
        RestAssured.given()
                .body("{\"name\": \"admin\", \"password\": \"pass010203\"}")
                .contentType(ContentType.JSON)
                .when()
                .post("/api/v1/auth/login")
                .then()
                .statusCode(401);
    }
}