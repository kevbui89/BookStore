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
 * Selenium test to see if the orders page is working as expected.
 * 
 * @author Alessandro Ciotola
 */
public class MyOrdersSeleniumIT 
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
     * Method to test if the email is sent.
     * 
     * @throws Exception 
     */
    @Test
    public void testSendOrderEmail() throws Exception
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
        
        driver.get("http://localhost:8080/G3BookStore/client/myorders.xhtml");       
        
        driver.findElement(By.xpath("//*[@id=\"j_idt67:0:j_idt75\"]/a/i")).click();
        
        driver.findElement(By.id("j_idt86:sendEmailBtn")).click();     
        
        wait.until(ExpectedConditions.titleIs("Email sent."));       
    }
    
    /**
     * Method to test if the print page is shown.
     * 
     * @throws Exception 
     */
    @Test
    public void testSendOrderPrint() throws Exception
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
        
        driver.get("http://localhost:8080/G3BookStore/client/myorders.xhtml");       
        
        driver.findElement(By.xpath("//*[@id=\"j_idt67:0:j_idt75\"]/a/i")).click();
        
        driver.findElement(By.xpath("//*[@id=\"j_idt86\"]/input[3]")).click();     
        
        wait.until(ExpectedConditions.titleIs("Order History"));       
    }
}
