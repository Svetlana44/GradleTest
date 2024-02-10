package tests.testng;

import calc.CalcSteps;
import listener.RetryListenerTestNG;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.IOException;

/*  тест на TestNG движке
аннотацию для прослушки можно навесить на весь класс, можно на отдельные методы , можно на общий базовый класс и на наследников будет действовать    */
@Listeners(RetryListenerTestNG.class) /* прослушка на сохранение упавших тестов  */
public class NGTestsAll {

    @BeforeSuite
    public void setAnalyser(ITestContext context) {
        for (ITestNGMethod testMethod : context.getAllTestMethods()) {
            /* для каждого теста будет новый count */
            testMethod.setRetryAnalyzer(new RetryListenerTestNG());
        }
    }

    @AfterSuite
    public void saveFailed() throws IOException {
        RetryListenerTestNG.saveTests();
    }

    @Test(groups = {"sum1"})
    public void sumTestNGTest1() {
        CalcSteps calcSteps = new CalcSteps();
        Assert.assertTrue(calcSteps.isPositive(-10));
    }

    @Test(groups = {"sum2"})
    public void sumTestNGTest2() {
        CalcSteps calcSteps = new CalcSteps();
        Assert.assertTrue(calcSteps.isPositive(-10));
    }

    @Test()
    public void sumTestNGTest3() {
        CalcSteps calcSteps = new CalcSteps();
        Assert.assertTrue(calcSteps.isPositive(-10));
    }
}
