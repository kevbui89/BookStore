package com.g3bookstore.databasetests;

import com.g3bookstore.backingbeans.LocaleChanger;
import com.g3bookstore.controllers.BookJpaController;
import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import com.g3bookstore.converters.ProvinceConverter;
import com.g3bookstore.customcontrollers.CustomBookJpaController;
import com.g3bookstore.customcontrollers.CustomClientJpaController;
import com.g3bookstore.entities.Book;
import com.g3bookstore.entities.Client;
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
 * Test class for the Client
 *
 * @author Alessandro Ciotola
 */
@Ignore
@RunWith(Arquillian.class)
public class ClientControllerDBTest {

    @Inject
    private CustomClientJpaController customClientController;
    
    @Resource(name = "java:app/jdbc/bookstore")
    private DataSource ds;
    
    private Logger log = Logger.getLogger(CountryControllerDBTest.class.getName());

    public ClientControllerDBTest() {
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
     * Test which will check if the correct client is returned when searching 
     * by primary key.
     * 
     * @throws SQLException
     */
    @Test
    public void findClientByPrimaryKeyTest() throws SQLException
    {
        Client client = customClientController.getClientByID(1);
        log.log(Level.INFO, "Data>>>{0}", client.getUsername());
        assertEquals(client.getUsername(),"aleciot");
    }
    
    /**
     * Test which will check if the correct client is returned when searching 
     * by userName.
     * 
     * @throws SQLException
     */
    @Test
    public void findClientByUsernameTest() throws SQLException
    {
        List<Client> client = customClientController.getClientByUserName("aleciot");
        log.log(Level.INFO, "Data>>>{0}", client.get(0).getUsername());
        assertEquals(client.get(0).getEmail(),"alessandromciotola@gmail.com");
    }
    
    /**
     * Test which will check if the correct client is returned when searching 
     * by email.
     * 
     * @throws SQLException
     */
    @Test
    public void findClientByEmailTest() throws SQLException
    {
        List<Client> client = customClientController.getClientByEmail("alessandromciotola@gmail.com");
        log.log(Level.INFO, "Data>>>{0}", client.get(0).getUsername());
        assertEquals(client.get(0).getUsername(),"aleciot");
    }
    
    /**
     * Test which will check if the correct total sales by a specific client
     * is properly returned
     * 
     * @throws SQLException
     */
    @Test
    public void findClientByTotalSalesTest() throws SQLException
    {
        double clientSales = customClientController.getClientByTotalSales("aleciot").doubleValue();
        log.log(Level.INFO, "Data>>>{0}", clientSales);
        assertEquals(clientSales, 13.77, 0);
    }
    
    /**
     * Test which will check if the correct books are returned when searching 
     * for all books purchased by a specific client.
     * 
     * @throws SQLException
     */
    @Test
    public void findBooksByClientTest() throws SQLException
    {
        List<Book> bookList = customClientController.getClientByItemsPurchased("aleciot");
        log.log(Level.INFO, "Data>>>{0}", bookList.get(0).getTitle());
        assertThat(bookList).hasSize(3);
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
                .addPackage(ClientControllerDBTest.class.getPackage())
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
