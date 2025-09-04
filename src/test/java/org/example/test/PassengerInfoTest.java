package org.example.test;

import org.example.pages.PassengerInfoPage;
import org.example.pages.TripDetailPage;
import org.example.utils.ExcelUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;
import java.util.List;

public class PassengerInfoTest {
    private WebDriver driver;
    private static final String FILE_PATH = "src/test/resources/TestCase.xlsx";

    @BeforeEach
    void setUp() {

    }

    @Test
    @SuppressWarnings("BusyWait")
    void testPassengerInfoFormWith2Passenger() throws InterruptedException {
        List<String[]> testDataTripDetail = ExcelUtils.readExcel(FILE_PATH, "TripDetail");
        List<String[]> testDataPassenger = ExcelUtils.readExcel(FILE_PATH, "PassengerInfo");

        //Trip Detail
        String [] tripDetailPass = testDataTripDetail.get(1);
        String numbers = tripDetailPass[4];
        String purpose = tripDetailPass[5];
        String entryDate = tripDetailPass[6];
        String exitDate = tripDetailPass[7];
        String arrivalKeyword = tripDetailPass[8];
        String arrivalOption = tripDetailPass[9];
        String processingTime = tripDetailPass[10];

        //Passenger Info
        String[] row = testDataPassenger.get(0);

        String fullname = row[4];
        String gender = row[5];
        String dob = row[6];
        String nationKey = row[7];
        String nationOption = row[8];
        String nationalityKey = row[9];
        String nationalityOption = row[10];
        String passport = row[11];
        String expired = row[12];
        String passportImage = row[13];
        String portraitImage = row[14];

        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();
        driver.get("https://india-immi.org/vi/apply");
        TripDetailPage tripDetailPage = new TripDetailPage(driver);
        PassengerInfoPage passengerInfoPage = new PassengerInfoPage(driver);

        //Trip Detail
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
        Thread.sleep(1000);

        for (int i = 1; i<=Integer.parseInt(numbers); i++){
            //PassengerInfo
            passengerInfoPage.sendKeyFullnameInput(fullname);
            Thread.sleep(1000);

            passengerInfoPage.selectGender(gender);
            Thread.sleep(1000);

            passengerInfoPage.selectDob(dob);
            Thread.sleep(1000);

            passengerInfoPage.searchAndSelectNation(nationKey, nationOption);
            Thread.sleep(1000);

            passengerInfoPage.searchAndSelectNationality(nationalityKey, nationalityOption);
            Thread.sleep(1000);

            passengerInfoPage.sendKeyPassport(passport);
            Thread.sleep(1000);

            passengerInfoPage.selectExpired(expired);
            Thread.sleep(1000);

            passengerInfoPage.uploadPassport(passportImage);
            Thread.sleep(1000);

            passengerInfoPage.uploadPortrait(portraitImage);
            Thread.sleep(1000);

            passengerInfoPage.clickSave(i, Integer.parseInt(numbers));
            Thread.sleep(1000);
        }

        driver.quit();
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
