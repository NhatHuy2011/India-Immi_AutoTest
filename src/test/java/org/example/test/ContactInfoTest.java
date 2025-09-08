package org.example.test;

import org.example.enums.ContinueContactInfo;
import org.example.enums.SaveResultPassengerInfo;
import org.example.pages.ContactInfoPage;
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
import java.util.Arrays;
import java.util.List;

public class ContactInfoTest {
    private WebDriver driver;
    private static final String FILE_PATH = "src/test/resources/TestCase.xlsx";

    @BeforeEach
    void setUp() {

    }

    @Test
    void testContactInfoFormWith1Passenger() throws InterruptedException {
        // Đọc dữ liệu từ Excel
        List<String[]> testDataTripDetail = ExcelUtils.readExcel(FILE_PATH, "TripDetail");
        List<String[]> testDataPassenger = ExcelUtils.readExcel(FILE_PATH, "PassengerInfo");
        List<String[]> testDataContact = ExcelUtils.readExcel(FILE_PATH, "ContactInfo");

        int rowIndex = 2;
        for (String[] row : testDataContact) {
            // Khởi tạo driver
            driver = new ChromeDriver();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            driver.manage().window().maximize();
            driver.get("https://india-immi.org/vi/apply");
            TripDetailPage tripDetailPage = new TripDetailPage(driver);
            PassengerInfoPage passengerInfoPage = new PassengerInfoPage(driver);
            ContactInfoPage contactInfoPage = new ContactInfoPage(driver);

            // Lấy thông tin trip detail
            String[] tripDetailPass = testDataTripDetail.get(0);
            String numbers = tripDetailPass[4];
            String purpose = tripDetailPass[5];
            String entryDate = tripDetailPass[6];
            String exitDate = tripDetailPass[7];
            String arrivalKeyword = tripDetailPass[8];
            String arrivalOption = tripDetailPass[9];
            String processingTime = tripDetailPass[10];

            // Step 1: Điền thông tin Trip Detail
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

            // Lấy thông tin passenger
            String[] passengerInfoPass = testDataPassenger.get(0);
            String passengerFullname = passengerInfoPass[4];
            String gender = passengerInfoPass[5];
            String dob = passengerInfoPass[6];
            String nationalityKey = passengerInfoPass[7];
            String nationalityOption = passengerInfoPass[8];
            String nationKey = passengerInfoPass[9];
            String nationOption = passengerInfoPass[10];
            String passport = passengerInfoPass[11];
            String expired = passengerInfoPass[12];
            String passportImage = passengerInfoPass[13];
            String portraitImage = passengerInfoPass[14];

            // Điền vào form passenger
            passengerInfoPage.sendKeyFullnameInput(passengerFullname);
            Thread.sleep(1000);
            passengerInfoPage.selectGender(gender);
            Thread.sleep(1000);
            passengerInfoPage.selectDob(dob);
            Thread.sleep(1000);
            passengerInfoPage.searchAndSelectNationality(nationalityKey, nationalityOption);
            Thread.sleep(1000);
            passengerInfoPage.searchAndSelectNation(nationKey, nationOption);
            Thread.sleep(1000);
            passengerInfoPage.sendKeyPassport(passport);
            Thread.sleep(1000);
            passengerInfoPage.selectExpired(expired);
            Thread.sleep(1000);
            passengerInfoPage.uploadPassport(passportImage);
            Thread.sleep(1000);
            passengerInfoPage.uploadPortrait(portraitImage);
            Thread.sleep(1000);
            passengerInfoPage.clickSave(1, 1);
            Thread.sleep(1000);

            //Lay thong tin contact info
            String contactFullname = row[4];
            String phoneContact = row[5];
            String primaryEmail = row[6];
            int confirm = Integer.parseInt(row[7]);
            int accept = Integer.parseInt(row[8]);
            String expected = row[9];

            //Dien thong tin contact info vao form contact
            contactInfoPage.setContactFullnameInput(contactFullname);
            Thread.sleep(1000);

            contactInfoPage.setPhoneInput(phoneContact);
            Thread.sleep(1000);

            contactInfoPage.setPrimaryEmailInput(primaryEmail);
            Thread.sleep(1000);

            contactInfoPage.clickConfirm(confirm);
            Thread.sleep(1000);

            contactInfoPage.clickAccept(accept);
            Thread.sleep(1000);

            ContinueContactInfo clickContinue = contactInfoPage.clickContinue(1, 1);
            List<String> actualErrors = contactInfoPage.getAllErrorMessages();

            String actualMessage;
            boolean isTestPassed;

            if (clickContinue == ContinueContactInfo.PAYMENT_PAGE) {
                actualMessage = "Form thanh toán hiển thị";
                isTestPassed = expected.equalsIgnoreCase(actualMessage);
            } else {
                actualMessage = String.join(";", actualErrors);
                isTestPassed = actualMessage.equalsIgnoreCase(expected);
            }

            String status = isTestPassed ? "Pass" : "Fail";
            ExcelUtils.writeTestResults(FILE_PATH, "ContactInfo", rowIndex, actualMessage, 10, status, 11);
            driver.quit();
            rowIndex++;
        }
    }

