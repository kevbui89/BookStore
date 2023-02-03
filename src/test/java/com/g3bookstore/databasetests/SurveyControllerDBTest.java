package com.g3bookstore.databasetests;

import com.g3bookstore.backingbeans.LocaleChanger;
import com.g3bookstore.controllers.BookJpaController;
import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import com.g3bookstore.converters.ProvinceConverter;
import com.g3bookstore.customcontrollers.CustomBookJpaController;
import com.g3bookstore.customcontrollers.CustomSurveyJpaController;
import com.g3bookstore.entities.Book;
import com.g3bookstore.entities.Response;
import com.g3bookstore.entities.Survey;
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
 * Test class for the Survey Controller
 * 
 * @author Alessandro Ciotola
 */
@Ignore
@RunWith(Arquillian.class)
public class SurveyControllerDBTest {
    
    @Inject
    private CustomSurveyJpaController customSurveyController;
    
    @Resource(name = "java:app/jdbc/bookstore")
    private DataSource ds;
    
    private Logger log = Logger.getLogger(SurveyControllerDBTest.class.getName());
    
    public SurveyControllerDBTest() {
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
     * Test which will check if the correct survey is returned by searching by id.
     * 
     * @throws SQLException
     */
    @Test
    public void findSurveyByIdTest() throws SQLException
    {
        Survey survey = customSurveyController.getSurveyByID(1);
        log.log(Level.INFO, "Data>>>{0}", survey.getQuestion());
        assertEquals(survey.getQuestion(), "What book genre is your favorite?");
    }
    
    /**
     * Test which will check if the correct survey is returned when searching 
     * for a survey by question.
     * 
     * @throws SQLException
     */
    @Test
    public void findSurveyByQuestionTest() throws SQLException
    {
        Survey survey = customSurveyController.getSurveyByQuestion("What book genre is your favorite?");
        log.log(Level.INFO, "Data>>>{0}", survey.getQuestion());
        assertEquals(survey.getQuestion(), "What book genre is your favorite?");
    }
    
    /**
     * Test which will check if the correct responses are returned when searching for
     * responses by a specific question.
     * 
     * @throws SQLException
     */
    @Test
    public void findResponsesByQuestionTest() throws SQLException
    {
        List<Response> responseList = customSurveyController.getResponsesBySurvey(1);
        log.log(Level.INFO, "Data>>>{0}", responseList.get(0).getAnswer());
        assertThat(responseList).hasSize(4);
    }
    
    /**
     * Test which will check if the correct survey is returned when searching 
     * for a survey by looking for the current question.
     * 
     * @throws SQLException
     */
    @Test
    public void findSurveyByCurrentQuestionTest() throws SQLException
    {
        Survey survey = customSurveyController.getCurrentQuestion();
        log.log(Level.INFO, "Data>>>{0}", survey.getQuestion());
        assertEquals(survey.getQuestion(), "What book genre is your favorite?");
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
                .addPackage(SurveyControllerDBTest.class.getPackage())
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
