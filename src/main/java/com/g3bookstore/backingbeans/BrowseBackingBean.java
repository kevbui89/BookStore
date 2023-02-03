package com.g3bookstore.backingbeans;

import com.g3bookstore.customcontrollers.CustomAuthorJpaController;
import com.g3bookstore.customcontrollers.CustomBookJpaController;
import com.g3bookstore.customcontrollers.CustomGenreJpaController;
import com.g3bookstore.entities.Author;
import com.g3bookstore.entities.Book;
import com.g3bookstore.entities.Genre;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Class that manages the browsing page
 *
 * @author Kevin Bui
 */
@Named("BrowseGenres")
@SessionScoped
public class BrowseBackingBean implements Serializable {

    @Inject
    private CustomGenreJpaController genreController;

    @Inject
    private CustomBookJpaController bookController;

    @Inject
    private CustomAuthorJpaController authorController;

    private List<Genre> genres = new ArrayList<Genre>();

    private Logger log = Logger.getLogger(BrowseBackingBean.class.getName());

    /**
     * Returns a list of all the genres in the database
     *
     * @return
     */
    public List<Genre> getGenreList() {
        genres = genreController.getGenres();
        int size = genres.size();
        return genres;
    }

    /**
     * Returns a list of all the books from the genre
     *
     * @param g The genre
     * @return
     */
    public List<Book> getBooksByGenre(Genre g) {
        return bookController.getBooksByGenre(g);
    }

    /**
     * Returns all the books from the database
     *
     * @return
     */
    public List<Book> getAllBooks() {
        return bookController.getBooks();
    }

    /**
     * Returns all the authors for a book
     *
     * @return
     */
    public List<Author> getAuthorsForBooks(Book book) {
        return authorController.getAuthorsForBook(book);
    }

    /**
     * Returns a list of books depending on what the user chooses from the
     * browse_genre.xhtml page
     *
     * @return The list of books by genre selected
     */
    public List<Book> getAllBooksForGenre() {
        Map<String, String> parameters = FacesContext.getCurrentInstance().
                getExternalContext().getRequestParameterMap();

        log.log(Level.INFO, null, parameters.get("genre"));

        // Check the query string
        try {
            // Return default if invalid
            String genreId = parameters.get("genre");
            if (genreId == null || !genreId.matches("^[0-9]*$")) {
                return bookController.getBooksByGenre(genreController.getGenreByID(1));
            }

            // validate the genre id number
            int parsedId = Integer.parseInt(genreId);
            return bookController.getBooksByGenre(genreController.getGenreByID(parsedId));
        } catch (IllegalArgumentException ex) {
            log.log(Level.SEVERE, null, ex);
        }

        // Return default if nothing is found
        return bookController.getBooks();
    }

    /**
     * Returns all the top sellers from a genre
     * 
     * @return 
     */
    public List<Book> getTopSellers() {
        Map<String, String> parameters = FacesContext.getCurrentInstance().
                getExternalContext().getRequestParameterMap();

        log.log(Level.INFO, null, parameters.get("genre"));

        // Check the query string
        try {
            // Return default if invalid
            String genreId = parameters.get("genre");
            if (genreId == null || !genreId.matches("^[0-9]*$")) {
                return genreController.getTopSellersByGenre(genreController.getGenreByID(1));
            }

            // validate the genre id number
            int parsedId = Integer.parseInt(genreId);
            return genreController.getTopSellersByGenre(genreController.getGenreByID(parsedId));
        } catch (IllegalArgumentException ex) {
            log.log(Level.SEVERE, null, ex);
        }

        // Return default if nothing is found
        return genreController.getTopSellersByGenre(genreController.getGenreByID(1));
    }
    
    /**
     * Returns the rest of the books after top sellers
     * 
     * @return 
     */
    public List<Book> getOtherBooks() {
        Map<String, String> parameters = FacesContext.getCurrentInstance().
                getExternalContext().getRequestParameterMap();

        log.log(Level.INFO, null, parameters.get("genre"));

        // Check the query string
        try {
            // Return default if invalid
            String genreId = parameters.get("genre");
            if (genreId == null || !genreId.matches("^[0-9]*$")) {
                return genreController.getAllBooksAfterTopByGenre(genreController.getGenreByID(1));
            }

            // validate the genre id number
            int parsedId = Integer.parseInt(genreId);
            return genreController.getAllBooksAfterTopByGenre(genreController.getGenreByID(parsedId));
        } catch (IllegalArgumentException ex) {
            log.log(Level.SEVERE, null, ex);
        }

        // Return default if nothing is found
        return genreController.getAllBooksAfterTopByGenre(genreController.getGenreByID(1));
    }
    
    /**
     * Returns the genre chosen
     * 
     * @return The genre object
     */
    public String getGenre() {
        Map<String, String> parameters = FacesContext.getCurrentInstance().
                getExternalContext().getRequestParameterMap();

        log.log(Level.INFO, null, parameters.get("genre"));

        // Check the query string
        try {
            // Return default if invalid
            String genreId = parameters.get("genre");
            if (genreId == null || !genreId.matches("^[0-9]*$")) {
                return null;
            }

            // validate the genre id number
            int parsedId = Integer.parseInt(genreId);
            return genreController.getGenreByID(parsedId).getGenre();
        } catch (IllegalArgumentException ex) {
            log.log(Level.SEVERE, null, ex);
        }

        // Return default if nothing is found
        return null;
    }

}
