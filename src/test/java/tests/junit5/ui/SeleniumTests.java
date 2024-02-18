package tests.junit5.ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/* ChromDriver нужно складывать в папку resouces, чтобы проект работал не только на локальном компе */

public class SeleniumTests {
    private WebDriver driver;
    private String downloaderFolder = System.getProperty("user.dir") + File.separator + "build" + File.separator + "downloadFiles";

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

        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp() {
        /* теперь это не нужно, библиотека webdrivermanager сама закачает нужный драйвер */
       //        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        /* бинарник-это кастомный запуск определённого браузера, напрмер, Яндекс
         * нужно найти его exe на диске и указать, чтобы запускать через Яндекс */
        //      options.setBinary("C:/Users/SpS/AppData/Local/Yandex/YandexBrowser/Application/browser.exe");

        /* браузер будет запущен в невидимом окне на удалённом сервере */
        /* учитывать таймаут загрузки */
        //      options.addArguments("--headless");

        /*     options.addArguments("--user agent=Mozila/12983h Webkit");   */

        /* настройка, куда сохранять скачанные файлы  */
        Map<String, String> prefs = new HashMap<>();
        /* здесь обязательно абсолютный путь, но с системной директорией */
        prefs.put("download.default_directory", downloaderFolder);
        options.setExperimentalOption("prefs", prefs);
        System.out.println(downloaderFolder);

        /* чтобы опции применились, их нужно подставить в экземпляр при создании браузера */
     //   driver = new ChromeDriver(options);
                driver = new ChromeDriver();
        //FullHD
        //    driver.manage().window().setSize(new Dimension(1920, 1080));


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

    @Test
    public void simpleFormTest() {
        driver.get("http://85.192.34.140:8081/");
        WebElement elementsCard = driver.findElement(By.xpath("//div[@class='card-body']//h5[text()='Elements']"));
        elementsCard.click();

        WebElement elementsTextBox = driver.findElement(By.xpath("//span[text()='Text Box']"));
        elementsTextBox.click();

        WebElement fullName = driver.findElement(By.id("userName"));
        WebElement email = driver.findElement(By.id("userEmail"));
        WebElement currentAddress = driver.findElement(By.id("currentAddress"));
        WebElement permanentAddress = driver.findElement(By.id("permanentAddress"));
        WebElement submit = driver.findElement(By.id("submit"));

//        By nameNewBy = By.id();
        String expectedName = "Tomas Anderson";
        String expectedEmail = "Tomas@matrix.ru";
        String expectedCurrentAddress = "UA Los Angeles";
        String expectedPermanentAddress = "US Miami";

        fullName.sendKeys(expectedName);
        email.sendKeys(expectedEmail);
        currentAddress.sendKeys(expectedCurrentAddress);
        permanentAddress.sendKeys(expectedPermanentAddress);
        submit.click();

        WebElement fullNameNew = driver.findElement(By.xpath("//div[@id='output']//p[@id='name']"));
        WebElement emailNew = driver.findElement(By.xpath("//div[@id='output']//p[@id='email']"));
        WebElement currentAddressNew = driver.findElement(By.xpath("//div[@id='output']//p[@id='currentAddress']"));
        WebElement permanentAddressNew = driver.findElement(By.xpath("//div[@id='output']//p[@id='permanentAddress']"));

        String actualName = fullNameNew.getText();
        String actualEmail = emailNew.getText();
        String actualCurrentAddress = currentAddressNew.getText();
        String actualPermanentAddress = permanentAddressNew.getText();

        Assertions.assertTrue(actualName.contains(expectedName));
        Assertions.assertTrue(actualEmail.contains(expectedEmail));
        Assertions.assertTrue(actualCurrentAddress.contains(expectedCurrentAddress));
        Assertions.assertTrue(actualPermanentAddress.contains(expectedPermanentAddress));

    }

    @Test
    public void uploadAndDownloadTest() throws InterruptedException {
        driver.get("http://85.192.34.140:8081/");
        WebElement elementsCard = driver.findElement(By.xpath("//div[@class='card-body']//h5[text()='Elements']"));
        elementsCard.click();

        WebElement elementsTextBox = driver.findElement(By.xpath("//span[text()='Upload and Download']"));
        elementsTextBox.click();

        WebElement uploadBtn = driver.findElement(By.id("uploadFile"));
        /* здесь обязательно абсолютный путь, но с системной директорией */
        uploadBtn.sendKeys(System.getProperty("user.dir") + "/src/test/resources/threadqa.jpeg");
        /* Для загрузки файла нужно искать элемент с input */
        WebElement str = driver.findElement(By.id("uploadedFilePath"));

        Assertions.assertTrue(str.getText().contains("threadqa.jpeg"));
    }

    @Test
    public void downloadTest() throws InterruptedException {
        driver.get("http://85.192.34.140:8081/");
        WebElement elementsCard = driver.findElement(By.xpath("//div[@class='card-body']//h5[text()='Elements']"));
        elementsCard.click();

        WebElement elementsTextBox = driver.findElement(By.xpath("//span[text()='Upload and Download']"));
        elementsTextBox.click();

        WebElement downloadBtn = driver.findElement(By.id("downloadButton"));
        downloadBtn.click();

        /* ожидание загрузки файла */
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(x -> Paths.get(downloaderFolder, "stiker.png").toFile().exists());

        File file = new File("build/downloadFiles/sticker.png");
        Assertions.assertNotNull(file);
        Assertions.assertTrue(file.length() > 0);
    }
}
