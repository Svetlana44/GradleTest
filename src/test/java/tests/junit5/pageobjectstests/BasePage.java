package tests.junit5.pageobjectstests;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected JavascriptExecutor js;
    protected Actions actions;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        js = (JavascriptExecutor) driver;
        actions = new Actions(driver);
    }

    public String getTextJs(By element) {
        return (String) js.executeScript("return arguments[0].textContent;",
                driver.findElement(element));
    }

    public void jsClick(By element) {
        js.executeScript("arguments[0].click", driver.findElement(element));
    }

    /* значок загрузки  */
//    public void waitPageLoads() {
//        wait.until(ExpectedConditions.invisibilityOfElementLocated(pageLoader));
//    }

    /* это тоже самое что и сверху метод для Wildberries */
    public void waitPageLoadsWb() {
        By pageLoader = By.xpath("//div[@class='general-preloader']");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(pageLoader));
    }

    public void waitForElementUpdated(By locator) {
        wait.until(ExpectedConditions.stalenessOf(driver.findElement(locator)));
    }

    public void clearTextFieldFull(By locator) {
        driver.findElement(locator).clear();
        driver.findElement(locator).sendKeys(Keys.LEFT_CONTROL + "A");
        driver.findElement(locator).sendKeys(Keys.BACK_SPACE);
    }

    public WebElement waitForTextPresentedInList(By list, String value) {
        //  wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(list)); /* ждём пока список весь появится  */
        wait.until(ExpectedConditions.elementToBeClickable(list));
        return driver.findElements(list).stream()
                .filter(x -> x.getText().contains(value))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Города нет " + value));
    }

    public void waitForTextMatchesRegex(By locator, String regex) {
        Pattern pattern = Pattern.compile(regex);
        wait.until(ExpectedConditions.textMatches(locator, pattern));
    }

    /*  ждём, пока элемент не пропадёт со страницы */
    public void waitForElementDisappear(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /*  ждём появления элемента на странице */
    public void waitForElementAppear(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public Integer getDigitFromWebElement(WebElement element) {
        String text = element.getText().replaceAll("[^0-9.]", "");
        return Integer.parseInt(text);
    }


    public List<Integer> getDigitsFromList(By locator) {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
        return driver.findElements(locator).stream()
                .filter(x -> x.isDisplayed())                     /*  видимые элементы */
                .map(x -> getDigitFromWebElement(x))              /* из каждого элемента взять число  */
                .collect(Collectors.toList());
    }


}
