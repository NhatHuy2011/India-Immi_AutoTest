package org.example.test;

import org.example.pages.TripDetailPage;
import org.example.utils.ExcelUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TripDetailTest {
    private WebDriver driver;
    private static final String FILE_PATH = "src/test/resources/TestCase.xlsx";

    @BeforeEach
    void setUp() {

    }

    @Test
    void testTripDetailForm() throws InterruptedException {
        List<String[]> testData = ExcelUtils.readExcel(FILE_PATH, "TripDetail");
        int rowIndex = 2;

        for (String[] row : testData) {
            String numbers = row[4];
            String purpose = row[5];
            String entryDate = row[6];
            String exitDate = row[7];
            String arrivalKeyword = row[8];
            String arrivalOption = row[9];
            String processingTime = row[10];
            String expected = row[11];

            driver = new ChromeDriver();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            driver.manage().window().maximize();
            driver.get("https://india-immi.org/vi/apply");
            TripDetailPage tripDetailPage = new TripDetailPage(driver);

            String actualMessage;
            boolean isTestPassed;

            try {
                tripDetailPage.selectApplicants(numbers);
                tripDetailPage.selectPurpose(purpose);

                tripDetailPage.selectEntryDate(entryDate);
                Thread.sleep(1000);

                tripDetailPage.selectExitDate(exitDate);
                Thread.sleep(1000);

                tripDetailPage.searchAndSelectArrivalPort(arrivalKeyword, arrivalOption);
                Thread.sleep(1000);

                tripDetailPage.selectProcessingTime(processingTime);

                boolean click = tripDetailPage.clickContinue();

                if (expected.contains(";")) {
                    //Nhiều lỗi
                    List<String> expectedErrors = Arrays.stream(expected.split(";"))
                            .map(String::trim)
                            .toList();

                    List<String> actualErrors = tripDetailPage.getAllErrorMessages();

                    isTestPassed = actualErrors.containsAll(expectedErrors)
                            && expectedErrors.containsAll(actualErrors); // so khớp 2 chiều

                    actualMessage = String.join(";", actualErrors);
                } else {
                    //1 Lỗi
                    actualMessage = click
                            ? "Form thông tin hành khách hiển thị"
                            : tripDetailPage.getErrorMessage();

                    isTestPassed = expected.trim().equalsIgnoreCase(actualMessage);
                }

            } catch (ElementClickInterceptedException e) {
                actualMessage = "Ngày không hợp lệ";
                isTestPassed = expected.trim().equalsIgnoreCase(actualMessage);
            } catch (Exception e) {
                actualMessage = e.getMessage();
                isTestPassed = expected.trim().equalsIgnoreCase(actualMessage);
            }

            String status = isTestPassed ? "Pass" : "Fail";
            ExcelUtils.writeTestResults(FILE_PATH, "TripDetail", rowIndex, actualMessage, 12, status, 13);

            driver.quit();
            rowIndex++;
        }
    }

    @Test
    void testOneTripDetail() throws InterruptedException {
        int targetRowIndex = 0; // TestData index
        int excelRowIndex = targetRowIndex + 2; // Ghi lại kết quả

        List<String[]> testData = ExcelUtils.readExcel(FILE_PATH, "TripDetail");
        String[] row = testData.get(targetRowIndex);

        String numbers = row[4];
        String purpose = row[5];
        String entryDate = row[6];
        String exitDate = row[7];
        String arrivalKeyword = row[8];
        String arrivalOption = row[9];
        String processingTime = row[10];
        String expected = row[11];

        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();
        driver.get("https://india-immi.org/vi/apply");
        TripDetailPage tripDetailPage = new TripDetailPage(driver);

        tripDetailPage.selectApplicants(numbers);
        tripDetailPage.selectPurpose(purpose);
        tripDetailPage.selectEntryDate(entryDate);
        Thread.sleep(1000);
        tripDetailPage.selectExitDate(exitDate);
        Thread.sleep(1000);
        tripDetailPage.searchAndSelectArrivalPort(arrivalKeyword, arrivalOption);
        Thread.sleep(2000);
        tripDetailPage.selectProcessingTime(processingTime);

        boolean click = tripDetailPage.clickContinue();
        String actualMessage = click
                ? "Form thông tin hành khách hiển thị"
                : tripDetailPage.getErrorMessage();

        boolean isTestPassed = expected.trim().equalsIgnoreCase(actualMessage);

        String status = isTestPassed ? "Pass" : "Fail";
        ExcelUtils.writeTestResults(FILE_PATH, "TripDetail", excelRowIndex, actualMessage, 12, status, 13);

        driver.quit();
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
