package com.g3bookstore.backingbeans;

import com.g3bookstore.customcontrollers.CustomBookJpaController;
import com.g3bookstore.customcontrollers.CustomManagerController;
import com.g3bookstore.entities.Book;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Class responsible for providing the data for displaying it in the stock
 * reports page
 *
 * @author Werner
 */
@Named("stockBacking")
@RequestScoped
public class StockReportBackingBean {

    @Inject
    private CustomBookJpaController bookController;
    @Inject
    private CustomManagerController managerController;
    private List<Book> books;

    /**
     * Returns all the books within the database.
     *
     * @return
     */
    public List<Book> getAllBooks() {
        books = bookController.getBooks();
        return books;
    }

    /**
     * Calculates and returns the number of units sold by the given book object
     *
     * @param book
     * @return
     */
    public String salesFiguers(Book book) {
        return managerController.totalSoldByBook(book).get(0) + "";
    }
}
