package com.g3bookstore.databasetests;

import com.g3bookstore.backingbeans.LocaleChanger;
import com.g3bookstore.controllers.BookJpaController;
import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import com.g3bookstore.converters.ProvinceConverter;
import com.g3bookstore.customcontrollers.CustomAuthorJpaController;
import com.g3bookstore.customcontrollers.CustomBookJpaController;
import com.g3bookstore.customcontrollers.CustomClientJpaController;
import com.g3bookstore.customcontrollers.CustomManagerController;
import com.g3bookstore.customcontrollers.CustomPublisherJpaController;
import com.g3bookstore.entities.Author;
import com.g3bookstore.entities.Book;
import com.g3bookstore.entities.Client;
import com.g3bookstore.entities.Publisher;
import com.g3bookstore.rssnews.FeedMessage;
import com.g3bookstore.utilities.Seeder;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;
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
 * Test class for the Manager Controller
 *
 * @author Werner Jose, Alessandro Ciotola
 */
@Ignore
@RunWith(Arquillian.class)
public class ManagerControllerDBTest {

    @Inject
    private CustomManagerController managerController;
    @Inject
    private CustomPublisherJpaController publisherController;
    @Inject
    private CustomAuthorJpaController authorController;
    @Inject
    private CustomClientJpaController clientController;

    @Resource(name = "java:app/jdbc/bookstore")
    private DataSource ds;

    private Logger log = Logger.getLogger(CountryControllerDBTest.class.getName());

    public ManagerControllerDBTest() {
    }

    /**
     * Seeds the database before each test
     */
    @Before
    public void seedDatabase() {
        Seeder db = new Seeder(ds);
        db.seedDatabase();
    }

    /**
     * Test total sales of book by publisher and date.
     * 
     */
    @Test
    public void testTotalSaleByPublisherAndDate() {
        Date startDate = Date.valueOf(LocalDate.of(2018, 03, 01));
        Date endDate = Date.valueOf(LocalDate.of(2018, 03, 05));
        Publisher publisher = publisherController.getPublisherByExactName("Disney").get(0);
        
        List<Book> bookList = managerController.totalSaleByPublisherAndDate(publisher, startDate, endDate);      
        log.log(Level.INFO, "list size>>>{0}", managerController.totalSoldByBook(bookList.get(0)));
        assertEquals(2, bookList.size());
    }
    
    /**
     * Test top sellers of books ordered by sales.
     * 
     */
    @Test
    public void testBookTopSellerOrderBySales() {
        Date startDate = Date.valueOf(LocalDate.of(2018, 03, 01));
        Date endDate = Date.valueOf(LocalDate.of(2018, 03, 05));
        
        List<Book> bookList = managerController.bookTopSellerOrderBySales(startDate, endDate);      
        log.log(Level.INFO, "list size>>>{0}", managerController.totalSoldByBook(bookList.get(0)));
        assertEquals("The Lion King", bookList.get(0).getTitle());
    }
    
    /**
     * Test client top sellers order by purchases
     * 
     */
    @Test
    public void testClientTopSellerOrderByPurchases() {
        Date startDate = Date.valueOf(LocalDate.of(2018, 03, 01));
        Date endDate = Date.valueOf(LocalDate.of(2018, 03, 05));
        
        List<Client> clientList = managerController.clientTopSellerOrderByPurchases(startDate, endDate);      
        log.log(Level.INFO, "client size>>>{0}", clientList.size());
        assertEquals("aleciot", clientList.get(0).getUsername());
    }
    
    /**
     * Test all books never sold
     * 
     */
    @Test
    public void testAllBookNeverSold() {
        Date startDate = Date.valueOf(LocalDate.of(2020, 03, 01));
        Date endDate = Date.valueOf(LocalDate.of(2020, 03, 05));
        
        List<Book> bookList = managerController.allBookNeverSold(startDate, endDate);      
        log.log(Level.INFO, "book size>>>{0}", bookList.size());
        assertEquals(101, bookList.size());
    }
    
