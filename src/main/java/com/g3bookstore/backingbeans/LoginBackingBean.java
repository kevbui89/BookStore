package com.g3bookstore.backingbeans;

import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import com.g3bookstore.customcontrollers.CustomClientJpaController;
import com.g3bookstore.entities.Client;
import com.g3bookstore.util.MessageProvider;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

/**
 * BackingBean responsible for handling logging in and keeping track of email.
 *
 * @author Werner, Alessandro, Kevin
 * @version 28/02/2018
 * @since 1.8
 */
@Named("loginBean")
@SessionScoped
public class LoginBackingBean implements Serializable {

    @Inject
    private CustomClientJpaController customClientController;

    private Logger log = Logger.getLogger(LoginBackingBean.class.getName());
    private String previousPage = "/client/index.xhtml";
    private Client client;

    /**
     * Method responsible for retrieving the client.
     *
     * @return the client
     */
    public Client getClient() {
        if (client == null) {
            client = new Client();
        }
        return client;
    }

    /**
     * Method responsible for retrieving the email.
     *
     * @return the email
     */
    public String getEmail() {
        if (client == null) {
            return getClient().getEmail();
        }
        return client.getEmail();
    }

    /**
     * Method responsible for setting the client.
     *
     * @param client
     */
    public void setClient(final Client client) {
        this.client = client;
    }

    /**
     * Method responsible for retrieving the previous page.
     *
     * @return the email
     */
    public String getPreviousPage() {
        return previousPage;
    }

    /**
     * Method responsible for setting the previous page.
     *
     * @param previousPage
     */
    public void setPreviousPage(final String previousPage) {
        this.previousPage = previousPage;
    }

    /**
     * Method responsible for checking if the user is logged in in certain
     * pages, if not logged in, redirected to login page.
     *
     * @throws java.io.IOException
     */
    public void checkLoggedIn() throws IOException {
        if (client == null || client.getEmail() == null) {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.redirect(context.getRequestContextPath() + "/client/login.xhtml");
        }
    }
    
    /**
     * Method responsible for checking if the user is logged in in certain
     * pages, if not logged in, redirected to login page.
     *
     * @throws java.io.IOException
     */
    public void checkLoggedInAndManager() throws IOException {
        if (client == null || client.getEmail() == null) {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.redirect(context.getRequestContextPath() + "/client/login.xhtml");
        } else if (client.getManager() == false) {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.responseSendError(403, "403");
        }  
    }

    /**
     * Returns true if first time logging in, false otherwise
     *
     * @return
     */
    public boolean getFirstLogin() {
        return client.getFirstLogin();
    }

    /**
     * Method responsible for getting the client by the given email and checking
     * credentials. If valid, will go to login page using navigation rules.
     *
     */
    public void login() throws RollbackFailureException, Exception {
        client = getClient();
        try {
            if (customClientController.getClientByEmail(client.getEmail()).size() > 0) {
                Client clientDb = customClientController.getClientByEmail(client.getEmail()).get(0);
                if (!client.getPassword().equals(clientDb.getPassword())) {
                    FacesMessage message = new FacesMessage(new MessageProvider().getValue("invalidpwuser"));
                    FacesContext.getCurrentInstance().addMessage("loginForm", message);
                    client = null;
                } else {
                    try {
                        client = clientDb;
                        if (client.getManager()) {
                            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                            context.redirect(context.getRequestContextPath() + "/manager/manager.xhtml");
                        } else {
                            if (getFirstLogin()) {
                                client.setFirstLogin(false);
                                customClientController.update(client);
                                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                                context.redirect(context.getRequestContextPath() + "/client/preferences.xhtml");
                            } else {
                                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                                context.redirect(context.getRequestContextPath() + getPreviousPage());
                            }
                        }
                    } catch (IOException io) {
                        log.log(Level.WARNING, "Error redirecting: {0}", io.getMessage());
                    }
                }
            } else {
                FacesMessage message = new FacesMessage(new MessageProvider().getValue("invalidpwuser"));
                FacesContext.getCurrentInstance().addMessage("loginForm", message);
                client = null;
            }
        } catch (EntityNotFoundException | NonUniqueResultException | NoResultException ex) {
            log.log(Level.WARNING, "Invalid user: {0}", ex.getMessage());
            FacesMessage message = new FacesMessage(new MessageProvider().getValue("invalidpwuser"));
            FacesContext.getCurrentInstance().addMessage("loginForm", message);
            client = null;
        }
    }

    /**
     * Method responsible for logging out the user by making the email and
     * password string empty.
     *
     */
    public void logOut() {
        if (client != null) {
            try {
                client = null;
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.redirect(context.getRequestContextPath() + getPreviousPage());
            } catch (IOException io) {
                log.log(Level.WARNING, "Error redirecting: {0}", io.getMessage());
            }
        } else {
            client = null;
        }
    }
}
