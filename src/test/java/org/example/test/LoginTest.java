package org.example.test;

import org.example.pages.LoginPage;
import org.example.utils.ExcelUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginTest {

    private WebDriver driver;
    private static final String FILE_PATH = "src/test/resources/TestCase.xlsx";

    @BeforeEach
    void setUp() {
        // Driver khởi tạo trong vòng lặp test case thay vì @BeforeEach
    }

    @Test
    void testLogin() throws InterruptedException {
        List<String[]> testData = ExcelUtils.readExcel(FILE_PATH, "Login");

        int rowIndex = 1;
        for (String[] row : testData) {
            String emailValue = row[4];
            String passwordValue = row[5];
            String expected = row[6];
            String lang = row[9];

            // chọn URL theo ngôn ngữ
            String baseUrl = lang.equalsIgnoreCase("vi")
                    ? "https://india-immi.org/vi/login"
                    : "https://india-immi.org/en/login";

            driver = new ChromeDriver();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            driver.manage().window().maximize();
            driver.get(baseUrl);
            LoginPage loginPage = new LoginPage(driver);

            loginPage.enterEmail(emailValue);
            loginPage.enterPassword(passwordValue);
            loginPage.clickLogin();

            Thread.sleep(2000); // đợi thông báo

            String actualMessage = loginPage.getErrorMessage();
            boolean isTestPassed;

            try {
                assertEquals(expected.trim().toLowerCase(), actualMessage.toLowerCase(),
                        "Expected: " + expected + " nhưng nhận được: " + actualMessage);
                isTestPassed = true;
            } catch (AssertionError e) {
                isTestPassed = false;
            }

            // ghi kết quả vào Excel
            String status = isTestPassed ? "Pass" : "Fail";
            ExcelUtils.writeTestResults(FILE_PATH, "Login", rowIndex, actualMessage, status);

            driver.quit();
            rowIndex++;
        }
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}

