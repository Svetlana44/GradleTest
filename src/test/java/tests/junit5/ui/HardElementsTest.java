package tests.junit5.ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;
import java.util.List;

public class HardElementsTest {
    private WebDriver driver;

    @BeforeAll
    public static void downloadDriver() {
   /*     WebDriverManager.edgedriver().setup();
        WebDriverManager.iedriver().setup();
        WebDriverManager.firefoxdriver().setup();
        WebDriverManager.safaridriver().setup();   */

        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp() {
        /* теперь это не нужно, библиотека webdrivermanager сама закачает нужный драйвер */
        //          System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
//        ChromeOptions options = new ChromeOptions();
        /* бинарник-это кастомный запуск определённого браузера, напрмер, Яндекс
         * нужно найти его exe на диске и указать, чтобы запускать через Яндекс */
        //      options.setBinary("C:/Users/SpS/AppData/Local/Yandex/YandexBrowser/Application/browser.exe");

        /* браузер будет запущен в невидимом окне на удалённом сервере */
        /* учитывать таймаут загрузки */
        //      options.addArguments("--headless");
        /*     options.addArguments("--user agent=Mozila/12983h Webkit");   */

        /* чтобы опции применились, их нужно подставить в экземпляр при создании браузера */
        //   driver = new ChromeDriver(options);
        driver = new ChromeDriver();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        //FullHD
//        driver.manage().window().setSize(new Dimension(1920, 1080));
    }

    @AfterEach
    public void ternDown() {
        //     driver.quit();
        driver.close();
    }

    /* Basic authorization */
    @Test
    public void authTest() {
        driver.get("https://the-internet.herokuapp.com/basic_auth"); /*   без авторизации  */
        driver.get("https://admin:admin@the-internet.herokuapp.com/basic_auth");  /*   с авторизацией  */
        String h3 = driver.findElement(By.xpath("//h3")).getText();
        Assertions.assertEquals("Basic Auth", h3);
    }

    @Test
    public void alertOk() {
        String expectedText = "I am a JS Alert";
        driver.get("https://the-internet.herokuapp.com/javascript_alerts");
        driver.findElement(By.xpath("//button[@onclick='jsAlert()']"))
                .click();
        String actualText = driver.switchTo().alert()
                .getText();
        driver.switchTo().alert().accept();  /* нажать ОК на алерте */

        Assertions.assertEquals(expectedText, actualText);

        String result = driver.findElement(By.id("result")).getText();
        Assertions.assertEquals(result, "You successfully clicked an alert");
    }

    @Test
    public void iframeTest() {
        driver.get("https://mail.ru/");
        driver.findElement(By.xpath("//button[@class='resplash-btn resplash-btn_primary resplash-btn_mailbox-big fbkennh__1ei52a5']"))
                .click();

        WebElement iframe = driver.findElement(By.xpath("//iframe[@class='ag-popup__frame__layout__iframe']"));
        driver.switchTo().frame(iframe);

        driver.findElement(By.xpath("//input[@name='username']")).sendKeys("Привет!!!");
    }

    /*  обозначение следующего по дереву
    //div[text()='Pick one title']//following::div
    //div[text()='Pick one title']//following::div[text()='Mr.']  */
    @Test
    public void sliderTest() {
        driver.get("http://85.192.34.140:8081/");
        WebElement elementsCard = driver.findElement(By.xpath("//div[@class='card-body']//h5[text()='Widgets']"));
        elementsCard.click();

        WebElement elementsTextBox = driver.findElement(By.xpath("//span[text()='Slider']"));
        elementsTextBox.click();

        WebElement slider = driver.findElement(By.xpath("//input[@type='range']"));

        Actions actions = new Actions(driver);
        /*билдер собирает, перформер запускает*/
        actions.dragAndDropBy(slider, 50, 0).build().perform();

        int expectedValue = 85;
        int currentValue = Integer.parseInt(slider.getAttribute("value"));
        int valueToMove = expectedValue - currentValue;
        for (int i = 0; i < valueToMove; i++) {
            slider.sendKeys(Keys.ARROW_RIGHT);
        }

        WebElement sliderValue = driver.findElement(By.id("sliderValue"));
        int actualValue = Integer.parseInt(sliderValue.getAttribute("value"));
        Assertions.assertEquals(expectedValue, actualValue);
    }

    /*   открывается меню при наведении мышью, в меню 2 элемента  */
    @Test
    public void hoverTest() {
        driver.get("http://85.192.34.140:8081/");
        WebElement elementsCard = driver.findElement(By.xpath("//div[@class='card-body']//h5[text()='Widgets']"));
        elementsCard.click();

        WebElement elementsTextBox = driver.findElement(By.xpath("//span[text()='Menu']"));
        elementsTextBox.click();

        WebElement menuItemMiddle = driver.findElement(By.xpath("//a[text()='Main Item 2']"));
        Actions actions = new Actions(driver);
        actions.moveToElement(menuItemMiddle).build().perform();

        WebElement subSubList = driver.findElement(By.xpath("//a[text()='SUB SUB LIST »']"));
        actions.moveToElement(subSubList).build().perform();
        /*  несколько элементов find ElementS   */
        List<WebElement> lastElements = driver.findElements(By.xpath("//a[contains(text(),'Sub Sub Item')]"));
        Assertions.assertEquals(2, lastElements.size());
    }
}
