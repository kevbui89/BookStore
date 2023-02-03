package com.g3bookstore.backingbeans;

import com.g3bookstore.customcontrollers.CustomAuthorJpaController;
import com.g3bookstore.customcontrollers.CustomManagerController;
import com.g3bookstore.entities.Author;
import com.g3bookstore.entities.Book;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.FlowEvent;

/**
 * This class allow to query the result of the sales of an author between two
 * dates.
 *
 * @author Denis Lebedev
 */
@Named("saleByAuthor")
@SessionScoped
public class SaleByAuthorBackingBean implements Serializable {

    private final Logger log = Logger.getLogger(SaleByAuthorBackingBean.class.getName());

    @Inject
    private CustomManagerController manController;

    @Inject
    private CustomAuthorJpaController authController;

    private Author author;
    private Date startDate;
    private Date endDate;
    private Boolean displayTotalSale;

    public Boolean getDisplayTotalSale() {
        return displayTotalSale;
    }

    public void setDisplayTotalSale(Boolean displayTotalSale) {
        this.displayTotalSale = displayTotalSale;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    /**
     * Allow to get the total sale of a specific author.
     *
     * @return
     */
    public BigDecimal getTotalSale() {
        BigDecimal result = manController.totalMoneyByAuthor(author, startDate, endDate);
        log.log(Level.INFO, "Total money made:{0}", result);
        if(result == null)
            result = new BigDecimal(0);
        return result;
    }

    /**
     * Allow to obtain the result of the query for the all the books sold by an
     * author.
     *
     * @return list of Books
     */
    public List<Book> getData() {
        List<Book> result = manController.allBookSoldByAuthorOrderedByDate(author, startDate, endDate);
        log.log(Level.INFO, "Accessing data with the size of:{0}", result.size());
        return result;
    }

    public String onFlowProcess(FlowEvent event) {

        if (event.getOldStep().equals("infoTab")) {
            //The if statement ensure that the author given by the converter exist and grab it.
            if (author != null && author.getAuthorId() != null) {

                log.log(Level.INFO, "New Author object with the id:{0}", author.getAuthorId());
                author = authController.getAuthorByID(author.getAuthorId());
            }
        }

        return event.getNewStep();
    }
}
