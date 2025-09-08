package org.example.pages;

import org.example.enums.SaveResultPassengerInfo;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PassengerInfoPage {
    private final WebDriver driver;

    private final By fullnameInput = By.name("full_name");
    private final By fullnameError = By.xpath("//div[.//input[@name='full_name']]//p[contains(@class,'text-destructive')]");

    private final By genderButton = By.id("gender");
    private final By genderSelect = By.xpath("//button[@id='gender']/following-sibling::select");
    private final By genderError = By.xpath("//div[.//button[@id='gender']]//p[contains(@class,'text-destructive')]");

    private final By dobInput = By.id("dob");
    private final By dobError = By.xpath("//div[.//input[@id='dob']]//p[contains(@class,'text-destructive')]");

    private final By nationalityInput = By.xpath("(//input[@id='nationality'])[1]");
    private final By nationalityInputError = By.xpath("//input[@id='nationality'])[1]/p[contains(@class,'text-destructive')][1]");

    private final By nationInput = By.xpath("(//input[@id='nationality'])[2]");
    private final By nationInputError = By.xpath("(//input[@id='nationality'])[2]/p[contains(@class,'text-destructive')][1]");

    private final By passportInput = By.name("passport_number");
    private final By passportInputError = By.xpath("//div[.//input[@name='passport_number']]//p[contains(@class,'text-destructive')]");

    private final By expiredInput = By.id("expired_date");
    private final By expiredInputError = By.xpath("//div[.//input[@id='expired_date']]//p[contains(@class,'text-destructive')]");

    private final By passportUploadInput = By.id("passport-upload");
    private final By passportUploadInputError = By.xpath("//div[.//input[@id='passport-upload']]//p[contains(@class,'text-destructive')]");

    private final By portraitUploadInput = By.id("portrait-upload");
    private final By portraitUploadInputError = By.xpath("//div[.//input[@id='portrait-upload']]//p[contains(@class,'text-destructive')]");

    private final By saveButton = By.cssSelector("button.inline-flex.w-32.h-14[type='submit']");

    private final By cancelButton = By.cssSelector("button.w-32.h-14[type='button']");

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
            if(keyword != null && !keyword.trim().isEmpty()){
                WebElement input = driver.findElement(nationalityInput);
                input.click();
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

    public void searchAndSelectNation(String keyword, String optionToPick) {
        try {
            if(keyword != null && !keyword.trim().isEmpty()){
                WebElement input = driver.findElement(nationInput);
                input.click();
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

    public void sendKeyPassport(String value){
        WebElement passport = driver.findElement(passportInput);
        passport.clear();
        passport.sendKeys(value);
    }

    public void selectExpired(String dateValue){
        pickDate(expiredInput, dateValue);
    }

    public void uploadPassport(String path){
        if(path != null && !path.isEmpty()){
            WebElement passportUpload = driver.findElement(passportUploadInput);
            passportUpload.sendKeys(path);
        } else {
            System.out.println("Không gởi ảnh");
        }
    }

    public void uploadPortrait(String path){
        if(path != null && !path.isEmpty()){
            WebElement portraitUpload = driver.findElement(portraitUploadInput);
            portraitUpload.sendKeys(path);
        } else {
            System.out.println("Không gởi ảnh");
        }
    }

    public SaveResultPassengerInfo clickSave(int currentPassengerIndex, int totalPassengers) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

        try {
            WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(saveButton));

            // Scroll và click
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", btn);
            btn.click();

            if (currentPassengerIndex < totalPassengers) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("div[role='dialog'][data-state='open']")
                ));
                return SaveResultPassengerInfo.NEXT_PASSENGER;
            } else {
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("div.grid.grid-cols-1.lg\\:grid-cols-2.gap-4")
                ));
                return SaveResultPassengerInfo.CONTACT_INFO;
            }
        } catch (Exception e) {
            return SaveResultPassengerInfo.FAILED;
        }
    }

    public boolean clickCancel() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

        try {
            WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(cancelButton));

            // Scroll và click
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", btn);
            btn.click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div.grid.grid-cols-1.lg\\:grid-cols-2.gap-4")
            ));

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void selectHiddenDropdown(By buttonLocator, By selectLocator, String valueToSelect) {
        try {
            WebElement button = driver.findElement(buttonLocator);
            WebElement selectElement = driver.findElement(selectLocator);

            Select select = new Select(selectElement);
            select.selectByValue(valueToSelect);

            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript(
                    "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
                            "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                    selectElement
            );

            String selectedText = select.getFirstSelectedOption().getText();

            js.executeScript("arguments[0].innerText=arguments[1];", button, selectedText);

        } catch (Exception e) {

        }
    }

    private void pickDate(By inputLocator, String dateValue) {
        if (dateValue == null || dateValue.trim().isEmpty()){
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
}
