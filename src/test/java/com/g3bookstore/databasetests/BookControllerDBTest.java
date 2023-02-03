package com.g3bookstore.databasetests;

import com.g3bookstore.backingbeans.LocaleChanger;
import com.g3bookstore.controllers.BookJpaController;
import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import com.g3bookstore.converters.ProvinceConverter;
import com.g3bookstore.customcontrollers.CustomBookJpaController;
import com.g3bookstore.customcontrollers.CustomGenreJpaController;
import com.g3bookstore.entities.Book;
import com.g3bookstore.entities.Genre;
import com.g3bookstore.rssnews.FeedMessage;
import com.g3bookstore.utilities.Seeder;
import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;
import static org.assertj.core.api.Assertions.assertThat;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.runner.RunWith;

/**
 * Test class for the Books
 * 
 * @author Alessandro Ciotola, Kevin Bui
 */
@Ignore
@RunWith(Arquillian.class)
public class BookControllerDBTest {
    
    @Inject
    private CustomBookJpaController customBookController;
    
    @Inject
    private CustomGenreJpaController customGenreController;
    
    @Resource(name = "java:app/jdbc/bookstore")
    private DataSource ds;
    
    public BookControllerDBTest() {
    }
    
    /**
     * Seeds the database before each test
     */
    @Before
    public void seedDatabase() 
    {
        Seeder db = new Seeder(ds);
        db.seedDatabase();
    }
    
    private Logger log = Logger.getLogger(BookControllerDBTest.class.getName());
    
    /**
     * Test which will check if all the books have been received
     * 
     * @throws SQLException
     */
    @Test
    public void findAllBooksTest() throws SQLException
    {
        List<Book> bookList = customBookController.getBooks();
        log.log(Level.INFO, "Data>>>{0}", bookList.get(20).getTitle());
        assertThat(bookList).hasSize(101);
    }
    
    /**
     * Test which will check if the correct book is returned when searching 
     * by primary key.
     * 
     * @throws SQLException
     */
    @Test
    public void findBookByPrimaryKeyTest() throws SQLException
    {
        Book book = customBookController.getBookByID(20);
        log.log(Level.INFO, "Data>>>{0}", book.getTitle());
        assertEquals(book.getTitle(),"Petty: The Biography");
    }
    
    /**
     * Test which will check if the correct books are returned when searching 
     * by a similar title
     * 
     * @throws SQLException
     */
    @Test
    public void findBookLikeTitleTest() throws SQLException
    {
        List<Book> bookList = customBookController.getBookByTitle("and");
        log.log(Level.INFO, "Data>>>{0}", bookList.get(0).getTitle());
        assertThat(bookList).hasSize(13);
    }
    
    /**
     * Test which will check if the correct books are returned when searching 
     * by a similar author
     * 
     * @throws SQLException
     */
    @Test
    public void findBookLikeAuthorTest() throws SQLException
    {
        List<Book> bookList = customBookController.getBookByAuthor("Yoon");
        log.log(Level.INFO, "Data>>>{0}", bookList.get(0).getTitle());
        assertThat(bookList).hasSize(5);
    }
    
    /**
     * Test which will check if the correct books are returned when searching 
     * by a similar author
     * 
     * @throws SQLException
     */
    @Test
    public void findBookMultipleAuthorTest() throws SQLException
    {
        List<Book> bookList = customBookController.getBookByAuthor("Stephen");
        log.log(Level.INFO, "Data>>>{0}", bookList.get(0).getTitle());
        assertThat(bookList).hasSize(5);
    }
    
    /**
     * Test which will check if the correct books are returned when searching 
     * by a similar publisher
     * 
     * @throws SQLException
     */
    @Test
    public void findBookLikePublisherTest() throws SQLException
    {
        List<Book> bookList = customBookController.getBookByPublisher("Simon");
        log.log(Level.INFO, "Data>>>{0}", bookList.get(0).getTitle());
        assertThat(bookList).hasSize(6);
    }
    
    /**
     * Test which will check if the correct books are returned when searching 
     * by a similar isbn
     * 
     * @throws SQLException
     */ 
   @Test
    public void findBookByIsbnTest() throws SQLException
    {
        List<Book> bookList = customBookController.getBookByIsbn("006");
        log.log(Level.INFO, "Data>>>{0}", bookList.get(0).getTitle());
        assertThat(bookList).hasSize(5);
    }
    
    /**
     * Test if the method will return only one item inside the list.
     * 
     * @throws SQLException 
     */
    @Test
    public void findBookByExactIsbnTest() throws SQLException {
        List<Book> bookList = customBookController.getBookByExactIsbn("1400082773");
        log.log(Level.INFO, "Data>>>{0}", bookList.get(0).getTitle());
        assertThat(bookList).hasSize(1);
    }
    
