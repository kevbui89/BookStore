package com.g3bookstore.backingbeans;

import com.g3bookstore.customcontrollers.CustomClientJpaController;
import com.g3bookstore.customcontrollers.CustomDetailInvoiceJpaController;
import com.g3bookstore.customcontrollers.CustomMasterInvoiceJpaController;
import com.g3bookstore.entities.Client;
import com.g3bookstore.entities.DetailInvoice;
import com.g3bookstore.entities.MasterInvoice;
import com.g3bookstore.util.EmailSender;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Cookie;

/**
 *
 * @author Kevin Bui
 */
@Named("MyOrdersBean")
@SessionScoped
public class OrdersBackingBean implements Serializable {

    @Inject
    private CustomMasterInvoiceJpaController masterController;

    @Inject
    private CustomDetailInvoiceJpaController detailController;

    @Inject
    private CustomClientJpaController clientController;

    private int invoiceId;
    private Client client;
    private Logger log = Logger.getLogger(OrdersBackingBean.class.getName());
    private EmailSender emailSender;

    /**
     * Client created if it does not exist.
     *
     * @return the client.
     */
    public Client getClient() {
        if (client == null) {
            client = new Client();
        }
        return client;
    }

    /**
     * Method responsible for setting the client.
     *
     * @param client
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * When the page is loaded, search for the currently logged in user to
     * display their information.
     *
     */
    @PostConstruct
    public void init() {
        if (client == null) {
            client = getClient();
        }
        Cookie cookie = (Cookie) FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap().get("G3Cookie");
        client = clientController.getClientByEmail(cookie.getValue()).get(0);
    }

    /**
     * Returns a list of master invoices for the client
     *
     * @return
     */
    public List<MasterInvoice> getAllMasterInvoiceOfClient(Client c) {
        return (List<MasterInvoice>) masterController.getMasterInvoiceForClient(c);
    }

    /**
     * Returns the list of all detail invoice for a client
     *
     * @return
     */
    public List<DetailInvoice> getAllDetailInvoiceOfClient(Client c) {
        List<MasterInvoice> md = getAllMasterInvoiceOfClient(c);
        List<DetailInvoice> di = new ArrayList<DetailInvoice>();

        for (MasterInvoice m : md) {
            List<DetailInvoice> list = detailController.getDetailedInvoiceFromMasterInvoice(m);
            for (DetailInvoice d : list) {
                di.add(d);
            }
        }

        return di;
    }

    /**
     * Returns a list of detail invoice for a master invoice by id.
     *
     * @param id
     * @return
     */
    public List<DetailInvoice> getDetailInvoiceForMasterInvoice(int id) {
        return (List<DetailInvoice>) detailController.getDetailedInvoiceFromMasterInvoiceById(id);
    }

    /**
     * Returns the number of items purchased from a master invoice.
     *
     * @param id
     * @return
     */
    public int getNumberOfItemsPerMasterInvoice(int id) {
        List<DetailInvoice> list = detailController.getDetailedInvoiceFromMasterInvoiceById(id);

        return list.size();
    }

    /**
     * Returns a master invoice by id looking at the query string parameter.
     *
     * @return The book object
     */
    public void details(int id) {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.redirect(context.getRequestContextPath() + "/client/orderdetails.xhtml");
            invoiceId = id;
        } catch (IOException io) {
            log.log(Level.WARNING, "Error redirecting: {0}", io.getMessage());
        }
    }

    /**
     * Returns the master invoice from a session scoped id
     *
     * @return
     */
    public MasterInvoice getMasterInvoice() {
        return masterController.getMasterInvoiceByID(invoiceId);
    }

    /**
     * Returns the master invoice ID
     *
     * @return
     */
    public int getMasterInvoiceId() {
        return invoiceId;
    }

    /**
     * Sends an email to the client.
     *
     * @param mi
     */
    public void sendEmail() {
        emailSender = new EmailSender(client);
        MasterInvoice masterInvoice = getMasterInvoice();
        emailSender.sendEmail(masterInvoice);
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.redirect(context.getRequestContextPath() + "/client/email_success.xhtml");
        } catch (IOException io) {
            log.log(Level.WARNING, "Error redirecting when registering: {0}", io.getMessage());
        }
    }
}
