package org.example.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ContactInfoPage {
    private final WebDriver driver;

    private final By contactTitleButton = By.id("contact-title");
    private final By contactTitleSelect = By.xpath("//button[@id='contact-title']/following-sibling::select");

    private final By contactFullnameInput = By.name("contact.full_name");

    private final By phoneInputButton = By.cssSelector(".iti__selected-country");
    private final By phoneInput = By.cssSelector(".iti__tel-input");

    private final By primaryEmailInput = By.name("contact.primary_email");

    private final By secondaryEmailInput = By.name("contact.secondary_email");

    private final By confirmCheckbox = By.id("confirm");

    private final By acceptCheckbox = By.id("accept");

    private final By continueButton = By.cssSelector("button.inline-flex.w-52.h-14[type='submit']");

    public ContactInfoPage(WebDriver driver) {
        this.driver = driver;
    }

    public void setContactTitleButton(String value){
        selectHiddenDropdown(contactTitleButton, contactTitleSelect, value);
    }

    public void setContactFullnameInput(String value){
        WebElement fullnameInput = driver.findElement(contactFullnameInput);
        fullnameInput.sendKeys(Keys.CONTROL + "a", Keys.DELETE);
        fullnameInput.sendKeys(value);
    }

    public void searchAndSelectPhoneInput(String keyword, String optionToPick) {
        try {
            // Mở dropdown
            WebElement button = driver.findElement(phoneInputButton);
            button.click();

            // Nhập keyword vào ô search
            WebElement search = driver.findElement(By.cssSelector(".iti__search-input"));
            search.clear();
            search.sendKeys(keyword);

            // Chờ option hiển thị
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement option = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//li[contains(@class,'iti__country')][.//span[contains(text(),'" + optionToPick + "')]]")
            ));

            // Scroll đến option và click bằng JS
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", option);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", option);

            System.out.println("✅ Đã chọn mã vùng điện thoại: " + optionToPick);
        } catch (Exception e) {
            System.out.println("❌ Lỗi khi search mã vùng điện thoại: " + e.getMessage());
        }
    }

    public void setPhoneInput(String value){
        WebElement phone = driver.findElement(phoneInput);
        phone.clear();
        phone.sendKeys(value);
    }

    public void setPrimaryEmailInput(String value){
        WebElement primaryEmail = driver.findElement(primaryEmailInput);
        primaryEmail.clear();
        primaryEmail.sendKeys(value);
    }

    public void setSecondaryEmailInput(String value){
        WebElement secondaryEmail = driver.findElement(secondaryEmailInput);
        secondaryEmail.clear();
        secondaryEmail.sendKeys(value);
    }

    public void clickConfirm(){
        WebElement confirm = driver.findElement(confirmCheckbox);
        confirm.click();
    }

    public void clickAccept(){
        WebElement accept = driver.findElement(acceptCheckbox);
        accept.click();
    }

    public void clickContinue() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        try {
            // Kiểm tra sự tồn tại của nút
            WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(continueButton));
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
                    By.cssSelector("div.flex.flex-col.gap-6.flex-1")
            ));
            System.out.println("Nhấn nút thành công.");
        } catch (Exception e) {
            System.out.println("Lỗi khi nhấn nút: " + e.getMessage());
            // Thử click bằng JS
            WebElement btn = driver.findElement(continueButton);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div.flex.flex-col.gap-6.flex-1")
            ));
        }
    }

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

            System.out.println("Đã chọn [" + buttonLocator + "]: " + selectedText);
        } catch (Exception e) {
            System.out.println("Lỗi khi chọn dropdown [" + buttonLocator + "]: " + e.getMessage());
        }
    }
}