    @Test
    void testContactInfoFormWith2Passenger() throws InterruptedException {
        // Đọc dữ liệu từ Excel
        List<String[]> testDataTripDetail = ExcelUtils.readExcel(FILE_PATH, "TripDetail");
        List<String[]> testDataPassenger = ExcelUtils.readExcel(FILE_PATH, "PassengerInfo");
        List<String[]> testDataContact = ExcelUtils.readExcel(FILE_PATH, "ContactInfo");

        // Khởi tạo driver
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();
        driver.get("https://india-immi.org/vi/apply");
        TripDetailPage tripDetailPage = new TripDetailPage(driver);
        PassengerInfoPage passengerInfoPage = new PassengerInfoPage(driver);
        ContactInfoPage contactInfoPage = new ContactInfoPage(driver);

        // Lấy thông tin trip detail
        String[] tripDetailPass = testDataTripDetail.get(1);
        String numbers = tripDetailPass[4];
        String purpose = tripDetailPass[5];
        String entryDate = tripDetailPass[6];
        String exitDate = tripDetailPass[7];
        String arrivalKeyword = tripDetailPass[8];
        String arrivalOption = tripDetailPass[9];
        String processingTime = tripDetailPass[10];

        // Step 1: Điền thông tin Trip Detail
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

        int passengerCount = Integer.parseInt(numbers);

        // Lấy thông tin passenger
        int passengerStart = 0;
        int passengerEnd = 1;

        for (int i = passengerStart; i<=passengerEnd; i++){
            String[] passengerInfoPass = testDataPassenger.get(i);
            String passengerFullname = passengerInfoPass[4];
            String gender = passengerInfoPass[5];
            String dob = passengerInfoPass[6];
            String nationalityKey = passengerInfoPass[7];
            String nationalityOption = passengerInfoPass[8];
            String nationKey = passengerInfoPass[9];
            String nationOption = passengerInfoPass[10];
            String passport = passengerInfoPass[11];
            String expired = passengerInfoPass[12];
            String passportImage = passengerInfoPass[13];
            String portraitImage = passengerInfoPass[14];

            // Điền vào form passenger
            passengerInfoPage.sendKeyFullnameInput(passengerFullname);
            Thread.sleep(1000);
            passengerInfoPage.selectGender(gender);
            Thread.sleep(1000);
            passengerInfoPage.selectDob(dob);
            Thread.sleep(1000);
            passengerInfoPage.searchAndSelectNationality(nationalityKey, nationalityOption);
            Thread.sleep(1000);
            passengerInfoPage.searchAndSelectNation(nationKey, nationOption);
            Thread.sleep(1000);
            passengerInfoPage.sendKeyPassport(passport);
            Thread.sleep(1000);
            passengerInfoPage.selectExpired(expired);
            Thread.sleep(1000);
            passengerInfoPage.uploadPassport(passportImage);
            Thread.sleep(1000);
            passengerInfoPage.uploadPortrait(portraitImage);
            Thread.sleep(1000);
            passengerInfoPage.clickSave(i + 1, passengerCount);
            Thread.sleep(1000);
        }

        //Lay thong tin contact info
        int targetRowIndexContact = 7;
        int excelRowIndexContact = targetRowIndexContact + 2;

        String[] contactInfoData = testDataContact.get(targetRowIndexContact);
        String contactFullname = contactInfoData[4];
        String phoneContact = contactInfoData[5];
        String primaryEmail = contactInfoData[6];
        int confirm = Integer.parseInt(contactInfoData[7]);
        int accept = Integer.parseInt(contactInfoData[8]);
        String expected = contactInfoData[9];

        //Dien thong tin contact info vao form contact
        contactInfoPage.setContactFullnameInput(contactFullname);
        Thread.sleep(1000);

        contactInfoPage.setPhoneInput(phoneContact);
        Thread.sleep(1000);

        contactInfoPage.setPrimaryEmailInput(primaryEmail);
        Thread.sleep(1000);

        contactInfoPage.clickConfirm(confirm);
        Thread.sleep(1000);

        contactInfoPage.clickAccept(accept);
        Thread.sleep(1000);

        ContinueContactInfo clickContinue = contactInfoPage.clickContinue(1, 1);
        List<String> actualErrors = contactInfoPage.getAllErrorMessages();

        String actualMessage;
        boolean isTestPassed;

        if (clickContinue == ContinueContactInfo.PAYMENT_PAGE) {
            actualMessage = "Form thanh toán hiển thị";
            isTestPassed = expected.equalsIgnoreCase(actualMessage);
        } else {
            actualMessage = String.join(";", actualErrors);
            isTestPassed = actualMessage.equalsIgnoreCase(expected);
        }

        String status = isTestPassed ? "Pass" : "Fail";
        ExcelUtils.writeTestResults(FILE_PATH, "ContactInfo", excelRowIndexContact, actualMessage, 10, status, 11);
        driver.quit();
    }

