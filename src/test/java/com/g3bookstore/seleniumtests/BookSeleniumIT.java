package com.g3bookstore.seleniumtests;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import java.util.List;
import java.util.logging.Logger;
import static org.assertj.core.api.Assertions.assertThat;
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
 * Class that uses Selenium testing to test UI component features of the book page
 * 
 * @author Kevin
 */

public class BookSeleniumIT {

    private static final Logger LOGGER = Logger.getLogger(BookSeleniumIT.class.getName());
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
     * Test that will check if the proper book is displayed
     *
     * @throws Exception
     */
    @Test
    public void testBookPage() throws Exception {
        webDriver.manage().window().maximize();
        webDriver.get("http://localhost:8080/G3BookStore/client/book.xhtml?bookid=1");
        wait.until(ExpectedConditions.titleIs("Leonardo da Vinci"));
    }

    /**
     * Test that add book with id 1 inside the shopping cart
     *
     * @throws Exception
     */
    @Test
    public void addBookToCart() throws Exception {
        webDriver.manage().window().maximize();
        webDriver.get("http://localhost:8080/G3BookStore/client/book.xhtml?bookid=1");
        wait.until(ExpectedConditions.titleIs("Leonardo da Vinci"));

        webDriver.findElement(By.cssSelector("#track-to-cart\\:addtocart")).click();
        Thread.sleep(3000);

        wait.until(ExpectedConditions.titleIs("Shopping Cart"));
    }

    /**
     * Test that will check if the book with ID 1 has 4 similar books by genre
     *
     * @throws Exception
     */
    @Test
    public void similarGenreBook() throws Exception {
        webDriver.manage().window().maximize();
        webDriver.get("http://localhost:8080/G3BookStore/client/book.xhtml?bookid=1");
        wait.until(ExpectedConditions.titleIs("Leonardo da Vinci"));

        webDriver.findElement(By.className("similar-book-div"));
        List<WebElement> similarBooks = webDriver.findElements(By.className("row-genre"));
        assertThat(similarBooks).hasSize(4);
    }

    /**
     * Test that will check that book with ID 1 has one similar book by author
     *
     * @throws Exception
     */
    @Test
    public void similarAuthorBook() throws Exception {
        webDriver.manage().window().maximize();
        webDriver.get("http://localhost:8080/G3BookStore/client/book.xhtml?bookid=1");
        wait.until(ExpectedConditions.titleIs("Leonardo da Vinci"));

        webDriver.findElement(By.className("similar-book-div"));
        List<WebElement> similarBooks = webDriver.findElements(By.className("row-author"));
        assertThat(similarBooks).hasSize(1);
    }

    /**
     * Test that will click the first book in the similar book by genre
     *
     * @throws Exception
     */
    @Test
    public void clickFirstSimilarGenreBook() throws Exception {
        webDriver.manage().window().maximize();
        webDriver.get("http://localhost:8080/G3BookStore/client/book.xhtml?bookid=1");
        wait.until(ExpectedConditions.titleIs("Leonardo da Vinci"));

        webDriver.findElement(By.className("similar-book-div"));
        List<WebElement> similarBooks = webDriver.findElements(By.className("row-genre"));
        JavascriptExecutor executor = (JavascriptExecutor) webDriver;
        executor.executeScript("arguments[0].click();", similarBooks.get(0));
        Thread.sleep(3000);
    }

    /**
     * Test that will click the first book in the similar book by genre
     *
     * @throws Exception
     */ 
    @Test
    public void clickFirstSimilarAuthorBook() throws Exception {
        webDriver.manage().window().maximize();
        webDriver.get("http://localhost:8080/G3BookStore/client/book.xhtml?bookid=1");
        wait.until(ExpectedConditions.titleIs("Leonardo da Vinci"));

        webDriver.findElement(By.className("similar-book-div"));
        List<WebElement> similarBooks = webDriver.findElements(By.className("row-author"));
        JavascriptExecutor executor = (JavascriptExecutor) webDriver;
        executor.executeScript("arguments[0].click();", similarBooks.get(0));
        Thread.sleep(3000);
    }

    /**
     * Test that will log a user in, select the second book from the recently added
     * (Because the first one, the user already bought, so he cannot write a review),
     * clicks on write review, and submits a review
     * 
     * @throws Exception 
     */
    @Test
    public void writeReview() throws Exception {
        webDriver.manage().window().maximize();

        // Login
        webDriver.get("http://localhost:8080/G3BookStore/client/login.xhtml");
        WebElement usernameInput = webDriver.findElement(By.id("loginForm:username"));
        WebElement passwordInput = webDriver.findElement(By.id("loginForm:password"));

        usernameInput.clear();
        passwordInput.clear();

        usernameInput.sendKeys("kb@gmail.com");
        passwordInput.sendKeys("pw");

        webDriver.findElement(By.id("loginForm:login")).click();
        Thread.sleep(1000);
        wait.until(ExpectedConditions.titleIs("MankabookS"));

        // Click on the second book recently added
        WebElement book = webDriver.findElement(By.xpath("//*[@id=\"j_idt89:0:j_idt92\"]"));
        book.click();
        JavascriptExecutor executor = (JavascriptExecutor) webDriver;
        Thread.sleep(1000);

        // Click on write review
        webDriver.findElement(By.id("write-review:write-review-btn")).click();
        Thread.sleep(1000);

        // Write the comment
        webDriver.findElement(By.className("rating-stars-container"));
        List<WebElement> stars = webDriver.findElements(By.className("rating-star"));
        executor.executeScript("arguments[0].click();", stars.get(4));
        
        WebElement title = webDriver.findElement(By.id("reviewForm:title-text-area"));
        title.sendKeys("Title Selenium Test");
        
        WebElement review = webDriver.findElement(By.id("reviewForm:review-text-area"));
        review.sendKeys("Selenium Test Review");
        
        // Submit the review
        webDriver.findElement(By.id("reviewForm:review-submit")).click();
        
        // Check if comment was submitted
        wait.until(ExpectedConditions.titleIs("Review Pending"));
    }
}
