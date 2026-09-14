package org.example.selinium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AuthTest {

    WebDriver driver = new ChromeDriver();

    @Test
    public void accessDashBoard(){
        driver.get("http://localhost:5173/");
        String title = driver.getTitle();

        System.out.println(title);
        assertNotNull(title);
    }

    @Test
    public void validRegistration() {
        driver.get("http://localhost:5173/");

        driver.findElement(By.xpath("//button[contains(text(),'Register here')]")).click();

        driver.findElement(By.xpath("//input[@type='text']") )
                .sendKeys("testuser01");

        driver.findElement( By.xpath("//input[@type='password']") )
                .sendKeys("Test@12345");


        driver.findElement(By.xpath("//button[@type='submit']")
        ).click();


        String message = driver.findElement(
                By.xpath("//*[contains(text(),'Account created')]")
        ).getText();
    }

    @Test
    public void  duplicateRegistration(){
        driver.get("http://localhost:5173/");

        driver.findElement(By.xpath("//button[contains(text(),'Register here')]")).click();

        driver.findElement(By.xpath("//input[@type='text']") )
                .sendKeys("testuser01");

        driver.findElement( By.xpath("//input[@type='password']") )
                .sendKeys("Test@12345");


        driver.findElement(By.xpath("//button[@type='submit']")
        ).click();


        String message = driver.findElement(
                By.xpath("//*[contains(text(),'Account created')]")
        ).getText();
    }

    @Test
    public void emptyRegistration(){
        driver.get("http://localhost:5173/");

        driver.findElement(By.xpath("//button[contains(text(),'Register here')]")).click();

        driver.findElement(By.xpath("//input[@type='text']") )
                .sendKeys("");

        driver.findElement( By.xpath("//input[@type='password']") )
                .sendKeys("");


        driver.findElement(By.xpath("//button[@type='submit']")
        ).click();

    }

    @Test
    public void validLogin(){
        driver.get("http://localhost:5173/");

        driver.findElement(By.xpath("//input[@type='text']"))
                .sendKeys("testuser01");

        driver.findElement(By.xpath("//input[@type='password']"))
                .sendKeys("Test@12345");

        driver.findElement(By.xpath("//button[@type='submit']")
        ).click();

    }

    @Test
    public void wrongPasswordLogin(){
        driver.get("http://localhost:5173/");

        driver.findElement(By.xpath("//input[@type='text']"))
                .sendKeys("testuser01");

        driver.findElement(By.xpath("//input[@type='password']"))
                .sendKeys("Test@1234567");

        driver.findElement(By.xpath("//button[@type='submit']")
        ).click();

    }

    @Test
    public void emptyLogin(){
        driver.get("http://localhost:5173/");

        driver.findElement(By.xpath("//input[@type='text']"))
                .sendKeys("");

        driver.findElement(By.xpath("//input[@type='password']"))
                .sendKeys("");

        driver.findElement(By.xpath("//button[@type='submit']")
        ).click();
    }

    @Test
    public void generateSummary(){
        driver.get("http://localhost:5173/");

        driver.findElement(By.xpath("//input[@type='text']"))
                .sendKeys("testuser01");

        driver.findElement(By.xpath("//input[@type='password']"))
                .sendKeys("Test@12345");

        driver.findElement(By.xpath("//button[@type='submit']")
        ).click();

        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//button[contains(text(),'GENERATE SUMMARY')]")
        ));

        driver.findElement(
                By.xpath("//button[contains(text(),'GENERATE SUMMARY')]")
        ).click();
    }

    @Test
    public void signOut() {

        driver.get("http://localhost:5173/");

        driver.findElement(By.xpath("//input[@type='text']"))
                .sendKeys("testuser01");

        driver.findElement(By.xpath("//input[@type='password']"))
                .sendKeys("Test@12345");

        driver.findElement(By.xpath("//button[@type='submit']"))
                .click();

        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//button[contains(text(),'SIGN OUT')]")
        ));

        driver.findElement(
                By.xpath("//button[contains(text(),'SIGN OUT')]")
        ).click();

    }

    public static void main(String[] args){
        AuthTest registerTest = new AuthTest();
        registerTest.accessDashBoard();
        registerTest.validRegistration();
        registerTest.validLogin();
    }
}
