package com.g3bookstore.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 * This class seeds the database
 * 
 * @author Kevin
 */
public class Seeder {
    
    private DataSource ds;
    private Logger log = Logger.getLogger(Seeder.class.getName());
    
    public Seeder(DataSource ds) 
    {
        this.ds = ds;
    }
    
    /**
     * This routine is courtesy of Bartosz Majsak who also solved my Arquillian
     * remote server problem
     */
    public void seedDatabase() 
    {
        final String seedDataScript = loadAsString("CreateBookStoreTables.sql");

        try (Connection connection = ds.getConnection()) {
            for (String statement : splitStatements(new StringReader(
                    seedDataScript), ";")) {
                connection.prepareStatement(statement).execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed seeding database", e);
        }
        log.log(Level.INFO, "Successful seeding.");
    }
    
    /**
     * Loads database creation file and returns it as a String.
     * @param path      The path to the database file.
     * @return          Database file contents as a String.
     */
    private String loadAsString(final String path) {
        try (InputStream inputStream = Thread.currentThread()
                .getContextClassLoader().getResourceAsStream(path)) {
            return new Scanner(inputStream).useDelimiter("\\A").next();
        } 
        catch (IOException e) {
            throw new RuntimeException("Unable to close input stream.", e);
        }
        
    }

    /**
     * Splits database file into SQL statements
     * @param reader                Reader object.
     * @param statementDelimiter    The delimiter
     * @return                      list of sql statements.
     */
    private List<String> splitStatements(Reader reader, String statementDelimiter) {
        final BufferedReader bufferedReader = new BufferedReader(reader);
        final StringBuilder sqlStatement = new StringBuilder();
        final List<String> statements = new LinkedList<>();
        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || isComment(line)) {
                    continue;
                }
                sqlStatement.append(line);
                if (line.endsWith(statementDelimiter)) {
                    statements.add(sqlStatement.toString());
                    sqlStatement.setLength(0);
                }
            }
            return statements;
        } 
        catch (IOException e) {
            throw new RuntimeException("Failed parsing sql", e);
        }
    }

    /**
     * Checks if given string is a comment.
     * @param line      The line in the database
     * @return          true if this line is a comment, else false
     */
    private boolean isComment(final String line) {
        return line.startsWith("--") || line.startsWith("//") || line.startsWith("/*");
    }
    
}
