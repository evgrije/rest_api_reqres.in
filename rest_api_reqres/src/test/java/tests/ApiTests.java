package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import models.pojo.LoginBodyModel;
import models.pojo.LoginResponseModel;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApiTests {

    @Test
    void register(){
        String body = "{\n" +
                "    \"email\": \"eve.holt@reqres.in\",\n" +
                "    \"password\": \"pistol\"\n" +
                "}";
        given()
                .body(body)
                .contentType(JSON)

                .when()
                .post("https://reqres.in/api/register")

                .then()
                .log().body()
                .body("token", hasValue("QpwL5tke4Pnpja7X4"));

    }


    @Test
     void logIn(){
       // String body = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\"}";
        LoginBodyModel authBody = new LoginBodyModel();
        authBody.setLogin("eve.holt@reqres.in");
        authBody.setPassword("cityslicka");

        LoginResponseModel response = given()
                .filter(new AllureRestAssured())
                .body(authBody)
                .contentType(JSON)

                .when()
                .post("https://reqres.in/api/login")

                .then()
                    .log().body()
                    .statusCode(200)
                .extract().as(LoginResponseModel.class); //запрос мы достаем из него ответ как модель LoginResponseModel
        assertEquals("QpwL5tke4Pnpja7X4", response.getToken());

    }

    @Test
    void checkTotalUsersInList(){
        get("https://reqres.in/api/users?page=2")
                .then()
                .body("total", is(12))
                .statusCode(200);

    }

    @Test
    void checkSizeDataUsers(){
        given()
                .when()
                .queryParam("page", "2")
                .get("https://reqres.in/api/users")
                .then()
                .body("data.size()", equalTo(6));

    }

    @Test
    void checkSizeMoreThanOne(){
        given()
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .body("data.size()", greaterThan(1));
    }

    @Test
    void createUser(){
        String body = "{\"name\": \"morpheus\", \"job\": \"leader\"}";
        given()
                .body(body)
                .contentType(JSON)
                .log().uri()

                .when()
                    .post("https://reqres.in/api/users")

                .then()
                    .log().body()
                    .body("name", equalTo("morpheus"));
    }

}