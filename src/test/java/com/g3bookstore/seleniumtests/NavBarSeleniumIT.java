package com.g3bookstore.seleniumtests;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Selenium Test to see if the nav bar is functioning correctly.
 * 
 * @author Alessandro Ciotola
 */
@Ignore
public class NavBarSeleniumIT {

    private WebDriver driver;

    @Before
    public void setUp() {
        ChromeDriverManager.getInstance().setup();
        driver = new ChromeDriver();
    }
    
    @After
    public void shutDown() {
        driver.close();
    }

    /**
     * Method to test if the nav link brings us to the correct page.
     *
     * @throws Exception
     */
    @Test
    public void testSearchLink() throws Exception {
        driver.get("http://localhost:8080/G3BookStore/");
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.titleIs("MankabookS"));

        driver.findElement(By.id("searchLink")).click();

        wait.until(ExpectedConditions.titleIs("Search"));
    }

    /**
     * Method to test if the nav link brings us to the correct page.
     *
     * @throws Exception
     */
    @Test
    public void testBrowseLink() throws Exception {
        driver.get("http://localhost:8080/G3BookStore/");

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.titleIs("MankabookS"));

        driver.findElement(By.id("browseLink")).click();

        wait.until(ExpectedConditions.titleIs("Browse"));
    }

    /**
     * Method to test if the nav link brings us to the correct page.
     *
     * @throws Exception
     */
    @Test
    public void testFAQLink() throws Exception {
        driver.get("http://localhost:8080/G3BookStore/");

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.titleIs("MankabookS"));

        driver.findElement(By.id("faqLink")).click();

        wait.until(ExpectedConditions.titleIs("FAQ"));
    }

    /**
     * Method to test if the nav link brings us to the correct page.
     *
     * @throws Exception
     */
    @Test
    public void testCartLink() throws Exception {
        driver.get("http://localhost:8080/G3BookStore/");

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.titleIs("MankabookS"));

        driver.findElement(By.id("shop-cart")).click();

        wait.until(ExpectedConditions.titleIs("Shopping Cart"));
    }

    /**
     * Method to test if the nav link brings us to the correct page.
     *
     * @throws Exception
     */
    @Test
    public void testLogInLink() throws Exception {
        driver.get("http://localhost:8080/G3BookStore/");

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.titleIs("MankabookS"));

        driver.findElement(By.xpath("//*[@id=\"navbar\"]/ul/li[5]/a")).click();

        wait.until(ExpectedConditions.titleIs("Log In"));
    }

    /**
     * Method to test if the nav link brings us to the correct page.
     *
     * @throws Exception
     */
    @Test
    public void testRegistrationLink() throws Exception {
        driver.get("http://localhost:8080/G3BookStore/");

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.titleIs("MankabookS"));

        driver.findElement(By.xpath("//*[@id=\"navbar\"]/ul/li[6]/a")).click();

        wait.until(ExpectedConditions.titleIs("Registration"));
    }

    /**
     * Method to test if the nav link brings us to the correct page.
     *
     * @throws Exception
     */
    @Test
    public void testOrderHistoryLink() throws Exception {
        driver.get("http://localhost:8080/G3BookStore/");

        driver.findElement(By.xpath("//*[@id=\"navbar\"]/ul/li[5]/a")).click();

        WebElement inputElement = driver.findElement(By.id("loginForm:username"));
        inputElement.clear();
        inputElement.sendKeys("alessandromciotola@gmail.com");

        inputElement = driver.findElement(By.id("loginForm:password"));
        inputElement.clear();
        inputElement.sendKeys("pw");

        driver.findElement(By.id("loginForm:login")).click();

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.titleIs("MankabookS"));

        WebElement element = driver.findElement(By.xpath("//*[@id=\"j_idt53:submenuLink\"]/a/span[2]"));
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", element);

        WebElement element2 = driver.findElement(By.xpath("//*[@id=\"j_idt53:submenuLink\"]/ul/li[1]/a/span"));
        executor.executeScript("arguments[0].click();", element2);

        wait.until(ExpectedConditions.titleIs("Order History"));
    }
   
    /**
     * Method to test if the nav link brings us to the correct page.
     *
     * @throws Exception
     */
    @Test
    public void testAccountInformationLink() throws Exception {
        driver.get("http://localhost:8080/G3BookStore/");

        driver.findElement(By.xpath("//*[@id=\"navbar\"]/ul/li[5]/a")).click();

        WebElement inputElement = driver.findElement(By.id("loginForm:username"));
        inputElement.clear();
        inputElement.sendKeys("alessandromciotola@gmail.com");

        inputElement = driver.findElement(By.id("loginForm:password"));
        inputElement.clear();
        inputElement.sendKeys("pw");

        driver.findElement(By.id("loginForm:login")).click();

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.titleIs("MankabookS"));

        WebElement element = driver.findElement(By.xpath("//*[@id=\"j_idt53:submenuLink\"]/a/span[2]"));
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", element);

        WebElement element2 = driver.findElement(By.xpath("//*[@id=\"j_idt53:submenuLink\"]/ul/li[2]/a/span"));
        executor.executeScript("arguments[0].click();", element2);

        wait.until(ExpectedConditions.titleIs("Account Information"));
    }
    
    /**
     * Method to test if the nav link brings us to the correct page.
     *
     * @throws Exception
     */
    @Test
    public void testLibraryLink() throws Exception {
        driver.get("http://localhost:8080/G3BookStore/");

        driver.findElement(By.xpath("//*[@id=\"navbar\"]/ul/li[5]/a")).click();

        WebElement inputElement = driver.findElement(By.id("loginForm:username"));
        inputElement.clear();
        inputElement.sendKeys("alessandromciotola@gmail.com");

        inputElement = driver.findElement(By.id("loginForm:password"));
        inputElement.clear();
        inputElement.sendKeys("pw");

        driver.findElement(By.id("loginForm:login")).click();

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.titleIs("MankabookS"));

        WebElement element = driver.findElement(By.xpath("//*[@id=\"j_idt53:submenuLink\"]/a/span[2]"));
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", element);

        WebElement element2 = driver.findElement(By.xpath("//*[@id=\"j_idt53:submenuLink\"]/ul/li[3]/a/span"));
        executor.executeScript("arguments[0].click();", element2);

        wait.until(ExpectedConditions.titleIs("Order History"));
    }
    
    /**
     * Method to test if the nav link brings us to the correct page.
     *
     * @throws Exception
     */
    @Test
    public void testLogOutLink() throws Exception {
        driver.get("http://localhost:8080/G3BookStore/");

        driver.findElement(By.xpath("//*[@id=\"navbar\"]/ul/li[5]/a")).click();

        WebElement inputElement = driver.findElement(By.id("loginForm:username"));
        inputElement.clear();
        inputElement.sendKeys("alessandromciotola@gmail.com");

        inputElement = driver.findElement(By.id("loginForm:password"));
        inputElement.clear();
        inputElement.sendKeys("pw");

        driver.findElement(By.id("loginForm:login")).click();

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.titleIs("MankabookS"));

        WebElement element = driver.findElement(By.xpath("//*[@id=\"j_idt53:submenuLink\"]/a/span[2]"));
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", element);

        WebElement element2 = driver.findElement(By.xpath("//*[@id=\"j_idt53:submenuLink\"]/ul/li[5]/a/span"));
        executor.executeScript("arguments[0].click();", element2);

        wait.until(ExpectedConditions.titleIs("MankabookS"));
    }
}
