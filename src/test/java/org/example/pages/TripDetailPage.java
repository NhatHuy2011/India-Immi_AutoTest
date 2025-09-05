package org.example.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TripDetailPage {
    private final WebDriver driver;

    private final By applicantButton = By.id("applicants");
    private final By applicantSelect = By.xpath("//button[@id='applicants']/following-sibling::select");
    private final By applicantError = By.xpath("//div[.//button[@id='applicants']]//p[contains(@class,'text-destructive')]");

    private final By purposeButton = By.id("purpose");
    private final By purposeSelect = By.xpath("//button[@id='purpose']/following-sibling::select");
    private final By purposeError = By.xpath("//div[.//button[@id='purpose']]//p[contains(@class,'text-destructive')]");

    private final By entryDateInput = By.id("entry_date");
    private final By entryDateError = By.xpath("//div[.//button[@id='entry_date']]//p[contains(@class,'text-destructive')]");

    private final By exitDateInput = By.id("exit_date");
    private final By exitDateError = By.xpath("//div[.//button[@id='exit_date']]//p[contains(@class,'text-destructive')]");

    private final By arrivalPortInput = By.id("arrival_port");
    private final By arrivalPortError = By.xpath("//div[.//button[@id='arrival_port']]//p[contains(@class,'text-destructive')]");

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

            if(keyword != null && !keyword.trim().isEmpty()){
                input.sendKeys(keyword);

                if(optionToPick != null && !optionToPick.trim().isEmpty()){
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
                    WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//div[@role='option' and contains(@data-value, \"" + optionToPick + "\")]")
                    ));
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", option);
                    option.click();
                }
            } else {

            }
        } catch (Exception e) {

        }
    }

    public void selectProcessingTime(String value) {
        try {
            WebElement group = driver.findElement(radioGroup);

            By optionLocator = By.xpath(String.format(optionByValue, value));

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement option = wait.until(ExpectedConditions.elementToBeClickable(group.findElement(optionLocator)));

            option.click();
        } catch (Exception e) {

        }
    }

    public boolean clickContinue() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

        try {
            WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(continueBtn));

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", btn);
            btn.click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div[role='dialog'][data-state='open']")
            ));

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessage() {
        try {
            return driver.findElement(applicantError).getText().trim();
        } catch (Exception e1) {
            try {
                return driver.findElement(purposeError).getText().trim();
            } catch (Exception e2) {
                try {
                    return driver.findElement(entryDateError).getText().trim();
                } catch (Exception e3) {
                    try {
                        return driver.findElement(exitDateError).getText().trim();
                    } catch (Exception e4) {
                        try {
                            return driver.findElement(arrivalPortError).getText().trim();
                        } catch (Exception e5) {
                            try {
                                WebElement emptyMsg = driver.findElement(By.cssSelector("div[cmdk-empty]"));
                                return emptyMsg.getText().trim();
                            } catch (Exception e6){
                                return "";
                            }
                        }
                    }
                }
            }
        }
    }

    public List<String> getAllErrorMessages() {
        List<String> errors = new ArrayList<>();

        List<WebElement> errorElements = driver.findElements(
                By.xpath("//p[contains(@class,'text-destructive')]")
        );

        for (WebElement e : errorElements) {
            String msg = e.getText().trim();
            if (!msg.isEmpty()) {
                errors.add(msg);
            }
        }
        return errors;
    }

    //Show content in button dropdown
    private void selectHiddenDropdown(By buttonLocator, By selectLocator, String valueToSelect) {
        try {
            WebElement button = driver.findElement(buttonLocator);
            WebElement selectElement = driver.findElement(selectLocator);

            Select select = new Select(selectElement);
            select.selectByValue(valueToSelect);

            // kích hoạt sự kiện change & input để framework nhận giá trị
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript(
                    "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
                            "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                    selectElement
            );

            String selectedText = select.getFirstSelectedOption().getText();

            // Cập nhật text hiển thị trên button (nếu button chỉ là UI fake)
            js.executeScript("arguments[0].innerText=arguments[1];", button, selectedText);
        } catch (Exception e) {

        }
    }

    //Pick year-month-date
    private void pickDate(By inputLocator, String dateValue) {
        if (dateValue == null || dateValue.trim().isEmpty()) {
            return;
        }

        driver.findElement(inputLocator).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        LocalDate date = LocalDate.parse(dateValue);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[data-slot='calendar']")));

        WebElement yearDropdown = driver.findElement(By.cssSelector("select.rdp-years_dropdown"));
        new Select(yearDropdown).selectByValue(String.valueOf(date.getYear()));

        WebElement monthDropdown = driver.findElement(By.cssSelector("select.rdp-months_dropdown"));
        new Select(monthDropdown).selectByValue(String.valueOf(date.getMonthValue() - 1));

        String dataDay = date.getMonthValue() + "/" + date.getDayOfMonth() + "/" + date.getYear();

        WebElement dayButton = driver.findElement(By.cssSelector("button.rdp-day[data-day='" + dataDay + "']"));
        if (dayButton.getAttribute("disabled") != null) {
            throw new IllegalArgumentException("Ngày không hợp lệ");
        }

        dayButton.click();
    }
}