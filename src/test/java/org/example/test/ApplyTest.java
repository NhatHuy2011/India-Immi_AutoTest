package org.example.test;

import org.example.pages.ContactInfoPage;
import org.example.pages.PassengerInfoPage;
import org.example.pages.TripDetailPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

public class ApplyTest {
    private WebDriver driver;
    private TripDetailPage tripDetailPage;
    private PassengerInfoPage passengerInfoPage;
    private ContactInfoPage contactInfoPage;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://india-immi.org/vi/apply");
        tripDetailPage = new TripDetailPage(driver);
        passengerInfoPage = new PassengerInfoPage(driver);
        contactInfoPage = new ContactInfoPage(driver);
    }

    @Test
    public void testFillTripDetailForm() throws InterruptedException {
        //Trip Detail
        tripDetailPage.selectApplicants("1");
        Thread.sleep(1000);

        tripDetailPage.selectPurpose("e_tourist|1MD");
        Thread.sleep(1000);

        tripDetailPage.selectEntryDate("2025-09-10");
        Thread.sleep(1000);

        tripDetailPage.selectExitDate("2025-09-20");
        Thread.sleep(1000);

        tripDetailPage.searchAndSelectArrivalPort("Indira", "Indira Gandhi Int'l Airport");
        Thread.sleep(1000);

        tripDetailPage.selectProcessingTime("urgent");
        Thread.sleep(1000);

        tripDetailPage.clickContinue();
        Thread.sleep(1000);

        //Passenger Info
        passengerInfoPage.sendKeyFullnameInput("Nhat Huy");
        Thread.sleep(1000);

        passengerInfoPage.selectGender("2");
        Thread.sleep(1000);

        passengerInfoPage.selectDob("2003-08-04");
        Thread.sleep(1000);

        passengerInfoPage.searchAndSelectNationality("Vi", "Vietnam");
        Thread.sleep(1000);

        passengerInfoPage.searchAndSelectNation("Vi", "Vietnam");
        Thread.sleep(1000);

        passengerInfoPage.sendKeyPassport("111111111111");
        Thread.sleep(1000);

        passengerInfoPage.selectExpired("2025-12-31");
        Thread.sleep(1000);

        passengerInfoPage.uploadPassport("C:\\Users\\ADMIN\\Downloads\\image.jpg");
        Thread.sleep(1000);

        passengerInfoPage.uploadPortrait("C:\\Users\\ADMIN\\Downloads\\image.jpg");
        Thread.sleep(1000);

        passengerInfoPage.clickSave();
        Thread.sleep(1000);

        //Contact Info
        contactInfoPage.setContactTitleButton("2");
        Thread.sleep(1000);

        contactInfoPage.setContactFullnameInput("Nhat Huy");
        Thread.sleep(1000);

        contactInfoPage.searchAndSelectPhoneInput("Vi", "Vietnam");
        Thread.sleep(1000);

        contactInfoPage.setPhoneInput("3636363636");
        Thread.sleep(1000);

        contactInfoPage.setPrimaryEmailInput("huyhuy1111@gmail.com");
        Thread.sleep(1000);

        contactInfoPage.clickConfirm();
        Thread.sleep(1000);

        contactInfoPage.clickAccept();
        Thread.sleep(1000);

        contactInfoPage.clickContinue();
        Thread.sleep(1000);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
