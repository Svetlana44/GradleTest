package tests.junit5;

import lombok.SneakyThrows;
import models.Settings;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import utils.AppConfig;
import utils.JsonHelper;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/* для запуска выборочных тестов, вешаем тэги */
@Tag("API")

public class PropertyReaderTest {
    @Test
    @Tag("SMOKE")
    public void simpleReader() throws IOException {
        Properties properties = new Properties();
        FileInputStream in = new FileInputStream("src/test/resources//propertyReader.properties");
        properties.load(in);

        String url = properties.getProperty("url");
        boolean isProduction = Boolean.getBoolean(properties.getProperty("is_production"));
        int threads = Integer.parseInt(properties.getProperty("threads"));

        System.out.println(url);
        System.out.println(isProduction);
        System.out.println(threads);
    }

    @Test
    public void jacksonReader() throws IOException {
        Properties properties = new Properties();
        FileInputStream in = new FileInputStream("src/test/resources//propertyReader.properties");
        properties.load(in);

        String json = JsonHelper.toJson(properties);
        System.out.println(json);
    }

    @Test
    @SneakyThrows
    public void jacksonReaderSettings() {
        Properties properties = new Properties();
        FileInputStream in = new FileInputStream("src/test/resources//propertyReader.properties");
        properties.load(in);

        String json = JsonHelper.toJson(properties);
        System.out.println(json);

        Settings settings = JsonHelper.fromJsonString(json, Settings.class);
        System.out.println(settings);
    }

    @Test
    public void ownerReaderAnySettingsTest() {
        /*  AppConfig реализует Config
        у библиотеки Owner есть класс ConfigFactory, с методом для создания класса */
        AppConfig appConfig = ConfigFactory.create(AppConfig.class);
        System.out.println(appConfig.url());
        System.out.println(appConfig.isProduction());
        System.out.println(appConfig.threads());
    }
}
