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
 * Selenium test to see if the library page is working as expected.
 * 
 * @author Alessandro Ciotola
 */

public class LibrarySeleniumIT 
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
     * Method to test if the book is downloaded.
     * 
     * @throws Exception 
     */    
    @Test
    public void testClientUpdate() throws Exception
    {
        driver.get("http://localhost:8080/G3BookStore/client/login.xhtml");
        
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.titleIs("Log In"));        

        WebElement inputElement = driver.findElement(By.id("loginForm:username"));
        inputElement.clear();
        inputElement.sendKeys("alessandromciotola@gmail.com");

        inputElement = driver.findElement(By.id("loginForm:password"));
        inputElement.clear();
        inputElement.sendKeys("pw");
        
        driver.findElement(By.id("loginForm:login")).click();
        
        driver.get("http://localhost:8080/G3BookStore/client/library.xhtml");            
    }
}
