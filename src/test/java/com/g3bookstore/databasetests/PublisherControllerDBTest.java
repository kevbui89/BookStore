package com.g3bookstore.databasetests;

import com.g3bookstore.backingbeans.LocaleChanger;
import com.g3bookstore.controllers.BookJpaController;
import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import com.g3bookstore.converters.ProvinceConverter;
import com.g3bookstore.customcontrollers.CustomBookJpaController;
import com.g3bookstore.customcontrollers.CustomPublisherJpaController;
import com.g3bookstore.entities.Book;
import com.g3bookstore.entities.Publisher;
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
import org.junit.runner.RunWith;
import org.junit.Test;

/**
 * Test class for the Publisher Controller
 * 
 * @author Kevin Bui
 */
@Ignore
@RunWith(Arquillian.class)
public class PublisherControllerDBTest {
    
    @Inject
    private CustomPublisherJpaController publisherController;
    
    @Resource(name = "java:app/jdbc/bookstore")
    private DataSource ds;
    
    private Logger log = Logger.getLogger(PublisherControllerDBTest.class.getName());
    
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
     * Test that checks if all the correct books for a specific publisher is
     * returned.
     * @throws SQLException 
     */
    @Test
    public void findAllBooksForPublisher() throws SQLException
    {
        Publisher publisher = publisherController.getPublisherByID(21);
        List<Book> books = publisherController.getAllBooksByPublisher(publisher);
        log.log(Level.INFO, "Data>>>{0}", books.get(0).getPublisherId());
        assertThat(books).hasSize(2);
    }
    
      /**
     * Test which will check if the correct publisher is returned when searching 
     * by primary key.
     * 
     * @throws SQLException
     */
    @Test
    public void findPublisherByPrimaryKeyTest() throws SQLException
    {
        Publisher p = publisherController.getPublisherByID(21);
        log.log(Level.INFO, "Data>>>{0}", p.getPublisherId());
        assertEquals(p.getPublisher(), "Inkshares");
    }
    
    /**
     * Test if the method will return only one item inside the list.
     * 
     * @throws SQLException 
     */
    @Test
    public void findPublisherByExactNameTest() throws SQLException {
        List<Publisher> pubList = publisherController.getPublisherByExactName("Little");
        log.log(Level.INFO, "Data>>>{0}", pubList.get(0).getPublisher());
        assertThat(pubList).hasSize(1);
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
                .addPackage(GenreControllerDBTest.class.getPackage())
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
