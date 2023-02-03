package com.g3bookstore.backingbeans;

import com.g3bookstore.customcontrollers.CustomClientJpaController;
import java.io.Serializable;
import com.g3bookstore.entities.Client;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.RowEditEvent;

/**
 * This class will take care of the
 *
 * @author Werner
 */
@Named("ClientManagement")
@SessionScoped
public class ClientsBackingBean implements Serializable {

    @Inject
    private CustomClientJpaController clientController;
    private List<Client> clients;

    private Logger log = Logger.getLogger(BrowseBackingBean.class.getName());

    /**
     * Helper method to call the db for a list of all of the clients
     *
     * @return
     */
    private List<Client> callClientsDB() {
        return clientController.getClients();
    }

    /**
     * Getter, returns a list of all the clients within the db.
     *
     * @return
     */
    public List<Client> getClients() {
        if (this.clients == null) {
            this.clients = callClientsDB();
        }
        return this.clients;
    }

    /**
     * Edits the client object that raised the event and edits that object on
     * the database
     *
     * @param event
     * @throws Exception
     */
    public void onRowEdit(RowEditEvent event) throws Exception {
        Client updatedClient = (Client) event.getObject();
        log.log(Level.INFO, null, "client object: " + updatedClient.toString());
        clientController.update(updatedClient);
    }

    /**
     * Method is triggered when the user decides to cancel the edits he has done
     * on a client
     *
     * @param event
     */
    public void onRowCancel(RowEditEvent event) {
        log.log(Level.INFO, null, "cancled client update ");
        this.clients = callClientsDB();
    }

}
