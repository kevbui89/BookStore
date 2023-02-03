package com.g3bookstore.databasetests;

import com.g3bookstore.backingbeans.LocaleChanger;
import com.g3bookstore.controllers.BookJpaController;
import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import com.g3bookstore.converters.ProvinceConverter;
import com.g3bookstore.customcontrollers.CustomBookJpaController;
import com.g3bookstore.customcontrollers.CustomDetailInvoiceJpaController;
import com.g3bookstore.customcontrollers.CustomMasterInvoiceJpaController;
import com.g3bookstore.entities.Book;
import com.g3bookstore.entities.DetailInvoice;
import com.g3bookstore.entities.MasterInvoice;
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
 * Test class for the MasterInvoice Controller
 *
 * @author Kevin Bui
 */
@Ignore
@RunWith(Arquillian.class)
public class DetailInvoiceControllerDBTest {

    @Inject
    private CustomMasterInvoiceJpaController miController;

    @Inject
    private CustomDetailInvoiceJpaController diController;

    @Resource(name = "java:app/jdbc/bookstore")
    private DataSource ds;

    private Logger log = Logger.getLogger(DetailInvoiceControllerDBTest.class.getName());

    public DetailInvoiceControllerDBTest() {
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
    public void findDetailInvoiceFromMasterInvoice() throws SQLException {
        MasterInvoice mi = miController.getMasterInvoiceByID(1);
        List<DetailInvoice> di = diController.getDetailedInvoiceFromMasterInvoice(mi);
        log.log(Level.INFO, "DetailInvoice Size>>>{0}", di.size());
        assertThat(di).hasSize(2);
    }

    /**
     * Test which will check if the correct detail invoice is returned when
     * searching by primary key.
     *
     * @throws SQLException
     */
    @Test
    public void findDetailInvoiceByPrimaryKeyTest() throws SQLException {
        MasterInvoice mi = miController.getMasterInvoiceByID(1);
        List<DetailInvoice> di = diController.getDetailedInvoiceFromMasterInvoice(mi);
        log.log(Level.INFO, "DetailInvoice book title>>>{0}", di.get(0).getBookId().getTitle());
        assertEquals(di.get(0).getBookId().getTitle(), "Stranger in a Strange Land");
    }

    /**
     * Test which whill check if the correct detail invoices are returned when
     * searched from a master invoice id.
     *
     * @throws SQLException
     */
    @Test
    public void findDetailInvoiceFromMasterInvoiceById() throws SQLException {
        List<DetailInvoice> di = diController.getDetailedInvoiceFromMasterInvoiceById(1);
        log.log(Level.INFO, "DetailInvoice Size>>>{0}", di.size());
        assertThat(di).hasSize(2);
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
                .addPackage(GenreControllerDBTest.class.getPackage())
                .addPackage(ReviewControllerDBTest.class.getPackage())
                .addPackage(AdControllerDBTest.class.getPackage())
                .addPackage(FeedMessage.class.getPackage())
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
