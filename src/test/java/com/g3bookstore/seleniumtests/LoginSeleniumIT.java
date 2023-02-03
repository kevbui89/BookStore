package com.g3bookstore.seleniumtests;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import java.util.logging.Logger;
import static org.assertj.core.api.Assertions.assertThat;
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
 * Class that test the UI login functionality of the login.xhtml page.
 * 
 * @author Kevin
 */

public class LoginSeleniumIT {
    
    private static final Logger LOGGER = Logger.getLogger(LoginSeleniumIT.class.getName());
    private WebDriver webDriver;
    private WebDriverWait wait;
    
    /**
     * Sets up the required attributes for the Selenium tests
     */
    @Before
    public void setup() {
        ChromeDriverManager.chromedriver().setup();
        webDriver = new ChromeDriver();
        wait = new WebDriverWait(webDriver, 5);
    }
    
    /**
     * Closes the page after each test
     */
    @After
    public void quit() {
        webDriver.quit();
    }
    
    /**
     * Test which simulates a user login in with valid credentials
     * 
     * @throws Exception 
     */
    @Test
    public void testValidLogin() throws Exception {
        webDriver.manage().window().maximize();
        webDriver.get("http://localhost:8080/G3BookStore/client/login.xhtml");
        WebElement usernameInput = webDriver.findElement(By.id("loginForm:username"));
        WebElement passwordInput = webDriver.findElement(By.id("loginForm:password"));
        
        usernameInput.clear();
        passwordInput.clear();
        
        usernameInput.sendKeys("kb@gmail.com");
        passwordInput.sendKeys("pw");
        
        webDriver.findElement(By.id("loginForm:login")).click();
        
        wait.until(ExpectedConditions.titleIs("MankabookS"));
    }
    
    /**
     * Test which similates a user login in with invalid credentials
     * 
     * @throws Exception 
     */
    @Test
    public void testInvalidLogin() throws Exception {
        webDriver.manage().window().maximize();
        webDriver.get("http://localhost:8080/G3BookStore/client/login.xhtml");
        WebElement usernameInput = webDriver.findElement(By.id("loginForm:username"));
        WebElement passwordInput = webDriver.findElement(By.id("loginForm:password"));
        
        usernameInput.clear();
        passwordInput.clear();
        
        usernameInput.sendKeys("bademail@gmail.com");
        passwordInput.sendKeys("pw");
        
        webDriver.findElement(By.id("loginForm:login")).click();
        
        wait.until(ExpectedConditions.titleIs("Log In"));
        
        WebElement errorMessage = webDriver.findElement(By.cssSelector("div span#loginFormMessage"));
        assertThat(errorMessage.getText()).isEqualToIgnoringCase("Invalid username/password.");
    }
    
    /**
     * Test that similates a user trying to login without entering any fields
     * 
     * @throws Exception 
     */
    @Test
    public void testEmptyFields() throws Exception {
        webDriver.manage().window().maximize();
        webDriver.get("http://localhost:8080/G3BookStore/client/login.xhtml");
        WebElement usernameInput = webDriver.findElement(By.id("loginForm:username"));
        WebElement passwordInput = webDriver.findElement(By.id("loginForm:password"));
        
        usernameInput.clear();
        passwordInput.clear();
        
        usernameInput.sendKeys("");
        passwordInput.sendKeys("");
        
        webDriver.findElement(By.id("loginForm:login")).click();
        
        wait.until(ExpectedConditions.titleIs("Log In"));
        
        WebElement errorMessageUsername = webDriver.findElement(By.id("loginForm:usernameMessage"));
        WebElement errorMessagePassword = webDriver.findElement(By.id("loginForm:passwordMessage"));
        assertThat(errorMessageUsername.getText()).isEqualToIgnoringCase("This field is required.");
        assertThat(errorMessagePassword.getText()).isEqualToIgnoringCase("This field is required.");
    }   
}
