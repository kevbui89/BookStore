package com.g3bookstore.databasetests;

import com.g3bookstore.backingbeans.LocaleChanger;
import com.g3bookstore.controllers.BookJpaController;
import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import com.g3bookstore.converters.ProvinceConverter;
import com.g3bookstore.customcontrollers.CustomBookJpaController;
import com.g3bookstore.customcontrollers.CustomReviewJpaController;
import com.g3bookstore.entities.Book;
import com.g3bookstore.entities.Review;
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
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test class for the Review Controller
 * 
 * @author Kevin
 */
@Ignore
@RunWith(Arquillian.class)
public class ReviewControllerDBTest {
    
    @Inject
    private CustomReviewJpaController customReviewController;
    
    @Inject
    private CustomBookJpaController customBookController;
    
    @Resource(name = "java:app/jdbc/bookstore")
    private DataSource ds;
    
    private Logger log = Logger.getLogger(CountryControllerDBTest.class.getName());
    
    public ReviewControllerDBTest() {
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
    
    /**
     * Test which will check if the correct reviews are returned for a specific book
     * 
     * @throws SQLException
     */
    @Test
    public void findReviewsByBookIdTest() throws SQLException
    {
        Book book = customBookController.getBookByID(101);
        List<Review> reviews = customReviewController.getReviewByBook(book);
        log.log(Level.INFO, "Data>>>{0}", reviews.get(0).getComment());
        assertThat(reviews).hasSize(2);
    }
    
    /**
     * Test which will check if the correct reviews are returned when searching 
     * by rating
     * 
     * @throws SQLException
     */
    @Test
    public void findReviewsByRatingTest() throws SQLException
    {
        List<Review> reviews = customReviewController.getReviewByRating(4);
        log.log(Level.INFO, "Data>>>{0}", reviews.get(0).getComment());
        assertThat(reviews).hasSize(1);
    }
    
    /**
     * Test which will check if all the reviews are being returned correctly
     * 
     * @throws SQLException
     */
    @Test
    public void findAllReviewsTest() throws SQLException
    {
        List<Review> reviews = customReviewController.getAllReviews();
        log.log(Level.INFO, "Data>>>{0}", reviews.get(0).getComment());
        assertThat(reviews).hasSize(3);
    }
    
    /**
     * Test which will check if the correct genre is returned when searching 
     * by primary key.
     * 
     * @throws SQLException
     */
    @Test
    public void findReviewByPrimaryKeyTest() throws SQLException
    {
        Review review = customReviewController.getReviewByID(1);
        log.log(Level.INFO, "Data>>>{0}", review.getComment());
        assertEquals(review.getComment(),"this book is good");
    }
    
    /**
     * Test that all approved comments for a book are correctly returned
     * @throws SQLException 
     */
    @Test
    public void findApprovedReviewsForBook() throws SQLException {
        Book b = customBookController.getBookByID(101);
        List<Review> valid = customReviewController.getApprovedReviews(b);
        for (Review r : valid) {
            assertEquals(r.getValid(), true);
        }
    }
    
    /**
     * Test that all approved comments for a book are correctly returned
     * @throws SQLException 
     */
    @Test
    public void findNonApprovedReviewsForBook() throws SQLException {
        Book b = customBookController.getBookByID(100);
        List<Review> valid = customReviewController.getApprovedReviews(b);
        for (Review r : valid) {
            assertEquals(r.getValid(), false);
        }
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
                .addPackage(ReviewControllerDBTest.class.getPackage())
                .addPackage(AdControllerDBTest.class.getPackage())
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
