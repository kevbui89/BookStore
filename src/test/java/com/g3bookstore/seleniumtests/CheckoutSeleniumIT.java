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
 * Selenium test to see if the checkout page is working correctly.
 *
 * @author Alessandro Ciotola
 */

public class CheckoutSeleniumIT 
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
     * Method to test if the purchase is successful.
     * 
     * @throws Exception 
     */
    @Test
    public void testConfirmPurchase() throws Exception
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
        
        driver.get("http://localhost:8080/G3BookStore/client/book.xhtml?bookid=85");       
    
        driver.findElement(By.xpath("//*[@id=\"track-to-cart:addtocart\"]/span[2]")).click();  
        
        driver.get("http://localhost:8080/G3BookStore/client/shopping_cart.xhtml");
        
        driver.findElement(By.xpath("//*[@id=\"content\"]/div/div/div[7]/div/a")).click();
        
        inputElement = driver.findElement(By.id("checkoutForm:cardHolder"));
        inputElement.clear();
        inputElement.sendKeys("Alessandro Ciotola");

        driver.findElement(By.xpath("//*[@id=\"checkoutForm:j_idt75:0\"]")).click();
 
        inputElement = driver.findElement(By.id("checkoutForm:cardNumber"));
        inputElement.clear();
        inputElement.sendKeys("4111111111111111");
        
        Select dropdown = new Select(driver.findElement(By.id("checkoutForm:expiryMonth")));
        dropdown.selectByVisibleText("01");
        
        Select dropdown2 = new Select(driver.findElement(By.id("checkoutForm:expiryYear")));
        dropdown2.selectByVisibleText("2018");
        
        inputElement = driver.findElement(By.id("checkoutForm:cardSecurity"));
        inputElement.clear();
        inputElement.sendKeys("981");        
        
        driver.findElement(By.xpath("//*[@id=\"checkoutForm:log-link\"]")).click();
        
        wait.until(ExpectedConditions.titleIs("Thank You!"));         
    }
    
    /**
     * Method to test if the credit card is invalid.
     * 
     * @throws Exception 
     */
    @Test
    public void testConfirmPurchaseInvalidCard() throws Exception
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
        
        driver.get("http://localhost:8080/G3BookStore/client/book.xhtml?bookid=87");
    
        driver.findElement(By.xpath("//*[@id=\"track-to-cart:addtocart\"]/span[2]")).click(); 
        
        driver.get("http://localhost:8080/G3BookStore/client/shopping_cart.xhtml");
        
        driver.findElement(By.xpath("//*[@id=\"content\"]/div/div/div[7]/div/a")).click();
        
        inputElement = driver.findElement(By.id("checkoutForm:cardHolder"));
        inputElement.clear();
        inputElement.sendKeys("Alessandro Ciotola");
        
        Thread.sleep(2000);

        driver.findElement(By.xpath("//*[@id=\"checkoutForm:j_idt75:0\"]")).click();
 
        inputElement = driver.findElement(By.id("checkoutForm:cardNumber"));
        inputElement.clear();
        inputElement.sendKeys("41111111111");
        
        Select dropdown = new Select(driver.findElement(By.id("checkoutForm:expiryMonth")));
        dropdown.selectByVisibleText("01");
        
        Select dropdown2 = new Select(driver.findElement(By.id("checkoutForm:expiryYear")));
        dropdown2.selectByVisibleText("2018");
        
        inputElement = driver.findElement(By.id("checkoutForm:cardSecurity"));
        inputElement.clear();
        inputElement.sendKeys("981");        
        
        driver.findElement(By.xpath("//*[@id=\"checkoutForm:log-link\"]")).click();
        
        wait.until(ExpectedConditions.titleIs("Check Out"));       
    }
}
