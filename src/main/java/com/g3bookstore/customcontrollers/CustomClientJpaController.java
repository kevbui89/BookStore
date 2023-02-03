package com.g3bookstore.customcontrollers;

import com.g3bookstore.controllers.ClientJpaController;
import com.g3bookstore.controllers.exceptions.IllegalOrphanException;
import com.g3bookstore.controllers.exceptions.NonexistentEntityException;
import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import com.g3bookstore.entities.Book;
import com.g3bookstore.entities.Client;
import com.g3bookstore.entities.Client_;
import com.g3bookstore.entities.MasterInvoice;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Custom controller for Clients.
 *
 * @author Alessandro Ciotola, Denis Levedev
 */
@Named("theClient")
@RequestScoped
public class CustomClientJpaController implements Serializable {

    @Inject
    private ClientJpaController clientJpaController;

    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em = null;

    /**
     * Allow to create a valid client.
     *
     * @param client
     * @throws Exception
     */
    public void create(Client client) throws Exception {
        clientJpaController.create(client);
    }

    /**
     * Allow to update an entity.
     *
     * @param client
     * @throws NonexistentEntityException
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void update(Client client) throws NonexistentEntityException, RollbackFailureException, Exception {
        clientJpaController.edit(client);
    }

    /**
     * Allow to delete a client.
     *
     * @param id
     * @throws IllegalOrphanException
     * @throws NonexistentEntityException
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void delete(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        clientJpaController.destroy(id);
    }

    /**
     * Method which will return all clients in the database
     *
     * @return
     */
    public List<Client> getClients() {
        return clientJpaController.findClientEntities();
    }

    /**
     * Method which will find a specific client by ID
     *
     * @param id
     * @return
     */
    public Client getClientByID(int id) {
        return clientJpaController.findClient(id);
    }

    /**
     * Method which will search through the client table for a client using a
     * username.
     *
     * @param userName
     * @return The Client object
     */
    public List<Client> getClientByUserName(String userName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Client> cq = cb.createQuery(Client.class);
        Root<Client> client = cq.from(Client.class);
        cq.select(client);
        cq.where(cb.equal(client.get(Client_.username), userName));

        Query q = em.createQuery(cq);
        return (List<Client>) q.getResultList();
    }

    /**
     * Method which will search through the client table for a client using an
     * email.
     *
     * @param email
     * @return The Client object
     */
    public List<Client> getClientByEmail(String email) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Client> cq = cb.createQuery(Client.class);
        Root<Client> client = cq.from(Client.class);
        cq.select(client);
        cq.where(cb.equal(client.get(Client_.email), email));

        Query q = em.createQuery(cq);
        return (List<Client>) q.getResultList();
    }

    /**
     * Method which will search through the client table for a client using an
     * email.
     *
     * @param userName
     * @return The Client object
     */
    public BigDecimal getClientByTotalSales(String userName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Client> cq = cb.createQuery(Client.class);

        Root<Client> client = cq.from(Client.class);
        cq.select(client);
        cq.where(cb.equal(client.get(Client_.username), userName));

        Query q = em.createQuery(cq);

        Client newClient = (Client) q.getSingleResult();
        List<MasterInvoice> invoices = newClient.getMasterInvoiceList();
        return invoices.get(0).getGrossValue();
    }

    /**
     * Method which will search through the client table for all items purchased
     * by a specific client.
     *
     * @param userName
     * @return The Client object
     */
    public List<Book> getClientByItemsPurchased(String userName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Client> cq = cb.createQuery(Client.class);
        Root<Client> client = cq.from(Client.class);
        cq.select(client);
        cq.where(cb.equal(client.get(Client_.username), userName));

        Query q = em.createQuery(cq);
        Client newClient = (Client) q.getSingleResult();
        List<MasterInvoice> invoices = newClient.getMasterInvoiceList();

        List<Book> bookList = new ArrayList<>();

        for (int i = 0; i < invoices.size(); i++) {
            for (int j = 0; j < invoices.get(i).getDetailInvoiceList().size(); j++) {
                bookList.add(invoices.get(i).getDetailInvoiceList().get(j).getBookId());
            }
        }
        return bookList;
    }
}
