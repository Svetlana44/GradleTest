package tests.swaggertests;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import listener.AdminUser;
import listener.AdminUserResolver;
import listener.CustomTpl;
import models.swagger.FullUser;
import models.swagger.Info;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import tests.junit5.api.UserService;
import utils.RandomTestData;

import java.util.List;
import java.util.Random;

import static assertions.Conditions.hasMessage;
import static assertions.Conditions.hasStatusCode;
@ExtendWith(AdminUserResolver.class)
public class UserNewTests {

    public static Random random;
    private static UserService userService;
    private FullUser fullUser;

    @BeforeEach
    public void initTestUser() {
        fullUser = getRandomUser();
    }

    @BeforeAll
    public static void setUp() {
        /*    RestAssured.baseURI = "http://85.192.34.140:8080/";  */
        RestAssured.baseURI = "http://85.192.34.140:8080/api";
        /*добавить 2 стандартных фильтра логирования и 1 кастомный */
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(),
                CustomTpl.customLogFilter().withCustomTemplates());
        random = new Random();
        userService = new UserService();
    }

    private FullUser getRandomUser() {
        int randomNumber = Math.abs(random.nextInt());

        return FullUser.builder()
                .login("threadQATestUser" + randomNumber)
                .pass("passwordCOOL")
                .build();

    }

    private FullUser getAdminUser() {
        return FullUser.builder()
                .login("admin")
                .pass("admin")
                .build();
    }

    /* проверка регистрации пользователя */
    @Test
    public void positiveRegisterTest() {
        userService.register(fullUser)
                .should(hasStatusCode(201))
                .should(hasMessage("User created"));
    }

    @Test
    public void negativeRegisterLoginExistsTest() {
        userService.register(fullUser);
        userService.register(fullUser)
                .should(hasStatusCode(400))
                .should(hasMessage("Login already exist"));
    }

    @Test
    public void negativeRegisterNoPasswordTest() {
        fullUser.setPass(null);
        userService.register(fullUser)
                .should(hasMessage("Missing login or password"))
                .should(hasStatusCode(400));
    }

    @Test
    public void positiveAdminAuthTest() {
        FullUser user = getAdminUser();
        String token = userService.auth(user)
                .should(hasStatusCode(200))
                .asJwt();
        Assertions.assertNotNull(token);
    }

    @Test
    public void positiveNewUserTest() {
        userService.register(fullUser);
        String token = userService.auth(fullUser)
                .should(hasStatusCode(200))
                .asJwt();

        Assertions.assertNotNull(token);
    }

    @Test
    public void negativeNewUserTest() {
        /*регистрации не было, сразу авторизация, нет такого юзера*/
        userService.auth(fullUser)
                .should(hasStatusCode(401));
    }

    @Test
    public void positiveGetUserInfoTest(@AdminUser FullUser admin) {
        FullUser user = getAdminUser();
        String token = userService.auth(user).asJwt();
        userService.getUserInfo(token)
                .should(hasStatusCode(200));
    }

    @Test
    public void negativeGetUserInfoInvalidJWTTest() {
        userService.getUserInfo("fake jwt")
                .should(hasStatusCode(401));
    }

    @Test
    public void negativeGetUserWithoutJWTTest() {
        userService.getUserInfo()
                .should(hasStatusCode(401));
    }

    @Test
    public void positiveChangeUserPassTest() {
        String oldPassword = fullUser.getPass();
        userService.register(fullUser);

        String token = userService.auth(fullUser).asJwt();

        String updatedPassValue = "newpassUpdated";

        userService.updatePass(updatedPassValue, token)
                .should(hasStatusCode(200))
                .should(hasMessage("User password successfully changed"));

        fullUser.setPass(updatedPassValue);

        token = userService.auth(fullUser).should(hasStatusCode(200)).asJwt();

        FullUser updatedUser = userService.getUserInfo(token).as(FullUser.class);

        Assertions.assertNotEquals(oldPassword, updatedUser.getPass());
    }

    @Test
    public void negativeChangeAdminPasswordTest() {
        FullUser user = getAdminUser();

        String token = userService.auth(user).asJwt();

        String updatedPassValue = "newpassUpdated";
        userService.updatePass(updatedPassValue, token)
                .should(hasStatusCode(400))
                .should(hasMessage("Cant update base users"));
    }

    @Test
    public void negativeDeleteAdminTest() {
        FullUser user = getAdminUser();

        String token = userService.auth(user).asJwt();

        userService.deleteUser(token)
                .should(hasStatusCode(400))
                .should(hasMessage("Cant delete base users"));
    }

    @Test
    public void positiveDeleteNewUserTest() {
        userService.register(fullUser);
        String token = userService.auth(fullUser).asJwt();

        userService.deleteUser(token)
                .should(hasStatusCode(200))
                .should(hasMessage("User successfully deleted"));
    }

    @Test
    public void positiveGetAllUsersTest() {
        List<String> users = userService.getAllUsers().asList(String.class);
        Assertions.assertTrue(users.size() >= 3);
    }

    @Test
    public void positiveRegisterWithGamesTest() {
        RandomTestData randomTestData = new RandomTestData();
        FullUser fullUser = randomTestData.getRandomUserWithGames();
        Response response = userService.register(fullUser)
                .should(hasStatusCode(201))
                .should(hasMessage("User created"))
                .asResponse();
        Info info = response.jsonPath().getObject("info", Info.class);
        /* SoftAssertions чтобы тесты сразу не падали а проверили всё   */
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(info.getMessage()).as("Сообщение об ошибке было неверное")
                .isEqualTo("фейк месседж");
        softAssertions.assertThat(response.statusCode()).as("Статус код не был 200")
                .isEqualTo(201);
        softAssertions.assertAll();
    }
}
