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
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Class that will test the UI components of the index.xhtml page
 *
 * @author Kevin
 */
public class IndexSeleniumIT {

    private static final Logger LOGGER = Logger.getLogger(IndexSeleniumIT.class.getName());
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
     * Test which will test that the books on sale actually have 10 books on
     * sale.
     *
     * @throws Exception
     */
    @Test
    public void testBooksOnSaleCount() throws Exception {
        webDriver.manage().window().maximize();
        webDriver.get("http://localhost:8080/G3BookStore/client/index.xhtml");
        wait.until(ExpectedConditions.titleIs("MankabookS"));

        WebElement salesContainer = webDriver.findElement(By.className("wrap"));
        List<WebElement> books = salesContainer.findElements(By.className("sales-images"));
        assertThat(books).hasSize(10);
    }

    /**
     * Test that clicks on the first book on sale
     *
     * @throws Exception
     */
    @Test
    public void testClickOnFirstBookOnSale() throws Exception {
        webDriver.manage().window().maximize();
        webDriver.get("http://localhost:8080/G3BookStore/client/index.xhtml");
        wait.until(ExpectedConditions.titleIs("MankabookS"));

        WebElement salesContainer = webDriver.findElement(By.className("wrap"));
        List<WebElement> books = salesContainer.findElements(By.className("sales-images"));
        JavascriptExecutor executor = (JavascriptExecutor) webDriver;
        executor.executeScript("arguments[0].click();", books.get(0));
        Thread.sleep(3000);
    }

    /**
     * Test which will test that the recent books added actually have 4 books
     *
     * @throws Exception
     */
    @Test
    public void testRecentBooks() throws Exception {
        webDriver.manage().window().maximize();
        webDriver.get("http://localhost:8080/G3BookStore/client/index.xhtml");
        wait.until(ExpectedConditions.titleIs("MankabookS"));

        WebElement salesContainer = webDriver.findElement(By.className("most-recent-books-added"));
        List<WebElement> books = salesContainer.findElements(By.className("btn-img"));
        assertThat(books).hasSize(4);
        webDriver.quit();
    }

    /**
     * Test which will test that if the user clicks the first book recently
     * added
     *
     * @throws Exception
     */
    @Test
    public void testClickFirstRecentBook() throws Exception {
        webDriver.manage().window().maximize();
        webDriver.get("http://localhost:8080/G3BookStore/client/index.xhtml");
        wait.until(ExpectedConditions.titleIs("MankabookS"));

        WebElement salesContainer = webDriver.findElement(By.className("most-recent-books-added"));
        List<WebElement> books = salesContainer.findElements(By.className("btn-img"));
        JavascriptExecutor executor = (JavascriptExecutor) webDriver;
        executor.executeScript("arguments[0].click();", books.get(0));
        Thread.sleep(3000);
    }

    /**
     * Test which will test that the genres displayed on the index page is
     * correct
     *
     * @throws Exception
     */
    @Test
    public void testGenres() throws Exception {
        webDriver.manage().window().maximize();
        webDriver.get("http://localhost:8080/G3BookStore/client/index.xhtml");
        wait.until(ExpectedConditions.titleIs("MankabookS"));

        WebElement genreContainer = webDriver.findElement(By.className("book-placeholder"));
        List<WebElement> genres = genreContainer.findElements(By.className("genre-styling"));
        assertThat(genres).hasSize(16);
    }

    /**
     * Test which clicks on the first genre from the list on the index page
     *
     * @throws Exception
     */
    @Ignore
    @Test
    public void testClickFirstGenre() throws Exception {
        webDriver.manage().window().maximize();
        webDriver.get("http://localhost:8080/G3BookStore/client/index.xhtml");
        wait.until(ExpectedConditions.titleIs("MankabookS"));

        WebElement genreContainer = webDriver.findElement(By.className("book-placeholder"));
        List<WebElement> genres = genreContainer.findElements(By.className("genre-styling"));
        JavascriptExecutor executor = (JavascriptExecutor) webDriver;
        executor.executeScript("arguments[0].click();", genres.get(0));
        Thread.sleep(3000);
        wait.until(ExpectedConditions.titleIs("Browse"));
    }

    /**
     * Test which will test UI functionality by clicking the first ad
     *
     * @throws Exception
     */
    @Test
    public void testClickFirstAd() throws Exception {
        webDriver.manage().window().maximize();
        webDriver.get("http://localhost:8080/G3BookStore/client/index.xhtml");
        wait.until(ExpectedConditions.titleIs("MankabookS"));

        WebElement genreContainer = webDriver.findElement(By.className("ads-container"));
        List<WebElement> ads = genreContainer.findElements(By.className("ads-single"));
        JavascriptExecutor executor = (JavascriptExecutor) webDriver;
        executor.executeScript("arguments[0].click();", ads.get(0));
        Thread.sleep(3000);
    }

    /**
     * Test which will ensure that the advertisement section has 2 ads
     *
     * @throws Exception
     */
    @Test
    public void testAds() throws Exception {
        webDriver.manage().window().maximize();
        webDriver.get("http://localhost:8080/G3BookStore/client/index.xhtml");
        wait.until(ExpectedConditions.titleIs("MankabookS"));

        WebElement genreContainer = webDriver.findElement(By.className("advertisements-section"));
        List<WebElement> ads = genreContainer.findElements(By.className("book-images"));
        assertThat(ads).hasSize(2);
    }

    /**
     * Test that will test the UI component of the Quick Search of the index page
     * 
     * @throws Exception 
     */
    @Test
    public void testQuickSearch() throws Exception {
        webDriver.manage().window().maximize();
        webDriver.get("http://localhost:8080/G3BookStore/client/index.xhtml");
        wait.until(ExpectedConditions.titleIs("MankabookS"));

        Select option = new Select(webDriver.findElement(By.name("searchForm:choice")));
        option.selectByVisibleText("Title(s)");
        Thread.sleep(3000);
        WebElement keyword = webDriver.findElement(By.id("searchForm:input"));
        keyword.sendKeys("king");
        Thread.sleep(3000);
        webDriver.findElement(By.id("searchForm:search")).click();
        wait.until(ExpectedConditions.titleIs("Search"));
        Thread.sleep(5000);
    }

    /**
     * Test that will click on the first news of the Rss News Feed from the index
     * page
     * 
     * @throws Exception 
     */
    @Test
    public void testClickFirstRssFeedNews() throws Exception {
        webDriver.manage().window().maximize();
        webDriver.get("http://localhost:8080/G3BookStore/client/index.xhtml");
        wait.until(ExpectedConditions.titleIs("MankabookS"));

        WebElement rssFeedContainer = webDriver.findElement(By.className("news-section"));
        List<WebElement> news = rssFeedContainer.findElements(By.className("news-styling"));
        JavascriptExecutor executor = (JavascriptExecutor) webDriver;
        executor.executeScript("arguments[0].click();", news.get(0));
        Thread.sleep(1000);
    }
    
    /**
     * Test that will click on the team picture
     * 
     * @throws Exception 
     */
    @Test
    public void testClickTeamImage() throws Exception {
        webDriver.manage().window().maximize();
        webDriver.get("http://localhost:8080/G3BookStore/client/index.xhtml");
        wait.until(ExpectedConditions.titleIs("MankabookS"));
        
        WebElement image = webDriver.findElement(By.className("the-team-image"));
        image.click();
        Thread.sleep(3000);
        
        wait.until(ExpectedConditions.titleIs("The Team"));
    }
}
