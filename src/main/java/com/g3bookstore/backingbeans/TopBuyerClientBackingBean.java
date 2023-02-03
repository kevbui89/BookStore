package com.g3bookstore.backingbeans;

import com.g3bookstore.customcontrollers.CustomManagerController;
import com.g3bookstore.entities.Client;
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
 * client that used most of his/her money with two
 * dates.
 * 
 * @author Denis Lebedev
 */
@Named("topBuyerClient")
@SessionScoped
public class TopBuyerClientBackingBean implements Serializable{
    
    private final Logger log = Logger.getLogger(TopBuyerClientBackingBean.class.getName());
    
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
     * Allow to obtain the result of the query for the
     * client top buyer.
     * 
     * @return a list of Clients
     */
    public List<Client> getData() {
        
        List<Client> result = manController.clientTopSellerOrderByPurchases(startDate, endDate);
        
        log.log(Level.INFO, "Accessing data with the size of:{0}", result.size());
        
        return result;
    }
    
}
