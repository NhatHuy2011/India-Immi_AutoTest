package org.example.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;

public class PassengerInfoPage {
    private final WebDriver driver;

    private final By fullnameInput = By.name("full_name");

    private final By genderButton = By.id("gender");
    private final By genderSelect = By.xpath("//button[@id='gender']/following-sibling::select");

    private final By dobInput = By.id("dob");

    private final By nationalityInput = By.xpath("(//input[@id='nationality'])[1]");

    private final By nationInput = By.xpath("(//input[@id='nationality'])[2]");

    private final By passportInput = By.name("passport_number");

    private final By expiredInput = By.id("expired_date");

    public PassengerInfoPage(WebDriver driver) {
        this.driver = driver;
    }

    public void sendKeyFullnameInput(String value){
        WebElement fullname = driver.findElement(fullnameInput);
        fullname.clear();
        fullname.sendKeys(value);
    }

    public void selectGender(String value){
        selectHiddenDropdown(genderButton, genderSelect, value);
    }

    public void selectDob(String dateValue){
        pickDate(dobInput, dateValue);
    }

    public void searchAndSelectNationality(String keyword, String optionToPick) {
        try {
            WebElement input = driver.findElement(nationalityInput);
            input.click();

            // Gõ từ khoá để filter
            input.sendKeys(keyword);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@role='option' and @data-value=\"" + optionToPick + "\"]")
            ));
            option.click();

            System.out.println("Đã chọn cảng nhập cảnh (search): " + optionToPick);
        } catch (Exception e) {
            System.out.println("Lỗi khi search arrival_port: " + e.getMessage());
        }
    }

    public void searchAndSelectNation(String keyword, String optionToPick) {
        try {
            WebElement input = driver.findElement(nationInput);
            input.click();

            // Gõ từ khoá để filter
            input.sendKeys(keyword);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@role='option' and @data-value=\"" + optionToPick + "\"]")
            ));
            option.click();

            System.out.println("Đã chọn cảng nhập cảnh (search): " + optionToPick);
        } catch (Exception e) {
            System.out.println("Lỗi khi search arrival_port: " + e.getMessage());
        }
    }

    public void sendKeyPassport(String value){
        WebElement passport = driver.findElement(passportInput);
        passport.clear();
        passport.sendKeys(value);
    }

    public void selectExpired(String dateValue){
        pickDate(expiredInput, dateValue);
    }

    private void selectHiddenDropdown(By buttonLocator, By selectLocator, String valueToSelect) {
        try {
            WebElement button = driver.findElement(buttonLocator);
            WebElement selectElement = driver.findElement(selectLocator);

            Select select = new Select(selectElement);
            select.selectByValue(valueToSelect);

            String selectedText = select.getFirstSelectedOption().getText();

            // Cập nhật text trên button
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].innerText=arguments[1];", button, selectedText);

            System.out.println("Đã chọn [" + buttonLocator + "]: " + selectedText);
        } catch (Exception e) {
            System.out.println("Lỗi khi chọn dropdown [" + buttonLocator + "]: " + e.getMessage());
        }
    }

    private void pickDate(By inputLocator, String dateValue) {
        driver.findElement(inputLocator).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        LocalDate date = LocalDate.parse(dateValue);

        // 1. Chọn năm
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[data-slot='calendar']")));
        WebElement yearDropdown = driver.findElement(By.cssSelector("select.rdp-years_dropdown"));
        new Select(yearDropdown).selectByValue(String.valueOf(date.getYear()));

        // 2. Chọn tháng
        WebElement monthDropdown = driver.findElement(By.cssSelector("select.rdp-months_dropdown"));
        new Select(monthDropdown).selectByValue(String.valueOf(date.getMonthValue() - 1));

        // 3. Click ngày (tìm lại calendarRoot sau khi DOM thay đổi)
        String dataDay = date.getMonthValue() + "/" + date.getDayOfMonth() + "/" + date.getYear();

        wait.until(d -> {
            WebElement calendarRoot = driver.findElement(By.cssSelector("div[data-slot='calendar']"));
            try {
                WebElement dayButton = calendarRoot.findElement(By.cssSelector("button.rdp-day[data-day='" + dataDay + "']"));
                dayButton.click();
                return true; // click thành công
            } catch (StaleElementReferenceException | NoSuchElementException e) {
                return false; // retry
            }
        });
    }
}
