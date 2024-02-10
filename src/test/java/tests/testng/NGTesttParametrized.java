package tests.testng;

import listener.RetryListenerTestNG;
import models.People;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*  тест на TestNG движке
аннотацию для прослушки можно навесить на весь класс, можно на отдельные методы , можно на общий базовый класс и на наследников будет действовать    */
@Listeners(RetryListenerTestNG.class) /* прослушка на сохранение упавших тестов  */
public class NGTesttParametrized {

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

    /* для параметризации тестов  */
    @DataProvider(name = "testUsers")
    public Object[] dataWithUsers() {
        People stas = new People("Stas", "25", "male");
        People katya = new People("Katya", "20", "female");
        People oleg = new People("Oleg", "30", "male");
        return new Object[]{stas, katya, oleg};
    }

    /*  этот тест использует данные из провайдера выше */
    @Test(dataProvider = "testUsers")
    public void testUsersWithRole(People people) {
        System.out.println(people.getName());
        Assert.assertTrue(people.getSex().contains("male"));
        Assert.assertTrue(people.getName().contains("a"));
    }

    @DataProvider(name = "ips")
    public Object[] testIpAddresses() {
        List<String> ips = new ArrayList<>();
        ips.add("127.0.0.1");
        ips.add("localhost");
        ips.add("58.43.121.90");
        return ips.toArray();
    }

    @Test(dataProvider = "ips")
    public void ipTest(String ip) {
        System.out.println(ip);
        Assert.assertTrue(ip.matches("\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b"));
    }

    @Test(dataProviderClass = DataTestArgumentsNG.class, dataProvider = "argsForCalc")
    public void calcTest(int a, int b, int c) {
        Assert.assertEquals(a + b, c);
    }

    @Test(dataProviderClass = DataTestArgumentsNG.class, dataProvider = "diffArgs")
    public void someMagicTransform(int a, String str) {
        Assert.assertEquals(convert(a), str);
    }

    private String convert(int a) {
        switch (a) {
            case 1:
                return "one";
            case 2:
                return "two";
            case 3:
                return "three";
            case 5:
                return "five";
            default:
                return "null";

        }
    }
}