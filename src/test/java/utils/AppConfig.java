package utils;

import org.aeonbits.owner.Config;

/* обязательно д.б. интерфейс

аннотация откуда брать исходики, относительный путь от ресурса resources */
@Config.Sources("classpath:propertyReader.properties")

public interface AppConfig extends Config {
    /* чтобы понять, какую настройку берёт метод указываем аннотацию @Key
    если не найдётся такой настройки, то напротив ключа пропишется null  */

    @Key("url")
    String url();

    @Key("is_production")
    Boolean isProduction();

    @Key("threads")
    Integer threads();
}