package tests.junit5;

import lombok.SneakyThrows;
import models.CatWithLombok;
import org.junit.jupiter.api.Test;


/* настроить идею, чтобы позволяла в реальном времени вносить изменения в код
в anotation processing поставить галочку Enable annotation processing */

public class LombokTest {
    @Test
    @SneakyThrows /*можно не прописывать try/catch  */
    public void lombokTest() {
        CatWithLombok cat = new CatWithLombok("Barsik", "russian", 2, false);
        CatWithLombok catWhite = CatWithLombok.builder()
                .isWhite(true)
                .name("Kissy")
                .build();
        CatWithLombok cat2 = CatWithLombok.builder()
                .age(5)
                .build();
    }
}
