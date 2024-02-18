Для явной настройки запуска конкретного веб-драйвера (например, для Firefox или Microsoft Edge) в Selenium WebDriver, вы можете использовать соответствующие классы для инициализации драйвера. Ниже приведены примеры для Firefox и Edge драйверов:

Пример инициализации Firefox драйвера:
```java
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class Example {
    public static void main(String[] args) {
        System.setProperty("webdriver.gecko.driver", "путь_к_драйверу_для_Firefox");

        FirefoxOptions options = new FirefoxOptions();
        // Установка других опций Firefox...

        WebDriver driver = new FirefoxDriver(options);
    }
}
```

Пример инициализации Microsoft Edge драйвера:
```java
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class Example {
    public static void main(String[] args) {
        System.setProperty("webdriver.edge.driver", "путь_к_драйверу_для_Edge");

        EdgeOptions options = new EdgeOptions();
        // Установка других опций Edge...

        WebDriver driver = new EdgeDriver(options);
    }
}
```

Обратите внимание, что в обоих примерах вы также можете устанавливать другие опции, специфичные для каждого драйвера. Например, для Firefox драйвера вы можете установить опции головного лицевого предпочтения, а для Edge драйвера - другие специфические опции.

Также убедитесь, что у вас есть соответствующие драйверы (GeckoDriver для Firefox и EdgeDriver для Microsoft Edge) и что их путь указан в `System.setProperty()`.