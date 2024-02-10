package assertions.conditions;

import assertions.Condition;
import io.restassured.response.ValidatableResponse;
import lombok.RequiredArgsConstructor;
import models.swagger.Info;
import org.junit.jupiter.api.Assertions;

@RequiredArgsConstructor
/* аннотация заменяет конструктор с финальными переменными */
public class MessageCondition implements Condition {

    private final String expectedMessage;

    @Override
    public void check(ValidatableResponse response) {
        /* можно так:    response.body("info.message", equalTo(expectedMessage));  но лучше ч/з модельный класс Info  */

        Info info = response.extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals(expectedMessage, info.getMessage());

    }
}
