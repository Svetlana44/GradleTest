package tests.testng;

import calc.CalcSteps;
import listener.RetryListenerTestNG;
import org.testng.Assert;
import org.testng.annotations.Test;

/*  тест на TestNG движке */
public class NGTests {
    @Test(retryAnalyzer = RetryListenerTestNG.class)
    public void sumTestNGTest() {
        CalcSteps calcSteps = new CalcSteps();
        Assert.assertTrue(calcSteps.isPositive(-10));
    }
}
