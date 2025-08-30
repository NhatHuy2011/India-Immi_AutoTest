package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.io.File;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // Setup WebDriver (e.g., ChromeDriver)
        WebDriver driver = new ChromeDriver();
        driver.get("https://the-internet.herokuapp.com/upload"); // Example URL

        // Locate the file input element
        WebElement fileInput = driver.findElement(By.id("file-upload"));

        // Specify the file to upload
        File uploadFile = new File("C:/Users/Admin/Downloads/png_5mb.png"); // Replace with your file path

        // Send the file path to the input element
        fileInput.sendKeys(uploadFile.getAbsolutePath());

        // Click the upload button (if applicable)
        driver.findElement(By.id("file-submit")).click();

        // Add assertions or further actions as needed
        driver.quit();
    }
}
