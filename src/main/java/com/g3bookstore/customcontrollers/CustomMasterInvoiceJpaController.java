package com.g3bookstore.customcontrollers;

import com.g3bookstore.controllers.MasterInvoiceJpaController;
import com.g3bookstore.controllers.exceptions.IllegalOrphanException;
import com.g3bookstore.controllers.exceptions.NonexistentEntityException;
import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import com.g3bookstore.entities.Client;
import com.g3bookstore.entities.MasterInvoice;
import com.g3bookstore.entities.MasterInvoice_;
import java.io.Serializable;
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
 * Custom controller for Master Invoice.
 *
 * @author Kevin Bui, Denis Lebedev
 */
@Named("theMasterInvoice")
@RequestScoped
public class CustomMasterInvoiceJpaController implements Serializable {

    @Inject
    private MasterInvoiceJpaController masterInvoiceJpaController;

    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em = null;

    /**
     * Allow to create a master invoice
     *
     * @param mi
     * @throws Exception
     */
    public void create(MasterInvoice mi) throws Exception {
        masterInvoiceJpaController.create(mi);
    }

    /**
     * Allow to delete a master invoice
     *
     * @param id
     * @throws NonexistentEntityException
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void delete(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        masterInvoiceJpaController.destroy(id);
    }

    /**
     * Allow to update a master invoice.
     *
     * @param mi
     * @throws IllegalOrphanException
     * @throws NonexistentEntityException
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void update(MasterInvoice mi) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        masterInvoiceJpaController.edit(mi);
    }

    /**
     * Method which will return all master invoices in the database
     *
     * @return
     */
    public List<MasterInvoice> getMasterInvoices() {
        return masterInvoiceJpaController.findMasterInvoiceEntities();
    }

    /**
     * Method which will find a specific master invoice by ID
     *
     * @param id
     * @return
     */
    public MasterInvoice getMasterInvoiceByID(int id) {
        return masterInvoiceJpaController.findMasterInvoice(id);
    }

    /**
     * Method which will search through the client table the client's invoices.
     *
     * @return The Book objects
     */
    public List<MasterInvoice> getMasterInvoiceForClient(Client client) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<MasterInvoice> cq = cb.createQuery(MasterInvoice.class);
        Root<MasterInvoice> masterInvoice = cq.from(MasterInvoice.class);
        cq.select(masterInvoice);
        cq.where(cb.equal(masterInvoice.get(MasterInvoice_.clientId), client));

        Query q = em.createQuery(cq);
        return (List<MasterInvoice>) q.getResultList();
    }
    
    /**
     * Method which will return the last id used.
     *
     * @return The id number
     */
    public int getMasterInvoiceId() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<MasterInvoice> cq = cb.createQuery(MasterInvoice.class);
        Root<MasterInvoice> masterInvoice = cq.from(MasterInvoice.class);
        cq.select(masterInvoice);

        Query q = em.createQuery(cq);
        List<MasterInvoice> list = (List<MasterInvoice>) q.getResultList();
        return list.size();
    }

}
