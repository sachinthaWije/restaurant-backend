package sachi.dev.restaurant.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class CustomerQueryTest {
    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
    }

    @Test
    public void customerSendQueryTest() {
        String jwtToken = "VALID CUSTOMER TOKEN";

        String restaurantId = "VALID RESTAURANT ID";
        driver.get("http://localhost:3000/customer/query/" + restaurantId);

        driver.manage().addCookie(new Cookie("Authorization", "Bearer " + jwtToken));

        WebElement subjectField = driver.findElement(By.name("subject"));
        WebElement messageField = driver.findElement(By.name("message"));
        WebElement sendButton = driver.findElement(By.xpath("//button[@type='submit']"));

        subjectField.sendKeys("Opening Hours");
        messageField.sendKeys("Could you please let me know the opening hours for your restaurant?");

        sendButton.click();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
