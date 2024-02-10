package assertions;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;

/*  обёртка части запроса
Info info =      given().contentType(ContentType.JSON)
                .body(fullUser)
                .post("/api/signup")
                .then()*/
/* чтобы не писать конструктор, можно написать аанотацию, подхватит в конструктор все финальные переменные
@RequiredArgsConstructor
    public AssertableResponse(ValidatableResponse response) {
        this.response = response;
    }
*/
@RequiredArgsConstructor
public class AssertableResponse {

    private final ValidatableResponse response;

    public AssertableResponse should(Condition condition) {
        condition.check(response);
        return this;
    }

    public String asJwt() {
        return response.extract().jsonPath().getString("token");
    }

    /* параметризованный метод передаём <T> и возвращаем Т  */
    public <T> T as(Class<T> tClass) {
        return response.extract().as(tClass);
    }

    /* параметризованный метод передаём <T> и возвращаем Т  для Json пути*/
    public <T> T as(String jsonPath, Class<T> tClass) {
        return response.extract().jsonPath().getObject(jsonPath, tClass);
    }

    /* получение списка через класс */
    public <T> List<T> asList(Class<T> tClass) {
        return response.extract().jsonPath().getList("", tClass);
    }

    /* получение списка через jsonPath */
    public <T> List<T> asList(String jsonPath, Class<T> tClass) {
        return response.extract().jsonPath().getList(jsonPath, tClass);
    }

    /*сырой респонс*/
    public Response asResponse() {
        return response.extract().response();
    }
}
