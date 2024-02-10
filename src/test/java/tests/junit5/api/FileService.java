package tests.junit5.api;

import assertions.AssertableResponse;
import io.restassured.http.ContentType;
import lombok.SneakyThrows;

import java.io.File;
import java.nio.file.Files;

import static io.restassured.RestAssured.given;

public class FileService {
    public AssertableResponse downloadBaseImage() {
        return new AssertableResponse(given().get("/files/download").then());
    }

    public AssertableResponse downloadLastFile() {
        return new AssertableResponse(given().get("/files/downloadLastUploaded").then());
    }

    /*отправка файлов на сервер */
    @SneakyThrows
    public AssertableResponse uploadFile(File file) {
        return new AssertableResponse(given()
                .contentType(ContentType.MULTIPART)
                /*что передаём-file            название файла        прочитать    из файла берём путь  */
                .multiPart("file", "myFile", Files.readAllBytes(file.toPath()))
                .post("/files/upload").then());
    }
}
