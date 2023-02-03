package com.g3bookstore.seleniumtests;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Selenium test to see if the registration page is working as expected.
 * 
 * @author Alessandro Ciotola
 */
@Ignore
public class RegistrationSeleniumIT 
{ 
    private WebDriver driver;

    @Before
    public void setUp() 
    {
        ChromeDriverManager.getInstance().setup();
        driver = new ChromeDriver();
    }
    
    @After
    public void shutDown() {
        driver.close();
    }
    
    /**
     * Method to test if the client is successfully registered.
     * 
     * @throws Exception 
     */
    @Test
    public void testClientRegistration() throws Exception
    {
        driver.get("http://localhost:8080/G3BookStore/client/registration.xhtml");

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.titleIs("Registration"));        

        WebElement inputElement = driver.findElement(By.id("registerForm:email"));
        inputElement.clear();
        inputElement.sendKeys("newSeleniumTest@gmail.com");

        inputElement = driver.findElement(By.id("registerForm:username"));
        inputElement.clear();
        inputElement.sendKeys("NewSeleniumTest");
        
        inputElement = driver.findElement(By.id("registerForm:password"));
        inputElement.clear();
        inputElement.sendKeys("password");
        
        inputElement = driver.findElement(By.id("registerForm:title"));
        inputElement.clear();
        inputElement.sendKeys("Mr.");
        
        inputElement = driver.findElement(By.id("registerForm:fname"));
        inputElement.clear();
        inputElement.sendKeys("Selenium");
        
        inputElement = driver.findElement(By.id("registerForm:lname"));
        inputElement.clear();
        inputElement.sendKeys("Test");
        
        inputElement = driver.findElement(By.id("registerForm:company"));
        inputElement.clear();
        inputElement.sendKeys("None");
        
        inputElement = driver.findElement(By.id("registerForm:addr1"));
        inputElement.clear();
        inputElement.sendKeys("12345 Selenium Test");
        
        inputElement = driver.findElement(By.id("registerForm:addr2"));
        inputElement.clear();
        inputElement.sendKeys("12345 Selenium Test2");
        
        inputElement = driver.findElement(By.id("registerForm:city"));
        inputElement.clear();
        inputElement.sendKeys("Montreal");
        
        Select dropdown = new Select(driver.findElement(By.id("registerForm:province")));
        dropdown.selectByVisibleText("Quebec");
        
        Select dropdown2 = new Select(driver.findElement(By.id("registerForm:country")));
        dropdown2.selectByVisibleText("Canada");
        
        inputElement = driver.findElement(By.id("registerForm:postal"));
        inputElement.clear();
        inputElement.sendKeys("J6W 0B6");
        
        inputElement = driver.findElement(By.id("registerForm:hometel"));
        inputElement.clear();
        inputElement.sendKeys("5142354125");
        
        inputElement = driver.findElement(By.id("registerForm:celltel"));
        inputElement.clear();
        inputElement.sendKeys("4502547456");

        driver.findElement(By.id("registerForm:submitRegister")).click();
        
        wait.until(ExpectedConditions.titleIs("Log In"));       
    }
    
    /**
     * Method to test if the client enters invalid input.
     * 
     * @throws Exception 
     */
    @Test
    public void testClientRegistrationInvalidInput() throws Exception
    {
        driver.get("http://localhost:8080/G3BookStore/client/registration.xhtml");

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.titleIs("Registration"));        

        WebElement inputElement = driver.findElement(By.id("registerForm:email"));
        inputElement.clear();
        inputElement.sendKeys("newSeleniumTestgmailcom");
    
        driver.findElement(By.id("registerForm:submitRegister")).click();
        
        wait.until(ExpectedConditions.titleIs("Registration"));       
    }
        
}
