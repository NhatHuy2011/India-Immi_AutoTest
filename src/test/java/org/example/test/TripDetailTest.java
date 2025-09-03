package org.example.test;

import org.example.pages.LoginPage;
import org.example.pages.TripDetailPage;
import org.example.utils.ExcelUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TripDetailTest {
    private WebDriver driver;
    private static final String FILE_PATH = "src/test/resources/TestCase.xlsx";

    @BeforeEach
    void setUp() {

    }

    @Test
    void testLogin() throws InterruptedException {
        List<String[]> testData = ExcelUtils.readExcel(FILE_PATH, "TripDetail");

        for (String[] row : testData) {
            String numbers = row[4];
            String purpose = row[5];
            String entryDate = row[6];
            String exitDate = row[7];
            String arrivalKeyword = row[8];
            String arrivalOption = row[9];
            String processingTime = row[10];

            driver = new ChromeDriver();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            driver.manage().window().maximize();
            driver.get("https://india-immi.org/vi/apply");
            TripDetailPage tripDetailPage = new TripDetailPage(driver);

            tripDetailPage.selectApplicants(numbers);
            Thread.sleep(1000);

            tripDetailPage.selectPurpose(purpose);
            Thread.sleep(1000);

            tripDetailPage.selectEntryDate(entryDate);
            Thread.sleep(1000);

            tripDetailPage.selectExitDate(exitDate);
            Thread.sleep(1000);

            tripDetailPage.searchAndSelectArrivalPort(arrivalKeyword, arrivalOption);
            Thread.sleep(1000);

            tripDetailPage.selectProcessingTime(processingTime);
            Thread.sleep(1000);

            tripDetailPage.clickContinue();
        }
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
