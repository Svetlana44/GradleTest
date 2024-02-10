package tests.junit5.api;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import models.fakeapiuser.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.*;

public class SimpleApiRefactoredTests {

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://fakestoreapi.com";
        /* логирование фильтруем  */
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Test
    public void getAllUsersTest() {
        given().get("/users")
                .then()
                .statusCode(200);
    }

    @Test
    public void getSingleUserTest() {
        int userId = 2;
        User response = given().pathParam("userId", userId)
                .get("/users/{userId}")
                .then()
                .statusCode(200)
                .extract().as(User.class);
         /*       .body("id", equalTo(userId))
                .body("address.zipcode", matchesPattern("\\d{5}-\\d{4}"));  */

        Assertions.assertEquals(userId, response.getId());
        Assertions.assertTrue(response.getAddress().getZipcode().matches("\\d{5}-\\d{4}"));

        Name responseName = given().pathParam("userId", userId)
                .get("/users/{userId}")
                .then()
                .statusCode(200)
                .extract().jsonPath().getObject("name", Name.class);
    }

    @Test
    public void getAllUsersWithLimit() {
        /*  https://fakestoreapi.com/users?limit=3
        проверим, что полученный список имеет размер 3  */
        int limit = 3;

        List<User> users = given().queryParam("limit", limit)
                .get("/users/")
                .then()
                .statusCode(200)
                /*  корень тела обозначается пустыми двойными кавычками  */
                .body("", hasSize(limit))
                .body("size()", greaterThan(1))
                .body("size()", greaterThanOrEqualTo(3))
                /*    .extract().jsonPath().getList("", User.class)  */
                /*  .extract().jsonPath().getList("", User.class);  можно записать по другому:
                  .extract().as(new TypeRef<List<User>>() {
                });   */
                .extract().as(new TypeRef<List<User>>() {
                });

        Assertions.assertEquals(3, users.size());
    }

    /* Сортировка  https://fakestoreapi.com/users?sort=desc
нужно проверить два тела с сортировкой и без сортировки(в коде его отсортировать) */
    @Test
    public void getAllUsersSortByDescTest() {
        String sortType = "desc";

        List<User> usersSortedResponse = given()
                .queryParam("sort", sortType)
                .get("/users")
                .then()
                .extract().as(new TypeRef<List<User>>() {
                });

        List<User> usersNotSortedResponse = given()
                .get("/users")
                .then()
                .extract().as(new TypeRef<List<User>>() {
                });

        List<Integer> sortedResponseIds = usersSortedResponse.stream()
                .map(u -> u.getId())
                .collect(Collectors.toList());
        List<Integer> sortedByCodeIds = usersNotSortedResponse.stream()
                .map(User::getId)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        Assertions.assertNotEquals(usersSortedResponse, usersNotSortedResponse);
        Assertions.assertEquals(sortedByCodeIds, sortedResponseIds);
    }

    /* сначала создать пользователя, потом, его поменять  */
    private User getTestUser() {
        Random random = new Random();
        Name name = new Name("Thomas", "Andersen");
        Geolocation geolocation = new Geolocation("-37.3159", "81.1496");
        Address address = Address.builder()
                .city("Moscow")
                .number(random.nextInt(100))
                .zipcode("54321-4321")
                .street("Noviy Arbat 12")
                .geolocation(geolocation)
                .build();

        return User.builder()
                .id(13)
                .name(name)
                .phone(String.valueOf(random.nextInt(123456789)))
                .email("fake@gmail.com")
                .username("thomasadmin")
                .password("mypassword")
                .address(address)
                .build();
    }

    @Test
    public void addNewUserTest() {
        User user = getTestUser();

        Integer userId = given().body(user)
                .post("/users")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .extract().jsonPath().getInt("id");

        Assertions.assertNotNull(userId);
    }

    @Test
    public void updateUserTest() {
        User user = getTestUser();
        String oldPassword = user.getPassword();

        user.setPassword("newpass2");

        User upDatedUser = given()
                .body(user)
                .pathParam("userId", user.getId())
                .put("/users/{userId}" /* + user.getId()*/)
                .then()
                .body("password", not(equalTo(oldPassword)))
                .extract().as(User.class);
        Assertions.assertNotEquals(upDatedUser.getPassword(), oldPassword);
    }

    /* авторизация , взаимодействие с закрытым ресурсом */
    @Test
    public void authUserTest() {
        AuthData authData = new AuthData("johnd", "m38rmF$");
        /* чтобы сервер понял  HashMap как Json, нужно указать .contentType(ContentType.JSON)  */
        String token = given().contentType(ContentType.JSON).body(authData)
                .post("/auth/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .extract().jsonPath().getString("token");
        Assertions.assertNotNull(token);
    }

}
