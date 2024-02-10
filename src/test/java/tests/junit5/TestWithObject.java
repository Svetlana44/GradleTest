package tests.junit5;

import models.People;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class TestWithObject {
    /*  стрим аргументов  для параметризованного теста  */
    private static Stream<Arguments> testPeople() {
        return Stream.of(
                Arguments.of(new People("stas", "18", "male")),
                Arguments.of(new People("sasha", "8", "female")),
                Arguments.of(new People("misha", "23", "male"))
        );
    }

    /* параметризованный тест  берёт из стрима выше объект People */
    @ParameterizedTest
    /*  нужно указать, из какого метода берёт параметр(вывод метода)  */
    @MethodSource("testPeople")
    public void testParamsFromStream(People people) {
        Assertions.assertTrue(people.getName().contains("s"));
    }
}