package com.g3bookstore.seleniumtests;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import java.util.ArrayList;
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
 * Class that will test the UI component of the browse page.
 *
 * @author Kevin
 */

public class BrowseSeleniumIT {

    private static final Logger LOGGER = Logger.getLogger(BrowseSeleniumIT.class.getName());
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
     * Test that check if the proper page is loaded if the client enters the
     * browse page
     *
     * @throws Exception
     */
    @Test
    public void testBrowsePage() throws Exception {
        webDriver.manage().window().maximize();
        webDriver.get("http://localhost:8080/G3BookStore/client/browse_genre.xhtml");
        wait.until(ExpectedConditions.titleIs("Browse"));
    }

    /**
     * Test that checks that all 16 genres are there
     *
     * @throws Exception
     */
    @Test
    public void testCountAllGenre() throws Exception {
        webDriver.manage().window().maximize();
        webDriver.get("http://localhost:8080/G3BookStore/client/browse_genre.xhtml");
        wait.until(ExpectedConditions.titleIs("Browse"));

        List<WebElement> genres = webDriver.findElements(By.className("spacing-genre"));
        assertThat(genres).hasSize(16);
    }

    /**
     * Test that clicks on all the genres and confirms that the right genre
     * appears on the screen of the user
     *
     * @throws Exception
     */
    @Test
    public void checkAllGenres() throws Exception {
        webDriver.manage().window().maximize();
        webDriver.get("http://localhost:8080/G3BookStore/client/browse_genre.xhtml");
        wait.until(ExpectedConditions.titleIs("Browse"));

        // String array of all the genres
        List<WebElement> elements = webDriver.findElements(By.className("spacing-genre"));

        for (int i = 1; i <= elements.size(); i++) {
            webDriver.get("http://localhost:8080/G3BookStore/client/browse_genre.xhtml");
            wait.until(ExpectedConditions.titleIs("Browse"));
            
            WebElement dash = webDriver.findElement(By.xpath("//*[@id=\"content\"]/section/div[1]/div/i"));
            dash.click();
            
            Thread.sleep(1000);
            
            webDriver.findElement(By.cssSelector(".menu-content > "
                    + "li:nth-child(" + i + ") > a:nth-child(1)")).click();
        }
    }

    /**
     * Test that browses each genre and checks if the books inside the genres 
     * are the right books
     * 
     * @throws Exception 
     */
    @Test
    public void browseEachGenreBooks() throws Exception {
        webDriver.manage().window().maximize();
        webDriver.get("http://localhost:8080/G3BookStore/client/browse_genre.xhtml");
        wait.until(ExpectedConditions.titleIs("Browse"));

        // String array of all the genres
        List<WebElement> elements = webDriver.findElements(By.className("spacing-genre"));
        List<String> genres = new ArrayList<>();
        for (int i = 0; i < elements.size(); i++) {
            genres.add(elements.get(i).getText());
        }

        for (int i = 1; i <= elements.size(); i++) {
            webDriver.get("http://localhost:8080/G3BookStore/client/browse_genre.xhtml");
            wait.until(ExpectedConditions.titleIs("Browse"));
            
            WebElement dash = webDriver.findElement(By.xpath("//*[@id=\"content\"]/section/div[1]/div/i"));
            dash.click();
            
            Thread.sleep(1000);

            // Click on the current genre
            webDriver.findElement(By.cssSelector(".menu-content > "
                    + "li:nth-child(" + i + ") > a:nth-child(1)")).click();

            // Get the list of books and click on the first one
            List<WebElement> list = webDriver.findElements(By.className("wrapper"));
            JavascriptExecutor executor = (JavascriptExecutor) webDriver;
            executor.executeScript("arguments[0].click();", list.get(0));
            //Thread.sleep(3000);
            
            // Get the book genres
            List<WebElement> bookGenres = webDriver.findElements(By.className("genre-book"));
            String genre = "";
            for (int k = 0; k < bookGenres.size(); k++) {
                genre = genre + bookGenres.get(k).getText();
            }
            
            assertThat(genre.toLowerCase()).contains(genres.get(i - 1).toLowerCase());
        }
    }

}
