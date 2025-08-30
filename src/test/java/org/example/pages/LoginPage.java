package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPage {
    private WebDriver driver;

    // Locators (dùng chung cho cả vi & en vì name và xpath giống nhau)
    private By emailField = By.name("email");
    private By passwordField = By.name("password");
    private By loginButton = By.cssSelector("button[type='submit']");

    private By popupMessage = By.cssSelector("div[data-content] div[data-title]");
    private By emailError = By.xpath("//input[@name='email']/following::p[contains(@class,'text-destructive')][1]");
    private By passwordError = By.xpath("//input[@name='password']/following::p[contains(@class,'text-destructive')][1]");

    // Constructor
    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    // Actions
    public void enterEmail(String email) {
        driver.findElement(emailField).clear();
        driver.findElement(emailField).sendKeys(email);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordField).clear();
        driver.findElement(passwordField).sendKeys(password);
    }

    public void clickLogin() {
        driver.findElement(loginButton).click();
    }

    public String getErrorMessage() {
        try {
            WebElement msg = driver.findElement(popupMessage);
            return msg.getText().trim();
        } catch (Exception e1) {
            try {
                return driver.findElement(emailError).getText().trim();
            } catch (Exception e2) {
                try {
                    return driver.findElement(passwordError).getText().trim();
                } catch (Exception e3) {
                    return "";
                }
            }
        }
    }
}
