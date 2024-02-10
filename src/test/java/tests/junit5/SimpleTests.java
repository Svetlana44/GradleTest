package tests.junit5;

import listener.RetryListenerJunit5;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;

/*   @Tag("SIMPLETEST")  */
/*Чтобы пользоваться этим утильным классом, нужно на др. класс повесить аннотацию @ExtendWith(RetryListener.class)  */
@ExtendWith(RetryListenerJunit5.class)
public class SimpleTests {

    @AfterAll
    public static void saveFailed() throws IOException {
        RetryListenerJunit5.saveTests();
    }

    @Test
    @Tag("SIMPLETEST")
    @Tag("API")
    @DisplayName("Первый тест")
//    @Disabled("Не хотим запускать")
    public void dummy() {
        Assertions.assertEquals(5, 4, "Не равно");
    }

    @ParameterizedTest
    @CsvSource({"stas,18,male", "sasha,20,female", "misha,10,male"})
    public void testParams(String name, String age, String sex) {
        Assertions.assertTrue(name.contains("s"));

    }

    @ParameterizedTest
    @CsvFileSource(resources = "/people.csv")
    public void testParamsInFile(String name, String age, String sex) {
        Assertions.assertTrue(name.contains("s"));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/peopleDelimiter.csv", delimiter = '$')
    public void testParamsInFileWithDelimiter(String name, int age, String sex) {
        Assertions.assertTrue(name.contains("s"));
        Assertions.assertTrue(age > 1);
    }

    @Test
 //   @DisplayName("Второй тест")
 //   @Disabled
    public void dummy2() {
        Assertions.assertEquals(4, 3, "4 Не равно 3");
    }
}
