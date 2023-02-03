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
 * Selenium test to see if the cart is functioning properly.
 *
 * @author Alessandro Ciotola
 */
@Ignore
public class ShoppingCartSeleniumIT
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
     * Method to test if the book is added to cart and the next page is shown.
     * 
     * @throws Exception 
     */
    @Test
    public void testAddBookToCart() throws Exception
    {
        driver.get("http://localhost:8080/G3BookStore/client/book.xhtml?bookid=84");

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.titleIs("All the Birds in the Sky"));        
    
        driver.findElement(By.id("track-to-cart:addtocart")).click();

        wait.until(ExpectedConditions.titleIs("Shopping Cart"));   
    }
    
    /**
     * Method to test if the book is removed from the cart.
     * 
     * @throws Exception 
     */
    @Test
    public void testRemoveFromCart() throws Exception
    {
        driver.get("http://localhost:8080/G3BookStore/client/book.xhtml?bookid=83");

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.titleIs("Do Androids Dream of Electric Sheep?"));        
    
        driver.findElement(By.id("track-to-cart:addtocart")).click();
        
        driver.get("http://localhost:8080/G3BookStore/client/shopping_cart.xhtml");
        
        driver.findElement(By.xpath("//*[@id=\"repeatCartBook:0:removeForm:removeBookBtn\"]/span")).click();
        
        wait.until(ExpectedConditions.titleIs("Shopping Cart"));     
    }
        
    /**
     * Method to test if the proceed to checkout button brings you to the appropriate page.
     * 
     * @throws Exception 
     */
    @Test
    public void testProceedToCheckoutBtn() throws Exception
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
        
        driver.get("http://localhost:8080/G3BookStore/client/book.xhtml?bookid=82");
    
        driver.findElement(By.xpath("//*[@id=\"track-to-cart:addtocart\"]/span[2]")).click();     
        
        driver.get("http://localhost:8080/G3BookStore/client/shopping_cart.xhtml");
        
        driver.findElement(By.id("proceedBtn")).click();
        
        wait.until(ExpectedConditions.titleIs("Check Out"));    
    }
}
