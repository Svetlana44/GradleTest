package listener;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/* для перезапуска упавших тестов
имплементуруем интерфейсы, первый обрабатывает ошибки, второй-отрабатывает логику после завершения теста
чтобы пользоваться этим утильным классом, нужно на др. класс повесить аннотацию @ExtendWith(RetryListener.class)
*/
public class RetryListenerJunit5 implements TestExecutionExceptionHandler, AfterTestExecutionCallback {

    private static final int MAX_RETRIES = 3;
    private static final Set<String> failedTestNames = new HashSet<>();

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        for (int i = 0; i < MAX_RETRIES; i++) {
            /* из контекста получаем запущенного метода
            invoke()  выполнить, аргументы берём из контекста  */
            try {
                context.getRequiredTestMethod().invoke(context.getRequiredTestInstances());
                return; /* метод завершается */
            } catch (Throwable ex) {
                throwable = ex.getCause();
            }
        }
        throw throwable;
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        Method method = context.getRequiredTestMethod();
        String testClass = context.getRequiredTestClass().getName();
        String testName = method.getName();
        String testToWrite = String.format("--tests %s.%s*", testClass, testName);
        context.getExecutionException().ifPresent(x -> failedTestNames.add(testToWrite));
    }

    public static void saveTests() throws IOException {
        /* получаем текущую директорию проекта */
        String output = System.getProperty("user.dir") + "/src/test/resources/FailedTests.txt";
        /* все тесты в одну строку */
        String result = String.join(" ", failedTestNames);
        FileUtils.writeStringToFile(new File(output), result);
    }
}
