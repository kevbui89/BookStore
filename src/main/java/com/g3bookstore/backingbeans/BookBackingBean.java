package com.g3bookstore.backingbeans;

import com.g3bookstore.customcontrollers.CustomBookJpaController;
import com.g3bookstore.customcontrollers.CustomClientJpaController;
import com.g3bookstore.entities.Book;
import com.g3bookstore.entities.Client;
import com.g3bookstore.entities.Genre;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * This class will take care of all the back end operations for the book page
 *
 * @author Kevin Bui
 */
@Named("BookBean")
@ManagedBean
@SessionScoped
public class BookBackingBean implements Serializable {

    @Inject
    private CustomClientJpaController clientController;

    @Inject
    private CustomBookJpaController bookController;

    @Inject
    private ClientCookieTracker cct;

    private static final Logger log = Logger.getLogger(BookBackingBean.class.getName());
    private Book book;

    /**
     * Returns the book object
     *
     * @return
     */
    public Book getBook() {
        return book;
    }

    /**
     * Sets the book object
     *
     * @param book
     */
    public void setBook(Book book) {
        this.book = book;
    }

    /**
     * Initializes sets the cookie
     */
    public void init() {
        if (getBooksInfo() == null) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            String outcome = "/WEB-INF/errors/404.xhtml";
            facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, outcome);
        } else {
            book = getBooksInfo();
            cct.saveGenreToDBAndCookie(book.getGenreList().get(0));
        }
    }

    /**
     * Returns a book object with all its information
     *
     * @return The book object
     */
    public Book getBooksInfo() {
        Map<String, String> parameters = FacesContext.getCurrentInstance().
                getExternalContext().getRequestParameterMap();

        log.log(Level.INFO, null, parameters.get("bookid"));

        // Check the query string
        try {
            String bookId = parameters.get("bookid");
            if (bookId == null || !bookId.matches("^[0-9]+]*$")) {
                return null;
            }

            int bookIdParsed = Integer.parseInt(bookId);
            if (validateBookId(bookIdParsed) == false) {
                return null;
            }

            return bookController.getBookByID(bookIdParsed);
        } catch (IllegalArgumentException ex) {
            log.log(Level.INFO, null, ex);
        }
        return null;
    }

    /**
     * Returns true if the book is on sale
     *
     * @return
     */
    public boolean checkBookOnSale() {
        Book b = getBooksInfo();

        if (b == null || b.getSalePrice() == null) {
            return false;
        }
        if (b.getSalePrice().intValue() > 0) {
            return true;
        }

        return false;
    }

    /**
     * Returns the similar books by genre.
     *
     * @return THe list of similar books
     */
    public List<Book> getSimilarBooksByGenre() {
        return bookController.getSimilarGenreBooks(bookController.getBookByID(book.getBookId()));
    }

    /**
     * Returns the similar books by author.
     *
     * @return THe list of similar books
     */
    public List<Book> getSimilarBooksByAuthor() {
        return bookController.getSimilarAuthorBooks(bookController.getBookByID(book.getBookId()));
    }

    /**
     * Checks if the list of authors is empty or not.
     *
     * @param b
     * @return
     */
    public boolean checkIfSimilarAuthorIsZero(Book b) {
        if (getSimilarBooksByAuthor() == null) {
            return false;
        }

        return (getSimilarBooksByAuthor().size() == 0) ? true : false;
    }

    /**
     * Returns a list of the most recent books added to the book store
     *
     * @return The list of books recently added to the bookstore
     */
    public List<Book> getMostRecentBooks(int size) {
        return (List<Book>) bookController.getMostRecentBooks(size);

    }

    /**
     * Returns all the books that are on sale.
     *
     * @return The list of books on sale
     */
    public List<Book> getBooksOnSale() {
        return (List<Book>) bookController.getBooksOnSale();
    }

    /**
     * Returns a specific amount of books on sale
     *
     * @param size The amount wanted
     * @return The list of books on sale
     */
    public List<Book> getBooksOnSale(int size) {
        return (List<Book>) bookController.getBooksOnSale(size);
    }

    /**
     * Returns a list of recommended books
     *
     * @param size
     * @return
     */
    public List<Book> getRecommendedBooks(int size, String email) {
        List<Client> c = clientController.getClientByEmail(email);
        List<Book> empty = new ArrayList<Book>();
        if (c.size() > 0) {
            cct.setClient(c.get(0));
            Genre g = cct.getLastGenreVisitedByClient();
            if (g != null) {
                return shuffleList(bookController.getBooksByGenre(g), size);
            }
        } 
        
        return empty;

    }

    /**
     * Returns a list of recommended books
     *
     * @param size
     * @return
     */
    public List<Book> getRecommendedBooksNotLogged(int size) {
        Genre g = cct.getLastGenreVisitedByClient();
        return shuffleList(bookController.getBooksByGenre(g), size);
    }

    /**
     * Shuffles a list of book with no duplicates and a specific size
     *
     * @param list The list of books
     * @return A list of recommended books with a specific size
     */
    private List<Book> shuffleList(List<Book> list, int size) {
        Collections.shuffle(list);

        Set<Book> bookSet = new HashSet<Book>();
        List<Book> noDuplicates = new ArrayList<Book>();
        List<Book> newList = new ArrayList<Book>();
        for (Book b : list) {
            noDuplicates.add(b);
        }

        bookSet.addAll(noDuplicates);
        noDuplicates.clear();
        noDuplicates.addAll(bookSet);

        if (noDuplicates.size() < size) {
            return noDuplicates;
        } else {
            for (int i = 0; i < size; i++) {
                newList.add(noDuplicates.get(i));
            }
        }

        return newList;
    }

    /**
     * Returns true if the id is a valid one.
     *
     * @param id The id of the book to be checked
     * @return true if the id is valid, else false
     */
    private boolean validateBookId(int id) {
        int size = bookController.getBooks().size();
        boolean valid = true;

        if (id < 1 || id > size) {
            valid = false;
        }

        return valid;
    }
}
