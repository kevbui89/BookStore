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
 * Selenium Test to see if the survey is functioning correctly.
 * 
 * @author Alessandro Ciotola
 */
public class SurveySeleniumIT 
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
     * Method to test if the survey answer was successfully chosen.
     * 
     * @throws Exception 
     */
    @Test
    public void testSurveyChoice() throws Exception
    {
        driver.get("http://localhost:8080/G3BookStore/");

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.titleIs("MankabookS"));  
        
        WebElement btn = driver.findElement(By.xpath("//*[@id=\"survey:j_idt142:0:surveyAnswerBtn\"]"));
        Thread.sleep(5000);
        btn.click();
        Thread.sleep(5000);
        
        wait.until(ExpectedConditions.titleIs("MankabookS"));       
    }    
}
