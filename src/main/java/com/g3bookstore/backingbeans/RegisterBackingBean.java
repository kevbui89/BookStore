package com.g3bookstore.backingbeans;

import com.g3bookstore.customcontrollers.CustomClientJpaController;
import com.g3bookstore.entities.Client;
import com.g3bookstore.util.MessageProvider;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * This backing bean is responsible for client registration
 *
 * @author Kevin Bui, Werner
 */
@Named("Register")
@ManagedBean
@RequestScoped
public class RegisterBackingBean implements Serializable {

    private final Logger log = Logger.getLogger(RegisterBackingBean.class.getName());
    private Client client;
    private String password;

    @Inject
    private CustomClientJpaController customClientController;

    /**
     * Returns a client
     *
     * @return
     */
    public Client getClient() {
        if (client == null) {
            client = new Client();
        }

        return client;
    }

    /**
     *
     * @param client
     */
    public void setClient(final Client client) {
        this.client = client;
    }

    /**
     * Sets the password for the client
     *
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * Saves a client to the database
     *
     * @throws Exception
     */
    public void registerClient() throws Exception {
        customClientController.create(client);
    }

    /**
     * Initializes the client
     */
    @PostConstruct
    public void init() {
        client = new Client();
        password = "";
    }

    /**
     * Registers a user to the database
     *
     * @throws Exception
     */
    public void register() throws Exception {
        Client newClient = getClient();
        newClient.setManager(false);
        if (customClientController.getClientByEmail(newClient.getEmail()).size() == 1) {
            log.log(Level.INFO, "An account already exists with this email.");
            FacesMessage message = new FacesMessage(new MessageProvider().getValue("userexistsemail"));
            FacesContext.getCurrentInstance().addMessage("registerForm:email", message);
        } else if (customClientController.getClientByUserName(newClient.getUsername()).size() == 1) {
            log.log(Level.INFO, "An account already exists with this username.");
            FacesMessage message = new FacesMessage(new MessageProvider().getValue("userexistsname"));
            FacesContext.getCurrentInstance().addMessage("registerForm:username", message);
        } else {
            registerClient();
            try {
                client.setFirstLogin(true);
                customClientController.update(client);
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.redirect(context.getRequestContextPath() + "/client/index.xhtml");
            } catch (IOException io) {
                log.log(Level.WARNING, "Error redirecting when registering: {0}", io.getMessage());
            }
        }
    }
}
