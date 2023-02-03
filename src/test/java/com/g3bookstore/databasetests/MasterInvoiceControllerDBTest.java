package com.g3bookstore.databasetests;

import com.g3bookstore.backingbeans.LocaleChanger;
import com.g3bookstore.controllers.BookJpaController;
import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import com.g3bookstore.converters.ProvinceConverter;
import com.g3bookstore.customcontrollers.CustomBookJpaController;
import com.g3bookstore.customcontrollers.CustomClientJpaController;
import com.g3bookstore.customcontrollers.CustomMasterInvoiceJpaController;
import com.g3bookstore.entities.Book;
import com.g3bookstore.entities.Client;
import com.g3bookstore.entities.MasterInvoice;
import com.g3bookstore.rssnews.FeedMessage;
import com.g3bookstore.utilities.Seeder;
import java.io.File;
import java.math.BigDecimal;
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
 * Test class for the MasterInvoice Controller
 *
 * @author Kevin Bui, Alessandro Ciotola
 */
@Ignore
@RunWith(Arquillian.class)
public class MasterInvoiceControllerDBTest {

    @Inject
    private CustomMasterInvoiceJpaController miController;

    @Inject
    private CustomClientJpaController clientController;

    @Resource(name = "java:app/jdbc/bookstore")
    private DataSource ds;

    private Logger log = Logger.getLogger(MasterInvoiceControllerDBTest.class.getName());

    public MasterInvoiceControllerDBTest() {
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
     * Test that will check if the correct master invoices are returned given a
     * specific client.
     *
     * @throws SQLException
     */
    @Test
    public void findMasterInvoiceForClient() throws SQLException {
        Client client = clientController.getClientByID(1);
        List<MasterInvoice> mi = miController.getMasterInvoiceForClient(client);
        log.log(Level.INFO, "MasterInvoice list size>>>{0}", mi.size());
        assertThat(mi).hasSize(2);
    }

    /**
     * Test which will check if the correct master invoice is returned when searching by
     * primary key.
     *
     * @throws SQLException
     */
    @Test
    public void findGenreByPrimaryKeyTest() throws SQLException {
        MasterInvoice mi = miController.getMasterInvoiceByID(1);
        log.log(Level.INFO, "Data>>>{0}", mi.getInvoiceId());
        assertEquals(mi.getNetValue(), new BigDecimal("11.98"));
    }
    
    /**
     * Test which will check if the correct id is returned when looking for the latest id.
     *
     * @throws SQLException
     */
    @Test
    public void findLastIdTest() throws SQLException {
        int id = miController.getMasterInvoiceId();
        log.log(Level.INFO, "Data>>>{0}", id);
        assertEquals(id, 4);
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
