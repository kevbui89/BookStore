package com.g3bookstore.backingbeans;

import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import com.g3bookstore.customcontrollers.CustomClientJpaController;
import com.g3bookstore.entities.Client;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Responsible for holding keeping client data for account settings.
 *
 * @author Alessandro Ciotola, Kevin Bui
 * @version 02/03/2018
 * @since 1.8
 */
@Named("acctSettings")
@SessionScoped
public class AccountSettingsBackingBean implements Serializable {

    @Inject
    private CustomClientJpaController clientController;

    private final Logger log = Logger.getLogger(AccountSettingsBackingBean.class.getName());
    private Client client;
    private String previousPage = "/client/index.xhtml";

    /**
     * Returns the client
     *
     * @return
     */
    public Client getClient() {
        return client;
    }

    /**
     * Sets the client
     *
     * @param client
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * Returns the previous page
     *
     * @return
     */
    public String getPreviousPage() {
        return previousPage;
    }

    /**
     * Sets the previous page
     *
     */
    public void setPreviousPage(final String previousPage) {
        this.previousPage = previousPage;
    }

    /**
     * When the update button is clicked, update the client information.
     *
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void updateClient() throws RollbackFailureException, Exception {
        if (client != null) {
            try {
                clientController.update(client);
                if (previousPage.equals("/client/checkout.xhtml")) {
                    try {
                        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                        context.redirect(context.getRequestContextPath() + "/client/checkout.xhtml");
                    } catch (IOException io) {
                        log.log(Level.WARNING, "Error redirecting when registering: {0}", io.getMessage());
                    }
                    previousPage = "";
                } else {
                    try {
                        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                        context.redirect(context.getRequestContextPath() + "/client/account_settings_update_success.xhtml");
                    } catch (IOException io) {
                        log.log(Level.WARNING, "Error redirecting when registering: {0}", io.getMessage());
                    }
                }
            } catch (Exception ex) {
                log.log(Level.WARNING, "Error updating the database: {0}", ex.getMessage());
            }
        }
    }

    /* Methods responsible for getting the client information
     * 
     * @param email 
     */
    public void getClientInfo(String email) {
        List<Client> clients = clientController.getClientByEmail(email);
        client = clients.get(0);
    }
}
