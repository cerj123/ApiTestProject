package com.apiTests;

import com.model.*;
import io.qameta.allure.Description;
import io.qameta.allure.Flaky;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.util.List;
import java.util.stream.Collectors;

import static com.specification.Specifications.requestSpecification;

public class ReqresTest {

    @Test
    @DisplayName("Checking user avatar and id")
    public void checkAvatarAndIdTest() {
        List<UserData> users = RestAssured.given()
                .spec(requestSpecification())
                .get("api/users?page=2")
                .then()
                .extract()
                .body()
                .jsonPath()
                .getList("data", UserData.class);

        users.forEach(x -> Assertions.assertTrue(x.getAvatar().contains(x.getId().toString())));
        Assertions.assertTrue(users.stream().allMatch(x -> x.getEmail().endsWith("@reqres.in")));

        List<String> avatars = users.stream().map(UserData::getAvatar).collect(Collectors.toList());
        List<String> ids = users.stream().map(x -> x.getId().toString()).collect(Collectors.toList());

        for (int i = 0; i < avatars.size(); i++) {
            Assertions.assertTrue(avatars.get(i).contains(ids.get(i)));
        }
    }

    @Test
    @DisplayName("Checking successful user registration")
    public void successfulRegTest() {
        Integer id = 4;
        String token = "QpwL5tke4Pnpja7X4";
        SuccessReg successReg = RestAssured.given()
                .spec(requestSpecification())
                .body(new Register("eve.holt@reqres.in", "pistol"))
                .post("api/register")
                .then()
                .extract()
                .as(SuccessReg.class);
        Assertions.assertNotNull(successReg.getId());
        Assertions.assertNotNull(successReg.getToken());
        Assertions.assertEquals(id, successReg.getId());
        Assertions.assertEquals(token, successReg.getToken());
    }

    @Test
    @DisplayName("Checking unsuccessful user registration")
    public void unsuccessfulRegTest() {
        String error = "Missing password";
        RestAssured.given()
                .spec(requestSpecification())
                .body(new Register("sydney@fife", ""))
                .post("api/register")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .assertThat()
                .body("error", Matchers.is(error));
    }

    @Test
    @DisplayName("Checking sorting by year")
    public void yearsSortTest() {
        List<ResourceData> resources = RestAssured.given()
                .spec(requestSpecification())
                .get("api/unknown")
                .then()
                .extract()
                .body()
                .jsonPath()
                .getList("data", ResourceData.class);

        List<Integer> years = resources.stream().map(ResourceData::getYear).collect(Collectors.toList());
        List<Integer> sortedYears = years.stream().sorted().collect(Collectors.toList());
        Assertions.assertEquals(sortedYears, years);
    }

    @Test
    @DisplayName("Checking deleting of user")
    public void deleteUserTest() {
        RestAssured.given()
                .spec(requestSpecification())
                .delete("api/users/2")
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    @Flaky
    @DisplayName("Checking of server time")
    @Description("Checking of server time is flaky test")
    public void timeTest() {
        UserTimeResponse response = RestAssured.given()
                .spec(requestSpecification())
                .body(new UserTime("morpheus", "zion resident"))
                .put("api/users/2")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(UserTimeResponse.class);

        String regexCurrentTime = "(.{11})$";
        String regexServerTime = "(.{5})$";
        String currentTime = Clock.systemUTC().instant().toString().replaceAll(regexCurrentTime, "");
        Assertions.assertEquals(currentTime, response.getUpdatedAt().replaceAll(regexServerTime, ""));
    }
}
