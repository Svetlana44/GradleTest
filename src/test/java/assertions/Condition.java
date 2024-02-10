package assertions;

import io.restassured.response.ValidatableResponse;

/*  интерфейс будем передавать в обёртку  */
public interface Condition {
    void check(ValidatableResponse response);
}
