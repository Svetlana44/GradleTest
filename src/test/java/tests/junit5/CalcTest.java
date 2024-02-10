package tests.junit5;

import calc.CalcSteps;
import io.qameta.allure.Allure;
import io.qameta.allure.Issue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class CalcTest {
    @Test
    public void sumTest() {
        CalcSteps calcSteps = new CalcSteps();
        int result = calcSteps.sum(1, 4);
        boolean isOk = calcSteps.isPositive(result);
        Assertions.assertTrue(isOk);
    }

    /*  это тот же самый тест ч/з стэпы, т.е. две формы записи */
    @Test
    @Issue("VIDEOTECH-5612")  /* номер задачи для Jira , см. allure.properties */
    public void sumStepsTest() {
        int a = -5;
        int b = 4;
        AtomicInteger result = new AtomicInteger();
        Allure.step("Прибавляем " + a + " к переменной " + b, step -> {
            result.set(a + b);
        });
        Allure.step("Проверяем, что результат " + result.get() + " больше нуля", x -> {
            Assertions.assertTrue(result.get() > 0);
        });
    }
}
