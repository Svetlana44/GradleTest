package tests.junit5.api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.fakeapiuser.Address;
import models.fakeapiuser.Geolocation;
import models.fakeapiuser.Name;
import models.fakeapiuser.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.*;
        /*https://fakestoreapi.com/users?limit=3
        pathParam():    https://fakestoreapi.com/users
        queryParam() :   ?limit=3
        */

public class SimpleApiTests {

    @Test
    public void getAllUsersTest() {
        given().get("https://fakestoreapi.com/users")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void getSingleUserTest() {
        int userId = 2;
        given().pathParam("userId", userId)
                .get("https://fakestoreapi.com/users/{userId}")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", equalTo(userId))
                .body("address.zipcode", matchesPattern("\\d{5}-\\d{4}"));
    }

    @Test
    public void getAllUsersWithLimit() {
        /*  https://fakestoreapi.com/users?limit=3
        проверим, что полученный список имеет размер 3  */
        int limit = 3;
        given().queryParam("limit", limit)
                .get("https://fakestoreapi.com/users/")
                .then()
                .log().all()
                .statusCode(200)
                /*  корень тела обозначается пустыми двойными кавычками  */
                .body("", hasSize(limit))
                .body("size()", greaterThan(1))
                .body("size()", greaterThanOrEqualTo(3));

    }

    /* Сортировка  https://fakestoreapi.com/users?sort=desc
    нужно проверить два тела с сортировкой и без сортировки(в коде его отсортировать) */
    @Test
    public void getAllUsersSortByDescTest() {
        String sortType = "desc";

        Response sortedResponse = given().queryParam("sort", sortType)
                .get("https://fakestoreapi.com/users")
                .then().log().all()
                .extract().response();

        Response notSortedResponse = given()
                .get("https://fakestoreapi.com/users")
                .then().log().all()
                .extract().response();

        List<Integer> sortedResponseIds = sortedResponse.jsonPath().getList("id");
        List<Integer> notSortedResponseIds = notSortedResponse.jsonPath().getList("id");

        List<Integer> sortedByCode = notSortedResponseIds
                .stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        Assertions.assertNotEquals(sortedResponseIds, notSortedResponseIds);
        Assertions.assertEquals(sortedByCode, sortedResponseIds);
    }

    @Test
    public void addNewUserTest() {
        Name name = new Name("Thomas", "Andersen");
        Geolocation geolocation = new Geolocation("-37.3159", "81.1496");
        Address address = Address.builder()
                .city("Moscow")
                .number(100)
                .zipcode("54321-4321")
                .street("Noviy Arbat 12")
                .geolocation(geolocation)
                .build();

        User bodyRequest = User.builder()
                .name(name)
                .phone("123456789")
                .email("fake@gmail.com")
                .username("thomasadmin")
                .password("mypassword")
                .address(address)
                .build();

        given().body(bodyRequest)
                .post("https://fakestoreapi.com/users")
                .then().log().all()
                .statusCode(200)
                .body("id", notNullValue());

    }

    /* сначала извлечь пользователя, потом, его поменять  */
    private User getTestUser() {
        Name name = new Name("Thomas", "Andersen");
        Geolocation geolocation = new Geolocation("-37.3159", "81.1496");
        Address address = Address.builder()
                .city("Moscow")
                .number(100)
                .zipcode("54321-4321")
                .street("Noviy Arbat 12")
                .geolocation(geolocation)
                .build();

        return User.builder()
                .name(name)
                .phone("123456789")
                .email("fake@gmail.com")
                .username("thomasadmin")
                .password("mypassword")
                .address(address)
                .build();
    }

    @Test
    public void updateUserTest() {
        User user = getTestUser();
        String oldPassword = user.getPassword();

        user.setPassword("newpass2");

        given().body(user)
                .put("https://fakestoreapi.com/users/" + user.getId())
                .then().log().all()
                .body("password", not(equalTo(oldPassword)));
    }

    @Test
    public void deleteUserTest() {
        given().delete("https://fakestoreapi.com/users/7")
                .then().log().all()
                .statusCode(200);
        /* + проверить, что под удалённым пользователем нельзя залогиниться и что его нет в БД*/
    }

    /* авторизация , взаимодействие с закрытым ресурсом */
    @Test
    public void authUserTest() {
        Map<String, String> userAuth = new HashMap<>();
        userAuth.put("username", "johnd");
        userAuth.put("password", "m38rmF$");
        /* чтобы сервер понял  HashMap как Json, нужно указать .contentType(ContentType.JSON)  */
        given().contentType(ContentType.JSON).body(userAuth)
                .post("https://fakestoreapi.com/auth/login")
                .then().log().all()
                .statusCode(200)
                .body("token", notNullValue());
    }
}
