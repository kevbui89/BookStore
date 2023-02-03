package com.g3bookstore.backingbeans;

import com.g3bookstore.customcontrollers.CustomManagerController;
import com.g3bookstore.entities.Book;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * This class allow to query the result of the
 * books that has been sold the most between two
 * dates.
 * 
 * @author Denis Lebedev
 */
@Named("topSellerBook")
@SessionScoped
public class TopSellerBookBackingBean implements Serializable{
    
    private final Logger log = Logger.getLogger(TopSellerBookBackingBean.class.getName());
    
    @Inject
    private CustomManagerController manController;
    
    
    private Date startDate;
    private Date endDate;
    
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
        
    /**
     * Allow to obtain the result of the query for books top seller.
     * 
     * @return a list of Books 
     */
    public List<Book> getData() {
        List<Book> result = manController.bookTopSellerOrderBySales(startDate, endDate);
        log.log(Level.INFO, "Accessing data with the size of:{0}", result.size());
        return result;
    }
    
}
