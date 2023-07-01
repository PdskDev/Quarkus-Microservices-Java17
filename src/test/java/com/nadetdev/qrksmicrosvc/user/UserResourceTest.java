package com.nadetdev.qrksmicrosvc.user;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserResourceTest {

    @Order(2)
    @Test
    @TestSecurity(user = "admin", roles = "admin")
    void list() {
        given()
                .when()
                .get("/api/v1/users")
                .then()
                .statusCode(200)
                .body("$.size()", greaterThanOrEqualTo(1),
                        "[0].name", is("nadetdev"),
                        "[0].password", nullValue());
    }

    @Order(1)
    @Test
    @TestSecurity(user = "admin", roles = "admin")
    void create() {
        given()
                .body("{\"name\": \"nadetdev\", \"password\": \"pass123\", \"roles\": [\"user\"]}")
                .contentType(ContentType.JSON)
                .when()
                .post("/api/v1/users")
                .then()
                .statusCode(201)
                .body(
                        "name", is("nadetdev"),
                        "password", nullValue(),
                        "created", not(emptyString())
                );
    }

    @Order(3)
    @Test
    @TestSecurity(user = "user", roles = "user")
    void createUnauthorized(){
        given()
                .body("{\"name\": \"bouesso\", \"password\": \"pass123\", \"roles\": [\"user\"]}")
                .contentType(ContentType.JSON)
                .when()
                .post("/api/v1/users")
                .then()
                .statusCode(403);
    }

    @Order(4)
    @Test
    @TestSecurity(user = "admin", roles = "admin")
    void createDuplicate(){
        given()
                .body("{\"name\": \"nadetdev\", \"password\": \"quarkus\", \"roles\": [\"user\"]}")
                .contentType(ContentType.JSON)
                .when()
                .post("/api/v1/users")
                .then()
                .statusCode(400);
    }

    @Order(5)
    @Test
    @TestSecurity(user = "admin", roles = "admin")
    void update() {
        var user = given()
                .body("{\"name\": \"user-to-update\", \"password\": \"quarkus\", \"roles\": [\"user\"]}")
                .contentType(ContentType.JSON)
                .when()
                .post("/api/v1/users")
                .as(User.class);

        user.name = "user-is-updated";

        given()
                .body(user)
                .contentType(ContentType.JSON)
                .when()
                .put("/api/v1/users/" + user.id)
                .then()
                .statusCode(200)
                .body(
                        "name", is("user-is-updated"),
                        "version", is(user.version + 1)
                );
    }
}