package tests.swaggertests;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import listener.CustomTpl;
import models.swagger.FullUser;
import models.swagger.Info;
import models.swagger.JwtAuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class UserTests {

    public static Random random;

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://85.192.34.140:8080/";
        /*добавить 2 стандартных фильтра логирования и 1 кастомный */
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(),
                CustomTpl.customLogFilter().withCustomTemplates());
        random = new Random();
    }

    /* проверка регистрации пользователя */
    @Test
    public void positiveRegisterTest() {

        int randomNumber = Math.abs(random.nextInt());

        FullUser fullUser = FullUser.builder()
                .login("threadQATestUser" + randomNumber)
                .pass("passwordCOOL")
                .build();

        Info info = given().contentType(ContentType.JSON)
                .body(fullUser)
                .post("/api/signup")
                .then()
                .statusCode(201)
                .extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals("User created", info.getMessage());
    }

    @Test
    public void negativeRegisterLoginExistsTest() {

        int randomNumber = Math.abs(random.nextInt());

        FullUser fullUser = FullUser.builder()
                .login("threadQATestUser" + randomNumber)
                .pass("passwordCOOL")
                .build();
        Info info = given().contentType(ContentType.JSON)
                .body(fullUser)
                .post("/api/signup")
                .then()
                .statusCode(201)
                .extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals("User created", info.getMessage());

        Info errorInfo = given().contentType(ContentType.JSON)
                .body(fullUser)
                .post("/api/signup")
                .then()
                .statusCode(400)
                .extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals("Login already exist", errorInfo.getMessage());
    }

    @Test
    public void negativeRegisterNoPasswordTest() {
        int randomNumber = Math.abs(random.nextInt());

        FullUser fullUser = FullUser.builder()
                .login("threadQATestUser" + randomNumber)
                .build();
        Info info = given().contentType(ContentType.JSON)
                .body(fullUser)
                .post("/api/signup")
                .then()
                .statusCode(400)
                .extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals("Missing login or password", info.getMessage());
    }

    @Test
    public void positiveAdminAuthTest() {
        JwtAuthData jwtAuthData = new JwtAuthData("admin", "admin");

        String token = given().contentType(ContentType.JSON)
                .body(jwtAuthData)
                .post("/api/login")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("token");

        Assertions.assertNotNull(token);
    }

    @Test
    public void positiveNewUserTest() {
        int randomNumber = Math.abs(random.nextInt());

        FullUser fullUser = FullUser.builder()
                .login("threadQATestUser" + randomNumber)
                .pass("passwordCOOL")
                .build();
        Info info = given().contentType(ContentType.JSON)
                .body(fullUser)
                .post("/api/signup")
                .then()
                .statusCode(201)
                .extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals("User created", info.getMessage());

        JwtAuthData jwtAuthData = new JwtAuthData(fullUser.getLogin(), fullUser.getPass());

        String token = given().contentType(ContentType.JSON)
                .body(jwtAuthData)
                .post("/api/login")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("token");

        Assertions.assertNotNull(token);
    }

    @Test
    public void negativeNewUserTest() {
        JwtAuthData jwtAuthData = new JwtAuthData("notUser", "notUser");

        String error = given().contentType(ContentType.JSON)
                .body(jwtAuthData)
                .post("/api/login")
                .then()
                .statusCode(401).extract().jsonPath().get("error");

        Assertions.assertEquals("Unauthorized", error);
    }

    @Test
    public void positiveGetUserInfoTest() {
        JwtAuthData jwtAuthData = new JwtAuthData("admin", "admin");

        String token = given().contentType(ContentType.JSON)
                .body(jwtAuthData)
                .post("/api/login")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("token");

        /*      given().header("Authorization", "Bearer " + token)  можно заменить: */
        given().auth().oauth2(token)
                .get("/api/user")
                .then().statusCode(200);
    }

    @Test
    public void negativeGetUserInfoInvalidJWTTest() {

        /*      given().header("Authorization", "Bearer " + token)  можно заменить: */
        given().auth().oauth2("some not token")
                .get("/api/user")
                .then().statusCode(401);
    }

    @Test
    public void negativeGetUserWithoutJWTTest() {
        given()
                .get("/api/user")
                .then().statusCode(401);
    }

    @Test
    public void positiveChangeUserPassTest() {
        int randomNumber = Math.abs(random.nextInt());

        FullUser fullUser = FullUser.builder()
                .login("threadQATestUser" + randomNumber)
                .pass("passwordCOOL")
                .build();
        Info info = given().contentType(ContentType.JSON)
                .body(fullUser)
                .post("/api/signup")
                .then()
                .statusCode(201)
                .extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals("User created", info.getMessage());

        JwtAuthData jwtAuthData = new JwtAuthData(fullUser.getLogin(), fullUser.getPass());

        String token = given().contentType(ContentType.JSON)
                .body(jwtAuthData)
                .post("/api/login")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("token");


        Map<String, String> password = new HashMap<>();
        String passUpdated = "newpassUpdated";
        password.put("password", passUpdated);

        Info infoMassage = given().contentType(ContentType.JSON)
                .auth().oauth2(token)
                .body(password)
                .put("/api/user")
                .then()
                .extract().jsonPath().getObject("info", Info.class);
          /*    String message = given().contentType(ContentType.JSON)
                .auth().oauth2(token)
                .body(password)
                .put("/api/user")
                .then()
                .extract().jsonPath().getString("info.message");
        Assertions.assertEquals("User password successfully changed", message);  */

        Assertions.assertEquals("User password successfully changed", infoMassage.getMessage());

        JwtAuthData jwtAuthDataUpdated = new JwtAuthData(fullUser.getLogin(), passUpdated);

        String tokenUpdated = given().contentType(ContentType.JSON)
                .body(jwtAuthDataUpdated)
                .post("/api/login")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("token");

        given().auth().oauth2(token)
                .get("/api/user")
                .then()/*.statusCode(401)*/;

        FullUser userUpdated = given().auth().oauth2(tokenUpdated)
                .get("/api/user")
                .then().statusCode(200)
                .extract().as(FullUser.class);

        Assertions.assertEquals(passUpdated, userUpdated.getPass());
    }

    @Test
    public void negativeChangeUserPassTest() {

        JwtAuthData jwtAuthData = new JwtAuthData("admin", "admin");

        String token = given().contentType(ContentType.JSON)
                .body(jwtAuthData)
                .post("/api/login")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("token");


        Map<String, String> password = new HashMap<>();
        String passUpdated = "newpassUpdated";
        password.put("password", passUpdated);

        Info infoMassage = given().contentType(ContentType.JSON)
                .auth().oauth2(token)
                .body(password)
                .put("/api/user")
                .then()
                .statusCode(400)
                .extract().jsonPath().getObject("info", Info.class);
          /*    String message = given().contentType(ContentType.JSON)
                .auth().oauth2(token)
                .body(password)
                .put("/api/user")
                .then()
                .extract().jsonPath().getString("info.message");
        Assertions.assertEquals("User password successfully changed", message);  */

        Assertions.assertEquals("Cant update base users", infoMassage.getMessage());
    }

    @Test
    public void negativeDeleteAdminTest() {
        JwtAuthData authData = new JwtAuthData("admin", "admin");

        String token = given().contentType(ContentType.JSON)
                .body(authData)
                .post("/api/login")
                .then().statusCode(200)
                .extract().jsonPath().getString("token");

        Info info = given().auth().oauth2(token)
                .delete("/api/user")
                .then().statusCode(400)
                .extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals("Cant delete base users", info.getMessage());
    }

    @Test
    public void positiveDeleteNewUserTest() {
        int randomNumber = Math.abs(random.nextInt());
        FullUser user = FullUser.builder()
                .login("threadQATestUser" + randomNumber)
                .pass("passwordCOOL")
                .build();

        Info info = given().contentType(ContentType.JSON)
                .body(user)
                .post("/api/signup")
                .then().statusCode(201)
                .extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals("User created", info.getMessage());

        JwtAuthData authData = new JwtAuthData(user.getLogin(), user.getPass());

        String token = given().contentType(ContentType.JSON)
                .body(authData)
                .post("/api/login")
                .then().statusCode(200)
                .extract().jsonPath().getString("token");

        Info infoDelete = given().auth().oauth2(token)
                .delete("/api/user")
                .then().statusCode(200)
                .extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals("User successfully deleted", infoDelete.getMessage());
    }

    @Test
    public void positiveGetAllUsersTest() {
        List<String> users = given()
                .get("/api/users")
                .then()
                .statusCode(200)
                /*TypeRef<String> указывает, в какой список нужно получить корень ответа
                в корне имя пользователей, пользователя превращает в строку */
                .extract().as(new TypeRef<List<String>>() {
                });
        Assertions.assertTrue(users.size() >= 3);
    }
}