    @Test
    void testContactInfoFormWith2PassengerButCancel() throws InterruptedException {
        // Đọc dữ liệu từ Excel
        List<String[]> testDataTripDetail = ExcelUtils.readExcel(FILE_PATH, "TripDetail");
        List<String[]> testDataPassenger = ExcelUtils.readExcel(FILE_PATH, "PassengerInfo");
        List<String[]> testDataContact = ExcelUtils.readExcel(FILE_PATH, "ContactInfo");

        // Khởi tạo driver
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();
        driver.get("https://india-immi.org/vi/apply");
        TripDetailPage tripDetailPage = new TripDetailPage(driver);
        PassengerInfoPage passengerInfoPage = new PassengerInfoPage(driver);
        ContactInfoPage contactInfoPage = new ContactInfoPage(driver);

        // Lấy thông tin trip detail
        String[] tripDetailPass = testDataTripDetail.get(1);
        String numbers = tripDetailPass[4];
        String purpose = tripDetailPass[5];
        String entryDate = tripDetailPass[6];
        String exitDate = tripDetailPass[7];
        String arrivalKeyword = tripDetailPass[8];
        String arrivalOption = tripDetailPass[9];
        String processingTime = tripDetailPass[10];

        // Step 1: Điền thông tin Trip Detail
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

        int passengerCount = Integer.parseInt(numbers);

        // Lấy thông tin passenger
        int passengerStart = 0;
        int passengerEnd = 1;

        for (int i = passengerStart; i<=passengerEnd; i++){
            String[] passengerInfoPass = testDataPassenger.get(i);
            String passengerFullname = passengerInfoPass[4];
            String gender = passengerInfoPass[5];
            String dob = passengerInfoPass[6];
            String nationalityKey = passengerInfoPass[7];
            String nationalityOption = passengerInfoPass[8];
            String nationKey = passengerInfoPass[9];
            String nationOption = passengerInfoPass[10];
            String passport = passengerInfoPass[11];
            String expired = passengerInfoPass[12];
            String passportImage = passengerInfoPass[13];
            String portraitImage = passengerInfoPass[14];

            // Điền vào form passenger
            passengerInfoPage.sendKeyFullnameInput(passengerFullname);
            Thread.sleep(1000);
            passengerInfoPage.selectGender(gender);
            Thread.sleep(1000);
            passengerInfoPage.selectDob(dob);
            Thread.sleep(1000);
            passengerInfoPage.searchAndSelectNationality(nationalityKey, nationalityOption);
            Thread.sleep(1000);
            passengerInfoPage.searchAndSelectNation(nationKey, nationOption);
            Thread.sleep(1000);
            passengerInfoPage.sendKeyPassport(passport);
            Thread.sleep(1000);
            passengerInfoPage.selectExpired(expired);
            Thread.sleep(1000);
            passengerInfoPage.uploadPassport(passportImage);
            Thread.sleep(1000);
            passengerInfoPage.uploadPortrait(portraitImage);
            Thread.sleep(1000);

            if(i < passengerEnd){
                passengerInfoPage.clickSave(i + 1, passengerCount);
            } else {
                passengerInfoPage.clickCancel();
            }
            
            Thread.sleep(1000);
        }

        //Lay thong tin contact info
        int targetRowIndexContact = 8;
        int excelRowIndexContact = targetRowIndexContact + 2;

        String[] contactInfoData = testDataContact.get(targetRowIndexContact);
        String contactFullname = contactInfoData[4];
        String phoneContact = contactInfoData[5];
        String primaryEmail = contactInfoData[6];
        int confirm = Integer.parseInt(contactInfoData[7]);
        int accept = Integer.parseInt(contactInfoData[8]);
        String expected = contactInfoData[9];

        //Dien thong tin contact info vao form contact
        contactInfoPage.setContactFullnameInput(contactFullname);
        Thread.sleep(1000);

        contactInfoPage.setPhoneInput(phoneContact);
        Thread.sleep(1000);

        contactInfoPage.setPrimaryEmailInput(primaryEmail);
        Thread.sleep(1000);

        contactInfoPage.clickConfirm(confirm);
        Thread.sleep(1000);

        contactInfoPage.clickAccept(accept);
        Thread.sleep(1000);

        ContinueContactInfo clickContinue = contactInfoPage.clickContinue(1, passengerCount);
        List<String> actualErrors = contactInfoPage.getAllErrorMessages();

        String actualMessage = "";
        boolean isTestPassed = false;

        if (clickContinue == ContinueContactInfo.PAYMENT_PAGE) {
            actualMessage = "Form thanh toán hiển thị";
            isTestPassed = expected.equalsIgnoreCase(actualMessage);
        } else if(clickContinue == ContinueContactInfo.NEXT_PASSENGER) {
            actualMessage = "Form thông tin hành khách hiển thị";
            isTestPassed = expected.equalsIgnoreCase(actualMessage);
        } else if (clickContinue == ContinueContactInfo.FAILED){
            actualMessage = String.join(";", actualErrors);
            isTestPassed = actualMessage.equalsIgnoreCase(expected);
        }

        String status = isTestPassed ? "Pass" : "Fail";
        ExcelUtils.writeTestResults(FILE_PATH, "ContactInfo", excelRowIndexContact, actualMessage, 10, status, 11);
        driver.quit();
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
