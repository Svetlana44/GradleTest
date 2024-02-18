package tests.junit5.pageobjectstests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

/*   for  Page Object */
public class BaseTest {
    /* ChromDriver нужно складывать в папку resouces, чтобы проект работал не только на локальном компе */
    protected WebDriver driver;

    /* этот метод загрузки нужного webDriver
    можно хром, можно firefox, adge,IE...
     нужно прописать,   чтобы руками не скачивать нужный webDriver,  ставим эту библиотеку
    testImplementation 'io.github.bonigarcia:webdrivermanager:5.6.3'*/
    @BeforeAll
    public static void downloadDriver() {
   /*     WebDriverManager.edgedriver().setup();
        WebDriverManager.iedriver().setup();
        WebDriverManager.firefoxdriver().setup();
        WebDriverManager.safaridriver().setup();   */
  //      WebDriverManager.chromedriver().setup();
        /* теперь это не нужно, библиотека webdrivermanager сама закачает нужный драйвер */
                  System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
    }

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        //FullHD
        driver.manage().window().setSize(new Dimension(1920, 1080));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(200));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(200));
  //      driver.get("https://www.wildberries.ru/");
    }

    @AfterEach
    public void ternDown() {
        driver.close();
    }
}
