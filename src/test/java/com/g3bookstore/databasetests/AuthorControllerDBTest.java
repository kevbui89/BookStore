package com.g3bookstore.databasetests;

import com.g3bookstore.backingbeans.LocaleChanger;
import com.g3bookstore.controllers.BookJpaController;
import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import com.g3bookstore.converters.ProvinceConverter;
import com.g3bookstore.customcontrollers.CustomAuthorJpaController;
import com.g3bookstore.customcontrollers.CustomBookJpaController;
import com.g3bookstore.entities.Author;
import com.g3bookstore.entities.Book;
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
 * Test class for the Review Controller
 * 
 * @author Kevin Bui, Alessandro Ciotola
 */
@Ignore
@RunWith(Arquillian.class)
public class AuthorControllerDBTest {
    
    @Inject
    private CustomAuthorJpaController authorController;
    
    @Inject
    private CustomBookJpaController bookController;
    
    @Resource(name = "java:app/jdbc/bookstore")
    private DataSource ds;
    
    private Logger log = Logger.getLogger(AuthorControllerDBTest.class.getName());
    
    public AuthorControllerDBTest() {
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
     * Test that will check if all books of a specific genre will be returned.
     */
    @Test
    public void findAllBooksForAuthor() throws SQLException
    {
        Author author = authorController.getAuthorByID(27);
        List<Book> books = authorController.getAllBooksByAuthor(author);
        log.log(Level.INFO, "Data>>>{0}", books.get(0).getAuthorList());
        assertThat(books).hasSize(3);
    }
    
    /**
     * Test which will check if the correct genre is returned when searching 
     * by primary key.
     * 
     * @throws SQLException
     */
    @Test
    public void findGenreByPrimaryKeyTest() throws SQLException
    {
        Author author = authorController.getAuthorByID(27);
        log.log(Level.INFO, "Data>>>{0}", author.getName());
        assertEquals(author.getName(), "Stephen King");
    }
    
    /**
     * Test if the correct authors are returned by a book
     * @throws SQLException 
     */
    @Test
    public void findAuthorsForBook() throws SQLException {
        Book book = bookController.getBookByID(1);
        log.log(Level.INFO, "Data>>>{0}", book.getBookId());
        List<Author> authors = authorController.getAuthorsForBook(book);
        assertThat(authors).hasSize(2);        
    }
    
    /**
     * Test if the method will return only one item inside the list.
     * 
     * @throws SQLException 
     */
    @Test
    public void findAuthorByExactNameTest() throws SQLException {
        List<Author> authorList = authorController.getAuthorsByExactName("Max Brooks");
        log.log(Level.INFO, "Data>>>{0}", authorList.get(0).getName());
        assertThat(authorList).hasSize(1);
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