    /**
     * Test which will check if the correct books are returned when searching 
     * by order of total sales
     * 
     * @throws SQLException
     */
    @Test
    public void findBookByTotalSalesTest() throws SQLException
    {
        List<Book> bookList = customBookController.getBookByTotalSales();
        log.log(Level.INFO, "Data>>>{0}", bookList.get(0).getTitle());
        assertEquals(bookList.get(0).getTitle(),"Leonardo da Vinci");
    }
    
    /**
     * Test which will check if the correct books are returned when searching 
     * for books that haven't been sold
     * 
     * @throws SQLException
     */
    @Test
    public void findBookNotSoldTest() throws SQLException
    {
        List<Book> bookList = customBookController.getBooksNotSold();
        log.log(Level.INFO, "Data>>>{0}", bookList.get(0).getTitle());
        assertThat(bookList).hasSize(101);
    }
    
    /**
     * Test which will check if the correct books are returned when searching 
     * for books that are on sale
     * 
     * @throws SQLException
     */
    @Test
    public void findBookOnSaleTest() throws SQLException
    {
        List<Book> bookList = customBookController.getBooksOnSale();
        log.log(Level.INFO, "Data>>>{0}", bookList.get(0).getTitle());
        assertThat(bookList).hasSize(26);
    }
    
    /**
     * Test which will check if the correct books are returned when searching for books
     * from a specific genre
     * @throws SQLException 
     */
    @Test
    public void findBookByGenre() throws SQLException {
        Genre g = customGenreController.getGenreByID(2);
        List<Book> bookList = customBookController.getBooksByGenre(g);
        log.log(Level.INFO, "Data>>>{0}", bookList.get(0).getTitle());
        assertThat(bookList).hasSize(25);
    }
    
    /**
     * Test if the similar books by genre returns an array of 3 books by genre and 
     * if the books returned are not equal to the original book
     * 
     * @throws SQLException 
     */
    @Test
    public void findSimilarBookByGenre() throws SQLException {
        boolean valid = true;
        
        Book b = customBookController.getBookByID(1);
        
        // Check if the size of the similar book array is 4
        List<Book> sb = customBookController.getSimilarGenreBooks(b);
        
        if (sb.size() != 4) {
            valid = false;
        }
        
        // Check if the books from the list are the same as the initial book
        for (Book book : sb) {
            if (book.equals(b)) {
                valid = false;
            }
        }
        
        assertTrue(valid);
    }
    
    /**
     * Test if the similar books by author returns an array of 3 books by author and 
     * if the books returned are not equal to the original book
     * 
     * @throws SQLException 
     */
    @Test
    public void findSimilarBookByAuthor() throws SQLException {        
        Book b = customBookController.getBookByID(30);
        
        // Check if the size of the similar book array is 3
        List<Book> sb = customBookController.getSimilarAuthorBooks(b);
        
        assertThat(sb).hasSize(2);
    }
    
    /**
     * Test if the most recent books are being returned correctly
     * 
     * @throws java.sql.SQLException
     */
    @Test
    public void getMostRecentBooks() throws SQLException {
        List<Book> mostRecent = customBookController.getMostRecentBooks(1);
        
        assertEquals(mostRecent.get(0).getTitle(), "The Lion King");
    }
    
    /**
     * Test if all the books returned from a general search will return the correct
     * books.
     * 
     * @throws SQLException 
     */
    @Test
    public void getAllBooksByKeywordTest() throws SQLException {
        List<Book> allBooks = customBookController.getAllBooksByKeyword("king");
        
        assertThat(allBooks).hasSize(6);
    }
    
    @Deployment
    public static WebArchive deploy()
    {
        // Use an alternative to the JUnit assert library called AssertJ
        // Need to reference MySQL driver as it is not part of either
        // embedded or remote
        final File[] dependencies = Maven
                .resolver()
                .loadPomFromFile("pom.xml")
                .resolve("mysql:mysql-connector-java",
                        "org.assertj:assertj-core").withoutTransitivity()
                .asFile();

        // The webArchive is the special packaging of your project
        // so that only the test cases run on the server or embedded
        // container
        final WebArchive webArchive = ShrinkWrap.create(WebArchive.class, "tests.war")
                .setWebXML(new File("src/main/webapp/WEB-INF/web.xml"))
                .addPackage(LocaleChanger.class.getPackage())
                .addPackage(Book.class.getPackage())
                .addPackage(Seeder.class.getPackage())
                .addPackage(FeedMessage.class.getPackage())
                .addPackage(BookControllerDBTest.class.getPackage())
                .addPackage(BookJpaController.class.getPackage())
                .addPackage(CustomBookJpaController.class.getPackage())
                .addPackage(RollbackFailureException.class.getPackage())
                .addPackage(ProvinceConverter.class.getPackage())
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/glassfish-resources.xml"), "glassfish-resources.xml")
                .addAsResource(new File("src/main/resources/META-INF/persistence.xml"), "META-INF/persistence.xml")
                .addAsResource("CreateBookStoreTables.sql")
                .addAsLibraries(dependencies);

        return webArchive;
    }
}
