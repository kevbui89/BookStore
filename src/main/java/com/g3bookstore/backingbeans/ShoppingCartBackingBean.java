package com.g3bookstore.backingbeans;

import com.g3bookstore.customcontrollers.CustomBookJpaController;
import com.g3bookstore.customcontrollers.CustomClientJpaController;
import com.g3bookstore.customcontrollers.CustomMasterInvoiceJpaController;
import com.g3bookstore.entities.Book;
import com.g3bookstore.entities.Client;
import com.g3bookstore.entities.DetailInvoice;
import com.g3bookstore.entities.MasterInvoice;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Responsible for backing shopping cart and displaying results.
 *
 * @author Alessandro Ciotola, Kevin Bui
 * @version 06/03/2018
 * @since 1.8
 */
@Named("cartBean")
@ManagedBean
@SessionScoped
public class ShoppingCartBackingBean implements Serializable {

    private Logger log = Logger.getLogger(ShoppingCartBackingBean.class.getName());

    @Inject
    private CustomClientJpaController clientController;

    @Inject
    private CustomMasterInvoiceJpaController masterController;

    private List<Book> bookList = new ArrayList<>();
    private String errorMessage;
    private boolean alreadyInCart;
    
    /**
     * Returns a boolean whether the book is already in the cart or not
     * 
     * @return 
     */
    public boolean getAlreadyInCart() {
        return alreadyInCart;
    }
    
    /**
     * Sets the already in cart boolean value
     * 
     * @param aic 
     */
    public void setAlreadyInCart(boolean aic) {
        this.alreadyInCart = aic;
    }

    /**
     * Returns the size of the booklist.
     *
     * @return
     */
    public int totalItems() {
        return bookList.size();
    }

    /**
     * When an item is added to the cart, add the id to the cart id list.
     *
     * @param clientId
     * @param b
     * @throws java.io.IOException
     */
    public void addBookToCart(Book b, int clientId) throws IOException {
        List<Book> purchased = getPurchasedBooks(clientId);
        
        if (!bookList.contains(b) && !purchased.contains(b)) {
            bookList.add(b);
        } else  {
            alreadyInCart = true;
        }
        
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.redirect(context.getRequestContextPath() + "/client/confirmed_cart.xhtml");
        } catch (IOException ex) {
            log.log(Level.WARNING, "Error redirecting: {0}", ex.getMessage());
        }
    }

    /**
     * Checks if the book has already been purchased
     * 
     * @param b
     * @param clientId
     * @return 
     */
    public boolean checkIfPurchased(Book b, int clientId) {
        List<Book> purchased = getPurchasedBooks(clientId);
        if (purchased.contains(b)) {
            return true;
        }
        return false;
    }

    /**
     * Redirects to the library
     */
    public void redirectToLibrary() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.redirect(context.getRequestContextPath() + "/client/library.xhtml");
        } catch (IOException io) {
            log.log(Level.WARNING, "Error redirecting: {0}", io.getMessage());
        }
    }

    /**
     * Method responsible for returning the list of books the customer wishes to
     * purchase.
     *
     * @return the book list.
     */
    public List<Book> getBookList() {
        Set<Book> bookSet = new HashSet<>();

        bookSet.addAll(bookList);
        bookList.clear();
        bookList.addAll(bookSet);

        return bookList;
    }

    /**
     * Method responsible for removing a book from the shopping cart when the
     * "x" is clicked on.
     *
     * @param book
     */
    public void removeBookFromCart(Book book) {
        for (int i = 0; i < bookList.size(); i++) {
            if (bookList.get(i).getTitle().equals(book.getTitle())) {
                bookList.remove(i);
            }
        }
    }

    /**
     * Method responsible for getting the total price of books before the tax.
     *
     * @return
     */
    public double getPriceNoTax() {
        double total = 0;
        for (int i = 0; i < bookList.size(); i++) {
            if (bookList.get(i).getSalePrice().doubleValue() > 0) {
                total += bookList.get(i).getSalePrice().doubleValue();
            } else {
                total += bookList.get(i).getListPrice().doubleValue();
            }
        }
        return Math.round(total * 100.0) / 100.0;
    }

    /**
     * Method responsible for getting the total price of all books in the cart
     * without the tax.
     *
     * @return total price
     */
    public double getPrice() {
        double total = getPriceNoTax();
        return Math.round(total * 100.0) / 100.0;
    }

    /**
     * Formats the double in the correct format
     *
     * @param d
     * @return
     */
    public String formatDouble(double d) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(d);
    }

    /**
     * Returns true if the list is empty
     *
     * @return true if the list is empty, else false
     */
    public boolean isEmpty() {
        return bookList.isEmpty();
    }

    /**
     * Returns the error message
     *
     * @return
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets the error message
     *
     * @param em
     */
    public void setErrorMessage(String em) {
        this.errorMessage = em;
    }

    /**
     * Emtpies the whole cart
     */
    public void removeAll() {
        bookList = new ArrayList<>();
    }

    /**
     * Checks the list of purchased book and renders the page accordingly.
     *
     * @return The list of book purchased from the client
     */
    private List<Book> getPurchasedBooks(int clientId) {
        Client c = clientController.getClientByID(clientId);
        List<MasterInvoice> masterInvoices = masterController.getMasterInvoiceForClient(c);
        List<DetailInvoice> detailInvoices = new ArrayList<DetailInvoice>();
        List<Book> books = new ArrayList<Book>();

        for (MasterInvoice mi : masterInvoices) {
            List<DetailInvoice> detailInvoiceForMasterInvoice = mi.getDetailInvoiceList();
            for (int i = 0; i < detailInvoiceForMasterInvoice.size(); i++) {
                detailInvoices.add(detailInvoiceForMasterInvoice.get(i));
            }
        }

        for (DetailInvoice di : detailInvoices) {
            books.add(di.getBookId());
        }

        return books;
    }
}
