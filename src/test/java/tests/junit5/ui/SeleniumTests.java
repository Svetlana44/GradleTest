package tests.junit5.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/* ChromDriver нужно складывать в папку resouces, чтобы проект работал не только на локальном компе */

public class SeleniumTests {
    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        /* бинарник-это кастомный запуск определённого браузера, напрмер, Яндекс
         * нужно найти его exe на диске и указать, чтобы запускать через Яндекс */
              options.setBinary("C:/Users/SpS/AppData/Local/Yandex/YandexBrowser/Application/browser.exe");

        /* браузер будет запущен в невидимом окне на удалённом сервере */
        /* учитывать таймаут загрузки */
        //      options.addArguments("--headless");

        /*     options.addArguments("--user agent=Mozila/12983h Webkit");   */

        /* чтобы опции применились, их нужно подставить в экземпляр при создании браузера */
        driver = new ChromeDriver(options);
        //       WebDriver driver = new ChromeDriver();
        //FullHD
        driver.manage().window().setSize(new Dimension(1920, 1080));

    }

    @AfterEach
    public void ternDown() {
        //     driver.quit();
        driver.close();
    }

    @Test
    public void simpleTest() {
        driver.get("https://threadqa.ru");
        /*перемещение по странице navigate*/
        //       driver.navigate().refresh();
        /*управление страницей*/
        //       driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(333));
        //      driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        /*можно получит список вкладок*/
//        driver.getWindowHandles();
//        driver.switchTo().activeElement();
        String actualTitle = driver.getTitle();
        String expectedTitle = "Oleg Pendrak | ThreadQA";

        Assertions.assertEquals(expectedTitle, actualTitle);
    }
}
