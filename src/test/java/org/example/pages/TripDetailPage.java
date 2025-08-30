package org.example.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;

public class TripDetailPage {
    private final WebDriver driver;

    private final By applicantButton = By.id("applicants");
    private final By applicantSelect = By.xpath("//button[@id='applicants']/following-sibling::select");

    private final By purposeButton = By.id("purpose");
    private final By purposeSelect = By.xpath("//button[@id='purpose']/following-sibling::select");

    private final By entryDateInput = By.id("entry_date");

    private final By exitDateInput = By.id("exit_date");

    private final By arrivalPortInput = By.id("arrival_port");

    private final By radioGroup = By.id("processing_time");
    private final String optionByValue = ".//button[@role='radio' and @value='%s']";

    private final By continueBtn = By.cssSelector("button.inline-flex.w-52.h-12[type='submit']");

    public TripDetailPage(WebDriver driver){
        this.driver = driver;
    }

    public void selectApplicants(String value) {
        selectHiddenDropdown(applicantButton, applicantSelect, value);
    }

    public void selectPurpose(String value) {
        selectHiddenDropdown(purposeButton, purposeSelect, value);
    }

    public void selectEntryDate(String dateValue) {
        pickDate(entryDateInput, dateValue);
    }

    public void selectExitDate(String dateValue) {
        pickDate(exitDateInput, dateValue);
    }

    public void searchAndSelectArrivalPort(String keyword, String optionToPick) {
        try {
            WebElement input = driver.findElement(arrivalPortInput);
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

    public void selectProcessingTime(String value) {
        try {
            WebElement group = driver.findElement(radioGroup);

            By optionLocator = By.xpath(String.format(optionByValue, value));

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement option = wait.until(ExpectedConditions.elementToBeClickable(group.findElement(optionLocator)));

            option.click();

            System.out.println("Đã chọn thời gian xử lý: " + value);
        } catch (Exception e) {
            System.out.println("Lỗi khi chọn processing_time: " + e.getMessage());
        }
    }

    public void clickContinue() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        try {
            // Kiểm tra sự tồn tại của nút
            WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(continueBtn));
            System.out.println("Nút tìm thấy: " + btn.isDisplayed());

            // Kiểm tra trạng thái enabled
            boolean isEnabled = btn.isEnabled();
            String disabledAttr = btn.getAttribute("disabled");
            System.out.println("Nút enabled: " + isEnabled + ", Thuộc tính disabled: " + disabledAttr);

            if (!isEnabled || disabledAttr != null) {
                throw new RuntimeException("Nút 'Tiếp tục' bị vô hiệu hóa. Kiểm tra form.");
            }

            // Scroll và click
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", btn);
            btn.click(); // Thử click bằng Selenium trước
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div[role='dialog'][data-state='open']")
            ));
            System.out.println("Nhấn nút thành công.");
        } catch (Exception e) {
            System.out.println("Lỗi khi nhấn nút: " + e.getMessage());
            // Thử click bằng JS
            WebElement btn = driver.findElement(continueBtn);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div[role='dialog'][data-state='open']")
            ));
        }
    }

    //Show content in button dropdown
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

    //Pick year-month-date
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