package com.g3bookstore.backingbeans;

import com.g3bookstore.customcontrollers.CustomClientJpaController;
import com.g3bookstore.customcontrollers.CustomManagerController;
import com.g3bookstore.entities.Book;
import com.g3bookstore.entities.Client;
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
 * This class allow to query the result the sales of a client between two dates.
 *
 * @author Denis Lebedev
 */
@Named("saleByClient")
@SessionScoped
public class SaleByClientBakcingBean implements Serializable {

    private final Logger log = Logger.getLogger(SaleByClientBakcingBean.class.getName());

    @Inject
    private CustomManagerController manController;

    @Inject
    private CustomClientJpaController clientController;

    private Client client;
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * Allow to get the total sale of a specific client.
     *
     * @return
     */
    public BigDecimal getTotalSale() {
        BigDecimal result = manController.totalMoneyByClient(client, startDate, endDate);
        log.log(Level.INFO, "Total money made:{0}", result);
        if (result == null) {
            result = new BigDecimal(0);
        }
        return result;
    }

    /**
     * Allow to obtain the result of the query for the all the books bought by a
     * Client.
     *
     * @return list of Books
     */
    public List<Book> getData() {
        List<Book> result = manController.allBookBoughtByClientOrderedByDate(client, startDate, endDate);
        log.log(Level.INFO, "Accessing data with the size of:{0}", result.size());
        return result;
    }

    public String onFlowProcess(FlowEvent event) {

        if (event.getOldStep().equals("infoTab")) {
            //The if statement ensure that the client given by the converter exist and grab it.
            if (client != null && client.getClientId() != null) {

                log.log(Level.INFO, "New Author object with the id:{0}", client.getClientId());
                client = clientController.getClientByID(client.getClientId());
            }
        }

        return event.getNewStep();
    }
}
