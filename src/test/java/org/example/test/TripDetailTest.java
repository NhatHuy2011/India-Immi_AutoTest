package org.example.test;

import org.example.pages.TripDetailPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

public class TripDetailTest {
    private WebDriver driver;
    private TripDetailPage tripDetailPage;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://india-immi.org/vi/apply");
        tripDetailPage = new TripDetailPage(driver);
    }

    @Test
    public void testFillTripDetailForm() throws InterruptedException {
        tripDetailPage.selectApplicants("5");
        Thread.sleep(1000); // Ch chờ 1 giây

        tripDetailPage.selectPurpose("e_tourist|5YM");
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
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
