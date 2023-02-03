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
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Selenium test to see if the Account Settings page is working as expected.
 * 
 * @author Alessandro Ciotola
 */
@Ignore
public class AccountSettingsSeleniumIT 
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
     * Method to test if the client is successfully updated.
     * 
     * @throws Exception 
     */    
    @Test
    public void testClientUpdate() throws Exception
    {
        driver.manage().window().maximize();
        driver.get("http://localhost:8080/G3BookStore/client/login.xhtml");
        
        WebDriverWait wait = new WebDriverWait(driver, 15);
        wait.until(ExpectedConditions.titleIs("Log In"));        

        WebElement inputElement = driver.findElement(By.id("loginForm:username"));
        inputElement.clear();
        inputElement.sendKeys("kb@gmail.com");

        inputElement = driver.findElement(By.id("loginForm:password"));
        inputElement.clear();
        inputElement.sendKeys("pw");
        
        driver.findElement(By.id("loginForm:login")).click();
        
        driver.get("http://localhost:8080/G3BookStore/client/account_info.xhtml");       
        
        Thread.sleep(1000);
        driver.findElement(By.xpath("//*[@id=\"log-link\"]")).click();        
        
        inputElement = driver.findElement(By.id("registerForm:password"));
        inputElement.clear();
        inputElement.sendKeys("password");
        
        driver.findElement(By.xpath("//*[@id=\"registerForm:updateBtn\"]")).click();          
        
        wait.until(ExpectedConditions.titleIs("Account Information")); 
        Thread.sleep(5000);
    }
}
