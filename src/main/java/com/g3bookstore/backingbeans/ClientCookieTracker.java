package com.g3bookstore.backingbeans;

import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import com.g3bookstore.customcontrollers.CustomClientJpaController;
import com.g3bookstore.customcontrollers.CustomGenreJpaController;
import com.g3bookstore.entities.Client;
import com.g3bookstore.entities.Genre;
import java.io.Serializable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Cookie;

/**
 * This class will take care of the cookie management
 *
 * @author Kevin Bui
 */
@Named("ClientTracker")
@SessionScoped
public class ClientCookieTracker implements Serializable {

    @Inject
    private CustomClientJpaController clientController;

    @Inject
    private CustomGenreJpaController genreController;

    private Client client;
    final private String GENRE_ID = "genre_id";
    final private String COOKIE_SETTING = "cookie_setting";

    private static final Logger log = Logger.getLogger(ClientCookieTracker.class.getName());

    /**
     * Client created if it does not exist.
     *
     * @return the client.
     */
    public Client getClient() {
        if (client == null) {
            client = new Client();
        }
        return client;
    }

    /**
     * Method responsible for setting the client.
     *
     * @param client
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * Returns the last genre visited by a client/visitor
     *
     * @return
     */
    public Genre getLastGenreVisitedByClient() {
        if (client != null) {
            Client c = clientController.getClientByID(client.getClientId());
            return c.getLastGenreVisited();
        } else {
            return getGenreFromCookie();
        }
    }

    /**
     * Saves the last genre visited by the client to the database and the cookie
     *
     * @param g
     */
    public void saveGenreToDBAndCookie(Genre g) {
        if (client != null) {
            try {
                saveVisitedGenreToDatabase(client, g);
            } catch (Exception ex) {
                log.log(Level.SEVERE, "Error saving to the database: {0}", ex.getMessage());
            }
        }
        saveVisitedGenreToCookie(g);
    }

    /**
     * Checks the settings of the current browser
     *
     * @return true if the browser supports cookies, else false
     */
    public boolean checkCookieSettings() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().addResponseCookie(COOKIE_SETTING, "G3CookieTest", null);
        Map<String, Object> cookies = FacesContext.getCurrentInstance().getExternalContext()
                .getRequestCookieMap();

        if (cookies == null) {
            return false;
        }

        if (cookies.containsKey(COOKIE_SETTING)) {
            return true;
        }

        return false;
    }

    /**
     * Validates if a cookie with the genre exists
     *
     * @return
     */
    public boolean isGenreSaved() {
        if (client != null) {
            return validateGenreDatabase();
        } else {
            return validateGenreCookie();
        }
    }

    /**
     * Saves a genre to the clients data
     *
     * @param g The genre
     */
    public void saveVisitedGenreToDatabase(Client c, Genre g) throws RollbackFailureException, Exception {
        c.setLastGenreVisited(g);
        clientController.update(c);
    }

    /**
     * Saves a visited genre to the cookie
     *
     * @param g The genre
     */
    private void saveVisitedGenreToCookie(Genre g) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().addResponseCookie(GENRE_ID, g.getGenreId().intValue() + "", null);
        Map<String, Object> cookies = FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap();
        log.info("Saved cookie: " + cookies.containsKey(GENRE_ID));
    }

    /**
     * Returns the genre from the cookie
     *
     * @return
     */
    private Genre getGenreFromCookie() {
        Map<String, Object> cookies = getCookies();

        Cookie cookie = (Cookie) cookies.get(GENRE_ID);
        if (cookie != null) {
            int genre_id = Integer.parseInt(cookie.getValue());

            // Check if valid genre id
            if (genre_id > 0) {
                Genre genre = genreController.getGenreByID(genre_id);
                return genre;
            }
        }

        return null;
    }

    /**
     * Returns the cookies
     *
     * @return
     */
    private Map<String, Object> getCookies() {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap();
    }

    /**
     * Validates the genre has is present in the cookie
     *
     * @return true if the cookie exists, else false
     */
    public boolean validateGenreCookie() {
        Map<String, Object> cookies = getCookies();
        return cookies.containsKey(GENRE_ID);
    }

    /**
     * Validates that the user has a last genre visited
     *
     * @return true if the user has a last genre, else false
     */
    public boolean validateGenreDatabase() {
        return client.getLastGenreVisited() != null;
    }

}
