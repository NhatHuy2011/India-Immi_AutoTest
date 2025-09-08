package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class PaymentPage {
    private final WebDriver driver;

    @FindBy(xpath = "//button[@role='radio']")
    private List<WebElement> paymentMethods;

    private final By paymentButton = By.xpath("//button[@type='submit' and contains(.,'Tiến hành thanh toán')]");

    public PaymentPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void  selectPayment(String option){
        for (WebElement btn : paymentMethods){
            String value = btn.getAttribute("value");
            if(value.equals(option)){
                btn.click();
                break;
            }
        }
    }

    public void clickPaymentButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

        WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(paymentButton));

        // Kiểm tra disabled
        if (btn.getAttribute("disabled") != null) {
            throw new RuntimeException("Nút thanh toán đang disabled. Hãy chọn phương thức thanh toán trước.");
        }

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", btn);

        try {
            wait.until(ExpectedConditions.elementToBeClickable(paymentButton)).click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
    }

}
