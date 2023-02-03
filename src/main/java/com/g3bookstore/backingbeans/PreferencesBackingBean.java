package com.g3bookstore.backingbeans;

import com.g3bookstore.customcontrollers.CustomBookJpaController;
import com.g3bookstore.customcontrollers.CustomClientJpaController;
import com.g3bookstore.customcontrollers.CustomGenreJpaController;
import com.g3bookstore.entities.Book;
import com.g3bookstore.entities.Client;
import com.g3bookstore.entities.Genre;
import com.g3bookstore.util.MessageProvider;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * This bean will take care of the preferences from the user input
 *
 * @author Kevin
 */
@Named("Preferences")
@SessionScoped
public class PreferencesBackingBean implements Serializable {

    @Inject
    private CustomGenreJpaController genreController;

    @Inject
    private CustomBookJpaController bookController;

    @Inject
    private CustomClientJpaController clientController;

    @Inject
    private ClientCookieTracker cct;

    private List<Genre> genres = new ArrayList<Genre>();
    private List<Book> books = new ArrayList<Book>();
    private Book book;
    private Logger log = Logger.getLogger(PreferencesBackingBean.class.getName());

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
     * @param bookId
     */
    public void setBook(Book clickedBook) {
        this.book = bookController.getBookByID(clickedBook.getBookId());
        log.log(Level.INFO, "BOOK PREFERENCES: {0}", book.getTitle());
    }

    /**
     * Returns a list of all genres
     *
     * @return
     */
    public List<Genre> getAllGenres() {
        genres = genreController.getGenres();
        return genres;
    }

    /**
     * Returns a list of books per genre
     *
     * @param g
     * @return
     */
    public List<Book> getBooksPerGenre(Genre g) {
        return genreController.getAllBooksByGenre(g);
    }

    /**
     * Returns the top sellers of a genre
     *
     * @param g
     * @return
     */
    public List<Book> getTopBooksPerGenre(Genre g) {
        return genreController.getTopSellersByGenre(g);
    }

    /**
     * Returns a list of single book per genre
     *
     * @return
     */
    public List<Book> getBookPerGenre() {
        List<Genre> genreList = getAllGenres();
        Set<Book> bookSet = new HashSet<Book>();
        List<Book> bookList = new ArrayList<>();

        for (int i = 0; i < genreList.size(); i++) {
            List<Book> list = getTopBooksPerGenre(genreList.get(i));
            bookList.add(list.get(0));
        }

        bookSet.addAll(bookList);
        bookList.clear();
        bookList.addAll(bookSet);

        List<Book> listBooks = new ArrayList<Book>();
        for (int i = 0; i < 8; i++) {
            listBooks.add(bookList.get(i));
        }

        return listBooks;
    }

    /**
     * Returns a single book randomly per genre
     *
     * @param genreBooks
     * @return
     */
    public Book getRandomBook(List<Book> genreBooks) {
        Collections.shuffle(genreBooks);
        return genreBooks.get(0);
    }

    public boolean isBookNull() {
        return book == null;
    }

    public void submitPreferences(int clientId) throws Exception {
        if (book != null) {
            Client c = clientController.getClientByID(clientId);
            cct.saveVisitedGenreToDatabase(c, book.getGenreList().get(0));
            log.log(Level.INFO, "BOOK PREFERENCES: {0}", book.getTitle());
            book = null;
            try {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.redirect(context.getRequestContextPath() + "/client/index.xhtml");
            } catch (IOException io) {
                log.log(Level.WARNING, "Error redirecting when registering: {0}", io.getMessage());
            }
        } else {
            FacesMessage message = new FacesMessage(new MessageProvider().getValue("mustselect"));
            FacesContext.getCurrentInstance().addMessage("prefForm", message);
        }
    }
}
