package tests.swaggertests;

import assertions.Conditions;
import io.qameta.allure.Attachment;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import listener.CustomTpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tests.junit5.api.FileService;

import java.io.File;

public class FilteTestsOld {
    private static FileService fileService;

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://85.192.34.140:8080/api";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(),
                CustomTpl.customLogFilter().withCustomTemplates());
        fileService = new FileService();
    }

    /*                  название файла             картинка    */
    @Attachment(value = "downloaded", type = "image/png")
    /*аннотация нужна, чтобы прикреплять это к алюр отчёту*/
    private byte[] attachFile(byte[] bytes) {
        return bytes;
    }

    @Test
    public void positiveDownloadTest() {
        byte[] file = fileService.downloadBaseImage().asResponse().asByteArray();
        /* это для allure передали скачанный файл */
        attachFile(file);
        File expectedFile = new File("src/test/resources/threadqa.jpeg");
        /*  измеряем длину массива байтов  */
        Assertions.assertEquals(expectedFile.length(), file.length);
    }

    @Test
    public void positiveUploadTest() {
        File expectedFile = new File("src/test/resources/threadqa.jpeg");
        fileService.uploadFile(expectedFile)
                .should(Conditions.hasStatusCode(200))
                .should(Conditions.hasMessage("file uploaded to server"));

        byte[] actualFile = fileService.downloadLastFile().asResponse().asByteArray();
        Assertions.assertTrue(actualFile.length != 0);
        Assertions.assertEquals(expectedFile.length(), actualFile.length);
    }
}

