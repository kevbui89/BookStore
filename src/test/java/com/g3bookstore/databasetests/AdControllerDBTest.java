package com.g3bookstore.databasetests;

import com.g3bookstore.backingbeans.LocaleChanger;
import com.g3bookstore.controllers.BookJpaController;
import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import com.g3bookstore.converters.ProvinceConverter;
import com.g3bookstore.customcontrollers.CustomAdJpaController;
import com.g3bookstore.customcontrollers.CustomBookJpaController;
import com.g3bookstore.entities.Ad;
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
 * Test class for the Ad Controller
 *
 * @author Alessandro, Kevin
 */
@Ignore
@RunWith(Arquillian.class)
public class AdControllerDBTest {

    @Inject
    private CustomAdJpaController customAdController;

    @Resource(name = "java:app/jdbc/bookstore")
    private DataSource ds;

    private Logger log = Logger.getLogger(AdControllerDBTest.class.getName());

    public AdControllerDBTest() {
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
     * Test which will check if the correct ad is returned by searching by id.
     *
     * @throws SQLException
     */
    @Test
    public void findAdByIdTest() throws SQLException {
        Ad ad = customAdController.getAdByID(1);
        log.log(Level.INFO, "Data>>>{0}", ad.getTitle());
        assertEquals(ad.getTitle(), "Honda");
    }

    /**
     * Test which will check if the correct ads are returned when searching for
     * an ad by title.
     *
     * @throws SQLException
     */
    @Test
    public void findAdsByTitleTest() throws SQLException {
        Ad ad = customAdController.getAdByTitle("Honda");
        log.log(Level.INFO, "Data>>>{0}", ad.getTitle());
        assertEquals(ad.getTitle(), "Honda");
    }

    /**
     * Test that will check that all the ads are retrieved from the database
     *
     * @throws SQLException
     */
    @Test
    public void findAllAdsTest() throws SQLException {
        List<Ad> ads = customAdController.getAds();
        assertThat(ads).hasSize(6);
    }

    @Test
    public void findAllChosenAds() throws SQLException {
        List<Ad> ads = customAdController.getChosenAd();
        assertThat(ads).hasSize(2);
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
