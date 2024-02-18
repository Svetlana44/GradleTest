package tests.junit5.pageobjectstests.selenide;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tests.junit5.pageobjectstests.unitikets.UtMainSelenidePage;

/* селенин под капотом содержит и инициализирует webdriver */
public class UtSelenideTest {

    @BeforeEach
    public void initSettings() {
        /*  Configuration.browserBinary="path";  настройка селениума, через какой браузер запускать, нужно указать бинарник
         * Configuration.screenshots=true;  селенид делает скриншоты при падении тестов
         * Configuration.downloadsFolder="src/test/resources"; куда скачитьвать файлы
         *
         * */
        Configuration.timeout = 60_000; /*   по умолчанию 30 сек  , м.б. мало*/
    }

    @Test
    public void exempleTest() {
//        Selenide.open("https://uniticket.ru/");
//        SelenideElement header = $x("//h1");  /* нашли элемент по xPath*/
//        header.getText();
//        header.should(Condition.partialText("Поиск дешевых авиабилетов"));  /*  содержит часть текста */
//        header.scrollTo(); /* пролистать элемент */
//        header.hover();/*  навести мышкой на элемент  */
//        /* $$  $$x   поиск по коллекции  */
//        /*  WebDriverRunner  */
//        Selenide.page(UtSearchSelenidePage.class);
//        ElementsCollection elements = $$x("//input");
//        elements.find(Condition.partialText("часть текста")).click();
//        elements.texts(); /* список текстов */
//        elements.should(CollectionCondition.exactTexts());
    }

    @Test
    public void firstSelenideTest() {
        int expectedDayForward = 25;
        int expectedDayBack = 29;

        Selenide.open("https://uniticket.ru/");

        UtMainSelenidePage mainPage = new UtMainSelenidePage();
        mainPage.setCityFrom("Казань")
                .setCityTo("Дубай")
                .setDayForward(expectedDayForward)
                .setDayBack(expectedDayBack)
                .search()
                .waitForPage()
                .waitForTitleDisappear()
                .assertMainDayBack(expectedDayBack)
                .assertMainDayForward(expectedDayForward)
                .assertAllDaysBackShouldHaveDay(expectedDayBack)
                .assertAllDaysForwardShouldHaveDay(expectedDayForward);
    }
}
