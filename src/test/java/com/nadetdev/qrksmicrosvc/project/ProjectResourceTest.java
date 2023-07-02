package com.nadetdev.qrksmicrosvc.project;

import com.nadetdev.qrksmicrosvc.task.Task;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class ProjectResourceTest {

    /*@Test
    @TestSecurity(user = "user", roles = "user")
    void delete() {
        var toDelete = given().body("{\"name\":\"to-delete\", " +
                        "\"user\": {\"name\": \"user\", \"password\": \"quarkus\", \"roles\": [\"user\"]}")
                .contentType(ContentType.JSON)
                .post("/api/v1/projects").as(Project.class);
        var dependentTask = given()
                .body("{\"title\":\"dependent-task\",\"project\":{\"id\":" + toDelete.id + "}}").contentType(ContentType.JSON)
                .post("/api/v1/tasks").as(Task.class);
        given()
                .when().delete("/api/v1/projects/" + toDelete.id)
                .then()
                .statusCode(204);
        assertThat(Task.<Task>findById(dependentTask.id).await().indefinitely().project, nullValue());
    }*/

}