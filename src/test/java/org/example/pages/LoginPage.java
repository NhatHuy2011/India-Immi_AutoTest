package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPage {
    private final WebDriver driver;

    private final By emailField = By.name("email");
    private final By emailError = By.xpath("//input[@name='email']/following::p[contains(@class,'text-destructive')][1]");

    private final By passwordField = By.name("password");
    private final By passwordError = By.xpath("//input[@name='password']/following::p[contains(@class,'text-destructive')][1]");

    private final By loginButton = By.cssSelector("button[type='submit']");
    private final By popupMessage = By.cssSelector("div[data-content] div[data-title]");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

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
