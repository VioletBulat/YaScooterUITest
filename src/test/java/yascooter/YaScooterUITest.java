package yascooter;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.Assert.*;

public class YaScooterUITest {

    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void setUp() {
        //ChromeOptions options = new ChromeOptions();
        //options.addArguments("--no-sandbox", "--headless", "--disable-dev-shm-usage");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void clickAccordionButtonsTest() {
        driver.get("https://qa-scooter.praktikum-services.ru/");

        // Создаем список кнопок
        List<WebElement> buttons = driver.findElements(By.xpath("//div[@class='accordion__button']"));

        for (WebElement button : buttons) {
            // Прокрутка до кнопки
            scrollToElement(button);
            wait.until(ExpectedConditions.elementToBeClickable(button));
            button.click();

            // Проверяем, что текст высветился
            By hiddenElementLocator = By.xpath("//div[@class='accordion__content']//div[@class='hidden']");
            Boolean isElementHidden = wait.until(ExpectedConditions.invisibilityOfElementLocated(hiddenElementLocator));
            assertTrue("Элемент с классом 'hidden' не скрыт после клика на кнопку", isElementHidden);
        }
    }

    private void scrollToElement(WebElement element) {
        // Прокручиваем страницу до элемента с помощью метода WebElement.scrollIntoView
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        // Ожидание немного, чтобы убедиться, что элемент виден
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    @Test
    public void orderScooterTest() {
        driver.get("https://qa-scooter.praktikum-services.ru/");

        // Нажимаем на кнопку Заказать
        WebElement orderButton = driver.findElement(By.xpath("//button[@class='Button_Button__ra12g Button_Middle__1CSJM']"));
        scrollToElement(orderButton);
        wait.until(ExpectedConditions.elementToBeClickable(orderButton));
        orderButton.click();

        // Заполнение формы по плейсхолдерам
        fillInputByPlaceholder("* Имя", "Иван");
        fillInputByPlaceholder("* Фамилия", "Иванов");
        fillInputByPlaceholder("* Адрес: куда привезти заказ", "Москва, ул. Пушкина, 1");

        // Находим элемент с выпадающим списком
        WebElement station = driver.findElement(By.xpath("//input[@placeholder='* Станция метро']"));
        station.click();
        // Имитируем перемещение вниз и кликаем
        station.sendKeys(Keys.ARROW_DOWN);
        station.sendKeys(Keys.ENTER);

        fillInputByPlaceholder("* Телефон: на него позвонит курьер", "+75312575235");

        // Нажимаем кнопку Далее
        WebElement nextButton = driver.findElement(By.xpath(".//button[@class='Button_Button__ra12g Button_Middle__1CSJM']"));
        nextButton.click();

        // Находим элемент с выпадающим списком
        WebElement date = driver.findElement(By.xpath("//input[@placeholder='* Когда привезти самокат']"));
        date.click();
        // Имитируем перемещение вниз и кликаем
        date.sendKeys(Keys.ARROW_DOWN);
        date.sendKeys(Keys.ENTER);

        WebElement rentalButton = driver.findElement(By.xpath("//div[@class='Dropdown-placeholder' and contains(text(), '* Срок аренды')]"));
        rentalButton.click();
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='Dropdown-option' and contains(text(), 'сутки')]")));
        submitButton.click();

        WebElement label = driver.findElement(By.xpath("//label[@for='black']"));
        label.click();

        fillInputByPlaceholder("Комментарий для курьера", "Комментарий");

        // Найти кнопку по классу
        WebElement makeOrderButton = driver.findElement(By.xpath("//button[@class='Button_Button__ra12g Button_Middle__1CSJM' and text()='Заказать']"));
        makeOrderButton.click();

        WebElement yesButton = driver.findElement(By.xpath("//button[@class='Button_Button__ra12g Button_Middle__1CSJM' and text()='Да']"));
        yesButton.click();

        WebElement orderSuccess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(), 'Заказ оформлен')]")));
        assertNotNull("Элемент не найден", orderSuccess);
    }

    // Метод для заполнения полей ввода по плейсхолдеру
    private void fillInputByPlaceholder(String placeholder, String value) {
        WebElement inputField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='" + placeholder + "']")));
        inputField.clear();
        inputField.sendKeys(value);
    }

    @After
    public void teardown() {
        driver.quit();
    }
}
