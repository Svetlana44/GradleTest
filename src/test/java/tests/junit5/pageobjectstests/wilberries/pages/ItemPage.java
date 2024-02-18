package tests.junit5.pageobjectstests.wilberries.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import tests.junit5.pageobjectstests.BasePage;

public class ItemPage extends BasePage {

    private By itemHeaderName = By.xpath("//div[@class='product-page__header']//h1");
    private By itemPrice = By.xpath("//span[@class='price-block__price']");

    public ItemPage(WebDriver driver) {
        super(driver);
    }

    public String getItemName() {
        return driver.findElement(itemHeaderName).getText();
    }

    public Integer getItemPrice() {
        /*js скрипт для взаимодействия с элементом, который м. скопировать из devtools
        можно передать неск элементов  */
        //   String priceText = (String) js.executeScript("return arguments[0].textContent;", itemPriceElement);
        String priceText = getTextJs(itemPrice);
        priceText = priceText.replaceAll("[^0-9.]", "");
        return Integer.parseInt(priceText);
    }
}
