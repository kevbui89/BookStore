/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.g3bookstore.backingbeans;

import com.g3bookstore.customcontrollers.CustomBookJpaController;
import com.g3bookstore.customcontrollers.CustomClientJpaController;
import com.g3bookstore.entities.Book;
import java.io.Serializable;
import com.g3bookstore.entities.Client;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.RowEditEvent;

/**
 * Class intended to provide both information and perform updates on data for
 * the sales management page.
 *
 * @author Werner
 */
@Named("SalesManagement")
@SessionScoped
public class SalesBackingBean implements Serializable {

    @Inject
    private CustomBookJpaController bookController;

    private List<Book> sales;
    private Logger log = Logger.getLogger(BrowseBackingBean.class.getName());

    /**
     * Helper method that returns all the books within the db.
     *
     * @return
     */
    private List<Book> callBooksDB() {
        return bookController.getBooks();
    }

    /**
     * Return all the books
     *
     * @return
     */
    public List<Book> getBooks() {
        if (this.sales == null) {
            this.sales = callBooksDB();
        }
        return this.sales;
    }

    /**
     * Edits the book object of the item that raised the event
     *
     * @param event
     * @throws Exception
     */
    public void onRowEdit(RowEditEvent event) throws Exception {
        Book updatedBook = (Book) event.getObject();
        log.log(Level.INFO, null, "book object: " + updatedBook);
        bookController.update(updatedBook);
    }

    public void onRowCancel(RowEditEvent event) {
        log.log(Level.INFO, null, "cancled update");
    }

}
