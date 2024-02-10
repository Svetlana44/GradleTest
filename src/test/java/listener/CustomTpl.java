package listener;

import io.qameta.allure.restassured.AllureRestAssured;

/*  описание фильтрации логирования, чтобы не вешать на все классы укажем его в SimpleApiRefactoredParametrizedTest
вместо new AllureRestAssured());

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://fakestoreapi.com";
        /* логирование фильтруем  */
/*        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(),
                new AllureRestAssured());
                }
  */
public class CustomTpl {
    private static final AllureRestAssured FILTER = new AllureRestAssured();

    private CustomTpl() {
    }

    public static CustomTpl customLogFilter() {
        return InitLogFilter.logFilter;
    }

    public AllureRestAssured withCustomTemplates() {
        FILTER.setRequestTemplate("request.ftl");
        FILTER.setResponseTemplate("response.ftl");
        return FILTER;
    }

    private static class InitLogFilter {
        private static final CustomTpl logFilter = new CustomTpl();
    }
}