    /**
     * Test all books sold by author.
     * 
     */
    @Test
    public void testAllBookSoldByAuthorOrderedByDate() {
        Date startDate = Date.valueOf(LocalDate.of(2018, 03, 01));
        Date endDate = Date.valueOf(LocalDate.of(2018, 03, 05));
        Author author = authorController.getAuthorByID(91);
        
        List<Book> bookList = managerController.allBookSoldByAuthorOrderedByDate(author, startDate, endDate);      
        log.log(Level.INFO, "book size>>>{0}", bookList.size());
        assertEquals(2, bookList.size());
    }
    
    /**
     * Test all books bought by clients
     * 
     */
    @Test
    public void testAllBookBoughtByClientOrderedByDate() {
        Date startDate = Date.valueOf(LocalDate.of(2018, 03, 01));
        Date endDate = Date.valueOf(LocalDate.of(2018, 03, 05));
        Client client = clientController.getClientByID(1);
        
        List<Book> bookList = managerController.allBookBoughtByClientOrderedByDate(client, startDate, endDate);      
        log.log(Level.INFO, "book size>>>{0}", bookList.size());
        assertEquals(1, bookList.size());
    }
    
    /**
     * Test all books sold by a publisher.
     * 
     */
    @Test
    public void testAllBookSoldByPublisherOrderedByDate() {
        Date startDate = Date.valueOf(LocalDate.of(2018, 03, 01));
        Date endDate = Date.valueOf(LocalDate.of(2018, 03, 05));
        Publisher publisher = publisherController.getPublisherByExactName("Disney").get(0);
        
        List<Book> bookList = managerController.allBookSoldByPublisherOrderedByDate(publisher, startDate, endDate);      
        log.log(Level.INFO, "book size>>>{0}", bookList.size());
        assertEquals("The Lion King", bookList.get(0).getTitle());
    }
    
    /**
     * Test get total money made.
     * 
     */
    @Test
    public void testTotalMoneyMade() {
        Date startDate = Date.valueOf(LocalDate.of(2018, 03, 01));
        Date endDate = Date.valueOf(LocalDate.of(2018, 03, 05));
        
        BigDecimal money = managerController.totalMoneyMade(startDate, endDate);      
        log.log(Level.INFO, "Total money made: ", money.doubleValue());
        assertEquals(2299.48, money.doubleValue(), 0);
    }
    
    /**
     * Test get total money made by author.
     * 
     */
    @Test
    public void testTotalMoneyByAuthor() {
        Date startDate = Date.valueOf(LocalDate.of(2018, 03, 01));
        Date endDate = Date.valueOf(LocalDate.of(2018, 03, 05));
        Author author = authorController.getAuthorByID(91);
        
        BigDecimal money = managerController.totalMoneyByAuthor(author, startDate, endDate);      
        log.log(Level.INFO, "Total money made: ", money.doubleValue());
        assertEquals(2299.48, money.doubleValue(), 0);
    }
    
    /**
     * Test get total money made by client.
     * 
     */
    @Test
    public void testTotalMoneyByClient() {
        Date startDate = Date.valueOf(LocalDate.of(2018, 03, 01));
        Date endDate = Date.valueOf(LocalDate.of(2018, 03, 05));
        Client client = clientController.getClientByID(1);
        
        BigDecimal money = managerController.totalMoneyByClient(client, startDate, endDate);      
        log.log(Level.INFO, "Total money made: ", money.doubleValue());
        assertEquals(1149.74, money.doubleValue(), 0);
    }
    
    /**
     * Test get total money made by publisher.
     * 
     */
    @Test
    public void testTotalMoneyByPublisher() {
        Date startDate = Date.valueOf(LocalDate.of(2018, 03, 01));
        Date endDate = Date.valueOf(LocalDate.of(2018, 03, 05));
        Publisher publisher = publisherController.getPublisherByExactName("Disney").get(0);
        
        BigDecimal money = managerController.totalMoneyByPublisher(publisher, startDate, endDate);      
        log.log(Level.INFO, "Total money made: ", money.doubleValue());
        assertEquals(2299.48, money.doubleValue(), 0);
    }    
    
    @Deployment
    public static WebArchive deploy() {
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
