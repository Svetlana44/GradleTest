package assertions.conditions;

import assertions.Condition;
import io.restassured.response.ValidatableResponse;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;

@RequiredArgsConstructor
public class StatusCodeCondition implements Condition {
    private final Integer statusCode;

    @Override
    public void check(ValidatableResponse response) {
        /* response.assertThat().statusCode(statusCode); можно так */
        int actualStatusCode = response.extract().statusCode();
        Assertions.assertEquals(statusCode, actualStatusCode);
    }
}
