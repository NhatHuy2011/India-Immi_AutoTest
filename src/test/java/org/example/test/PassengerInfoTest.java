package org.example.test;

import org.example.enums.SaveResultPassengerInfo;
import org.example.pages.PassengerInfoPage;
import org.example.pages.TripDetailPage;
import org.example.utils.ExcelUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    void testTestCasePassengerInfoFormWith2Passengers() throws InterruptedException {
        // Đọc dữ liệu từ Excel
        List<String[]> testDataTripDetail = ExcelUtils.readExcel(FILE_PATH, "TripDetail");
        List<String[]> testDataPassenger = ExcelUtils.readExcel(FILE_PATH, "PassengerInfo");

        // Giới hạn theo rowIndex (vd: từ 0 đến 19)
        int start = 0;
        int end = 19;

        // Lấy thông tin trip detail
        String[] tripDetailPass = testDataTripDetail.get(1);
        String numbers = tripDetailPass[4];
        String purpose = tripDetailPass[5];
        String entryDate = tripDetailPass[6];
        String exitDate = tripDetailPass[7];
        String arrivalKeyword = tripDetailPass[8];
        String arrivalOption = tripDetailPass[9];
        String processingTime = tripDetailPass[10];

        int passengerCount = Integer.parseInt(numbers);

        // Duyệt từng dòng trong khoảng
        for (int rowIndex = start; rowIndex <= end; rowIndex++) {
            String[] currentRow = testDataPassenger.get(rowIndex);
            String testCaseId = currentRow[0];

            // Lấy tất cả passenger thuộc TestCaseId này
            List<String[]> passengers = testDataPassenger.stream()
                    .filter(row -> row[0].equals(testCaseId))
                    .toList();

            // Nếu testcase này đã được xử lý ở vòng lặp trước thì bỏ qua (tránh chạy trùng TC01 2 lần)
            if (rowIndex > start) {
                String prevTestCaseId = testDataPassenger.get(rowIndex - 1)[0];
                if (prevTestCaseId.equals(testCaseId)) {
                    continue;
                }
            }

            System.out.println("Running test for TestCaseId: " + testCaseId);

            if (passengers.size() != passengerCount) {
                System.err.println("Số lượng hành khách trong dữ liệu không khớp với số lượng đã chọn: "
                        + passengerCount + " cho TestCaseId: " + testCaseId);
                continue;
            }

            // Khởi tạo driver
            driver = new ChromeDriver();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            driver.manage().window().maximize();
            driver.get("https://india-immi.org/vi/apply");
            TripDetailPage tripDetailPage = new TripDetailPage(driver);
            PassengerInfoPage passengerInfoPage = new PassengerInfoPage(driver);

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

            // Step 2: Điền thông tin hành khách
            String actualMessage = "";
            boolean isTestPassed = true;
            int excelRowIndex = testDataPassenger.indexOf(passengers.get(0)) + 2;

            for (int i = 0; i < passengerCount; i++) {
                String[] row = passengers.get(i);

                String fullname = row[4];
                String gender = row[5];
                String dob = row[6];
                String nationalityKey = row[7];
                String nationalityOption = row[8];
                String nationKey = row[9];
                String nationOption = row[10];
                String passport = row[11];
                String expired = row[12];
                String passportImage = row[13];
                String portraitImage = row[14];
                String expected = row[15];

                passengerInfoPage.sendKeyFullnameInput(fullname);
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

                SaveResultPassengerInfo saveResult = passengerInfoPage.clickSave(i + 1, passengerCount);
                List<String> actualErrors = passengerInfoPage.getAllErrorMessages();

                if (saveResult == SaveResultPassengerInfo.FAILED || !actualErrors.isEmpty()) {
                    actualMessage = String.join(";", actualErrors);
                    isTestPassed = actualMessage.equalsIgnoreCase(expected);
                } else if (saveResult == SaveResultPassengerInfo.NEXT_PASSENGER) {
                    actualMessage = "Form thông tin hành khách tiếp theo hiển thị";
                    isTestPassed = i < passengerCount - 1;
                } else if (saveResult == SaveResultPassengerInfo.CONTACT_INFO) {
                    actualMessage = "Form thông tin liên hệ hiện lên";
                    isTestPassed = expected.equalsIgnoreCase(actualMessage);
                }

                String status = isTestPassed ? "Pass" : "Fail";
                ExcelUtils.writeTestResults(FILE_PATH, "PassengerInfo",
                        excelRowIndex + i, actualMessage, 16, status, 17);
            }

            driver.quit();
        }
    }

    @Test
    @SuppressWarnings("BusyWait")
    void testTestCaseValidatePassengerInfoForm() throws InterruptedException {
        // Đọc dữ liệu từ Excel
        List<String[]> testDataTripDetail = ExcelUtils.readExcel(FILE_PATH, "TripDetail");
        List<String[]> testDataPassenger = ExcelUtils.readExcel(FILE_PATH, "PassengerInfo");

        int start = 20;
        int end = 30;

        for (int rowIndex = start; rowIndex <= end; rowIndex++){
            // Lấy thông tin trip detail
            String[] tripDetailPass = testDataTripDetail.get(0);
            String numbers = tripDetailPass[4];
            String purpose = tripDetailPass[5];
            String entryDate = tripDetailPass[6];
            String exitDate = tripDetailPass[7];
            String arrivalKeyword = tripDetailPass[8];
            String arrivalOption = tripDetailPass[9];
            String processingTime = tripDetailPass[10];

            // Khởi tạo driver
            driver = new ChromeDriver();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            driver.manage().window().maximize();
            driver.get("https://india-immi.org/vi/apply");
            TripDetailPage tripDetailPage = new TripDetailPage(driver);
            PassengerInfoPage passengerInfoPage = new PassengerInfoPage(driver);

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
            String[] row = testDataPassenger.get(rowIndex);
            String fullname = row[4];
            String gender = row[5];
            String dob = row[6];
            String nationalityKey = row[7];
            String nationalityOption = row[8];
            String nationKey = row[9];
            String nationOption = row[10];
            String passport = row[11];
            String expired = row[12];
            String passportImage = row[13];
            String portraitImage = row[14];
            String expected = row[15];

            // Điền vào form passenger
            passengerInfoPage.sendKeyFullnameInput(fullname);
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

            // Click Save và lấy kết quả
            SaveResultPassengerInfo saveResult = passengerInfoPage.clickSave(1, 1);
            List<String> actualErrors = passengerInfoPage.getAllErrorMessages();

            String actualMessage = "";
            boolean isTestPassed = true;

            // Xử lý kết quả
            if (saveResult == SaveResultPassengerInfo.FAILED || !actualErrors.isEmpty()) {
                actualMessage = String.join(";", actualErrors);
                isTestPassed = actualMessage.equalsIgnoreCase(expected);
            } else if (saveResult == SaveResultPassengerInfo.CONTACT_INFO) {
                actualMessage = "Form thông tin liên hệ hiện lên";
                isTestPassed = expected.equalsIgnoreCase(actualMessage);
            }

            // Ghi kết quả vào Excel
            String status = isTestPassed ? "Pass" : "Fail";
            ExcelUtils.writeTestResults(FILE_PATH, "PassengerInfo", rowIndex + 2, actualMessage, 16, status, 17);

            driver.quit();
        }
    }

    @Test
    void testOneTestCasePassengerInfoForm() throws InterruptedException {
        // Đọc dữ liệu từ Excel
        List<String[]> testDataTripDetail = ExcelUtils.readExcel(FILE_PATH, "TripDetail");
        List<String[]> testDataPassenger = ExcelUtils.readExcel(FILE_PATH, "PassengerInfo");

        int targetRowIndex = 29; // TestData index
        int excelRowIndex = targetRowIndex + 2; // Ghi lại kết quả

        // Lấy thông tin trip detail
        String[] tripDetailPass = testDataTripDetail.get(0);
        String numbers = tripDetailPass[4];
        String purpose = tripDetailPass[5];
        String entryDate = tripDetailPass[6];
        String exitDate = tripDetailPass[7];
        String arrivalKeyword = tripDetailPass[8];
        String arrivalOption = tripDetailPass[9];
        String processingTime = tripDetailPass[10];

        // Khởi tạo driver
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();
        driver.get("https://india-immi.org/vi/apply");
        TripDetailPage tripDetailPage = new TripDetailPage(driver);
        PassengerInfoPage passengerInfoPage = new PassengerInfoPage(driver);

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
        String[] row = testDataPassenger.get(targetRowIndex);
        String fullname = row[4];
        String gender = row[5];
        String dob = row[6];
        String nationalityKey = row[7];
        String nationalityOption = row[8];
        String nationKey = row[9];
        String nationOption = row[10];
        String passport = row[11];
        String expired = row[12];
        String passportImage = row[13];
        String portraitImage = row[14];
        String expected = row[15];

        System.out.println("Expected: " + expected);

        // Điền vào form passenger
        passengerInfoPage.sendKeyFullnameInput(fullname);
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

        // Click Save và lấy kết quả
        SaveResultPassengerInfo saveResult = passengerInfoPage.clickSave(1, 1);
        List<String> actualErrors = passengerInfoPage.getAllErrorMessages();
        System.out.println("Actual Errors: " + actualErrors);

        String actualMessage = "";
        boolean isTestPassed = true;

        // Xử lý kết quả
        if (saveResult == SaveResultPassengerInfo.FAILED || !actualErrors.isEmpty()) {
            actualMessage = String.join(";" , actualErrors);
            isTestPassed = actualMessage.equalsIgnoreCase(expected);
        } else if (saveResult == SaveResultPassengerInfo.CONTACT_INFO) {
            actualMessage = "Form thông tin liên hệ hiện lên";
            isTestPassed = expected.equalsIgnoreCase(actualMessage);
        }
        System.out.println("Actual Message: " + actualMessage);

        // Ghi kết quả vào Excel
        String status = isTestPassed ? "Pass" : "Fail";
        ExcelUtils.writeTestResults(FILE_PATH, "PassengerInfo", excelRowIndex, actualMessage, 16, status, 17);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
