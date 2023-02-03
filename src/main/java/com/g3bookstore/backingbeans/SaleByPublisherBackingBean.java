package com.g3bookstore.backingbeans;

import com.g3bookstore.customcontrollers.CustomManagerController;
import com.g3bookstore.customcontrollers.CustomPublisherJpaController;
import com.g3bookstore.entities.Book;
import com.g3bookstore.entities.Publisher;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.FlowEvent;

/**
 * This class allow to query the result all the books sold by a publisher
 * between two dates.
 *
 * @author Denis Lebedev
 */
@Named("saleByPublisher")
@SessionScoped
public class SaleByPublisherBackingBean implements Serializable {

    private final Logger log = Logger.getLogger(SaleByPublisherBackingBean.class.getName());

    @Inject
    private CustomManagerController manController;

    @Inject
    private CustomPublisherJpaController pubController;

    private Publisher pub;
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

    public Publisher getPublisher() {
        return pub;
    }

    public void setPublisher(Publisher pub) {
        this.pub = pub;
    }
    
    /**
     * Allow to get the total sale of a specific publisher.
     *
     * @return
     */
    public BigDecimal getTotalSale() {
        BigDecimal result = manController.totalMoneyByPublisher(pub, startDate, endDate);
        log.log(Level.INFO, "Total money made:{0}", result);
        if(result == null)
            result = new BigDecimal(0);
        return result;
    }
    
    /**
     * Allow to obtain the result of the query for the all the books sold by a
     * publisher.
     *
     * @return list of Books
     */
    public List<Book> getData() {
        //The if statement ensure that the author given by the converter exist and grab it.
        if (pub != null && pub.getPublisherId() != null) {
            log.log(Level.INFO, "New Author object with the id:{0}", pub.getPublisherId());
            pub = pubController.getPublisherByID(pub.getPublisherId());
        }

        List<Book> result = manController.allBookSoldByPublisherOrderedByDate(pub, startDate, endDate);
        log.log(Level.INFO, "Accessing data with the size of:{0}", result.size());
        return result;
    }

    public String onFlowProcess(FlowEvent event) {

        if (event.getOldStep().equals("infoTab")) {
            //The if statement ensure that the author given by the converter exist and grab it.
            if (pub != null && pub.getPublisherId() != null) {
                log.log(Level.INFO, "New Author object with the id:{0}", pub.getPublisherId());
                pub = pubController.getPublisherByID(pub.getPublisherId());
            }
        }

        return event.getNewStep();
    }
}
