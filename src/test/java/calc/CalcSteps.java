package calc;

import io.qameta.allure.Step;

/*  использование Allure report ч/з класс прослойку  */
public class CalcSteps {

    /*  можно писать не названия а индекс, например, {0} и {1}
    тэг аллюра   */
    @Step("Складываем числа {a} и {b}")
    public int sum(int a, int b) {
        return a + b;
    }

    @Step("Проверяем, что число {result} больше чем 0")
    public boolean isPositive(int result) {
        return result > 0;
    }

}
