package tests.base;

import common.CommonActions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import pages.base.BasePage;
import pages.loans.CarLoansPage;
import pages.telecommunications.MobilePhoneReplenishmentPage;

import static common.Config.CLEAR_COOKIES;
import static common.Config.HOLD_BROWSER_OPEN;

/*
аннотацией @Execution указали чтоб все тесты которые наследуются от BaseTest запускались многопоточно. Если
многопоточность не нужна можно просто закомментировать @Execution
 */
@Execution(ExecutionMode.CONCURRENT)

/*
Эта аннотация говорит что все аннотации в BaseTest они будут выполняться после прогона каждого класса. Единственным
минусом такой архитектуры теста будет являться то, что после выполнения определенного тестового класса
car_loans например, у нас будет отрываться браузер и потом браузер будет закрываться.
 */
/*
В BaseTest у нас прописан жизненный цикл всех тестов для каждого тестового класса. Это означает что @AfterEach and
@AfterAll будут выполняться для каждого класса по классово
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

// здесь будет происходить инициализация драйвера
public class BaseTest {
    /*
    Помимо создания и инициализации драйвера в tests.base у нас будут лежать в этом классе вспомогательные методы
    по работе с жизненным циклом test. Для того чтобы реализовать работу с жизненным циклом нам необходимо добавить
    аннотацию @TestInstance
    */

    // в BaseTest мы создадим драйвер через CommonActions.createDriver(), проинициализируем его и прокинем в
    // конструктор страницы BasePage. Аналогичным образом будем прокидывать его в конструкторы всех остальных страниц
    protected WebDriver driver = CommonActions.createDriver();

    // внутрь данного объекта страницы мы будем прокидывать драйвер
    protected BasePage basePage = new BasePage(driver);
    protected CarLoansPage carLoansPage = new CarLoansPage(driver);
    protected MobilePhoneReplenishmentPage mobilePhoneReplenishmentPage = new MobilePhoneReplenishmentPage(driver);

    @AfterEach
    // после каждого теста будем чистить cookies, чтоб каждый тест начинался как с нуля
    void clearCookiesAndLocalStorage() {
        if (CLEAR_COOKIES) {
            // если true, то удаляем cookies с помощью JavaScript кода
            JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
            driver.manage().deleteAllCookies();
            // указываем какой скрипт необходимо выполнить. Необходимо почистить все сессионные данные, которые
            // которые находятся в определенном окне
            javascriptExecutor.executeScript("window.sessionStorage.clear()");
        }
    }

    @AfterAll
    // данный метод будет заниматься закрытием браузера. Для данного метода создадим в классе Config
    // переменную HOLD_BROWSER_OPEN
    void close() {
        if (!HOLD_BROWSER_OPEN) {
            // если не true, то закрываем наш браузер
            driver.close();
        }
    }

}