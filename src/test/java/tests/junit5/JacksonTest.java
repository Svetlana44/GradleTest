package tests.junit5;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.People;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class JacksonTest {
    @Test
    public void testJackson() throws IOException {
        /* для прочтения json  */
        ObjectMapper objectMapper = new ObjectMapper();
        /* читаем json
        путь указывается до папки resources , проверить доступность файла CTRL+клик */
        File file = new File("src/test/resources/stas.json");
        /*   от куда и что читаем-в какой объект преобразуем
        у объекта д.б. и пустой конструктор и гетеры/сетеры  */
        People people = objectMapper.readValue(file, People.class);

        People sasha = new People("sasha", "10", "female");
        String json = objectMapper.writeValueAsString(sasha);
        System.out.println(json);

        People reSasha = objectMapper.readValue(json, People.class);
        System.out.println(reSasha);
    }
}