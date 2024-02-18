package tests.junit5.ui;

import com.codeborne.pdftest.PDF;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import static com.codeborne.selenide.Selenide.$x;

public class SelenideFileTests {
    @Test
    public void readPdfTest() throws IOException {
        File pdf = new File("src/test/resources/test.pdf");
        PDF pdfReader = new PDF(pdf);
        String allText = pdfReader.text;/* всё содержимое  */
        Calendar signatureTime = pdfReader.signatureTime; /*время подписани*/
        System.out.println(allText + "\n" + "----------------");
        System.out.println(signatureTime + " - signatureTime");
        Assertions.assertTrue(allText.contains("ПЕРЕНОСНЫЕ ДИАГНОСТИЧЕСКИЕ"));
    }

    @Test
    public void readPdfBrouserTest() throws IOException {
        /*  для примера https://www.pdf995.com/samples/pdf.pdf  */
        Selenide.open("https://www.pdf995.com/samples/");
        File file = $x("//td[@data-sort='pdf.pdf']/a").download();

        PDF pdfReader = new PDF(file);

        Assertions.assertEquals("Software 995", (pdfReader).author);
        Assertions.assertTrue(pdfReader.text.contains("Please visit"));
    }
}
