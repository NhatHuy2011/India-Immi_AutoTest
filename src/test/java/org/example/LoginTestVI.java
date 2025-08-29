package org.example;

import org.example.utils.ExcelUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginTestVI {

    private WebDriver driver;
    private static final String FILE_PATH = "src/test/resources/login.xlsx";

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();
        driver.get("https://india-immi.org/vi/login");
    }

    @Test
    void testLoginWithExcelData() throws InterruptedException {
        List<String[]> testData = ExcelUtils.readExcel(FILE_PATH, "Login_VI");

        int rowIndex = 1; // Start from row 1 (after header)
        for (String[] row : testData) {
            String emailValue = row[4];
            String passwordValue = row[5];
            String expected = row[6];

            // Mỗi test case mở browser riêng
            WebDriver driver = new ChromeDriver();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            driver.manage().window().maximize();
            driver.get("https://india-immi.org/vi/login");

            WebElement email = driver.findElement(By.name("email"));
            WebElement password = driver.findElement(By.name("password"));
            WebElement loginBtn = driver.findElement(By.xpath("//button[text()='Login to your account']"));

            email.sendKeys(emailValue);
            password.sendKeys(passwordValue);
            loginBtn.click();

            Thread.sleep(2000); // tạm chờ thông báo

            String actualMessage = "";
            boolean isTestPassed = false;

            // Kiểm tra thông báo ở nhiều vị trí
            try {
                // Thông báo dạng popup sau khi submit
                WebElement messageElement = driver.findElement(By.cssSelector("div[data-content] div[data-title]"));
                actualMessage = messageElement.getText().trim();
            } catch (NoSuchElementException e) {
                // Nếu không có popup thì kiểm tra lỗi input email
                try {
                    WebElement emailError = driver.findElement(
                            By.xpath("//input[@name='email']/following::p[contains(@class,'text-destructive')][1]")
                    );
                    actualMessage = emailError.getText().trim();
                } catch (NoSuchElementException ex) {
                    // Nếu không có emailError thì check lỗi password
                    WebElement passwordError = driver.findElement(
                            By.xpath("//input[@name='password']/following::p[contains(@class,'text-destructive')][1]")
                    );
                    actualMessage = passwordError.getText().trim();
                }
            }

            // So sánh kết quả và xác định trạng thái
            try {
                assertEquals(expected.trim().toLowerCase(), actualMessage.toLowerCase(),
                        "Expected: " + expected + " nhưng nhận được: " + actualMessage);
                isTestPassed = true; // Test passed if no assertion error
            } catch (AssertionError e) {
                isTestPassed = false; // Test failed if assertion error occurs
            }

            // Ghi kết quả vào file Excel
            String status = isTestPassed ? "Pass" : "Fail";
            ExcelUtils.writeTestResults(FILE_PATH, "Login_VI", rowIndex, actualMessage, status);

            driver.quit();
            rowIndex++; // Tăng chỉ số hàng cho lần lặp tiếp theo
        }
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}

