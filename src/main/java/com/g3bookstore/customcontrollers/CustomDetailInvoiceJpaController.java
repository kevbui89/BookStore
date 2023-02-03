package com.g3bookstore.customcontrollers;

import com.g3bookstore.controllers.DetailInvoiceJpaController;
import com.g3bookstore.controllers.MasterInvoiceJpaController;
import com.g3bookstore.controllers.exceptions.NonexistentEntityException;
import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import com.g3bookstore.entities.DetailInvoice;
import com.g3bookstore.entities.DetailInvoice_;
import com.g3bookstore.entities.MasterInvoice;
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
 * Custom controller for Detailed Invoices.
 *
 * @author Kevin Bui, Denis Lebedev
 */
@Named("theDetailInvoice")
@RequestScoped
public class CustomDetailInvoiceJpaController implements Serializable {

    @Inject
    private DetailInvoiceJpaController detailInvoiceJpaController;

    @Inject
    private MasterInvoiceJpaController masterInvoiceJpaController;

    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em = null;

    /**
     * Allow to create a detail invoice.
     *
     * @param di
     * @throws Exception
     */
    public void create(DetailInvoice di) throws Exception {
        detailInvoiceJpaController.create(di);
    }

    /**
     * Allow to delete a detail invoice.
     *
     * @param id
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void delete(Integer id) throws RollbackFailureException, Exception {
        detailInvoiceJpaController.destroy(id);
    }

    /**
     * Allow to update a detail invoice.
     *
     * @param di
     * @throws NonexistentEntityException
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void update(DetailInvoice di) throws NonexistentEntityException, RollbackFailureException, Exception {
        detailInvoiceJpaController.edit(di);
    }

    /**
     * Method which will return all detailed invoices in the database
     *
     * @return
     */
    public List<DetailInvoice> getDetailedInvoice() {
        return detailInvoiceJpaController.findDetailInvoiceEntities();
    }

    /**
     * Method which will find a specific detailed invoice by ID
     *
     * @param id
     * @return
     */
    public DetailInvoice getDetailInvoiceByID(int id) {
        return detailInvoiceJpaController.findDetailInvoice(id);
    }

    /**
     * Method which will search through the detail invoice table from the master
     * invoice.
     *
     * @param mi
     * @return The Book objects
     */
    public List<DetailInvoice> getDetailedInvoiceFromMasterInvoice(MasterInvoice mi) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<DetailInvoice> cq = cb.createQuery(DetailInvoice.class);
        Root<DetailInvoice> di = cq.from(DetailInvoice.class);
        cq.select(di);
        cq.where(cb.equal(di.get(DetailInvoice_.invoiceId), mi));

        Query q = em.createQuery(cq);
        return (List<DetailInvoice>) q.getResultList();
    }

    /**
     * Method which will search through the detail invoice table from the master
     * invoice.
     *
     * @param id
     * @return The Book objects
     */
    public List<DetailInvoice> getDetailedInvoiceFromMasterInvoiceById(int id) {
        MasterInvoice mi = masterInvoiceJpaController.findMasterInvoice(id);

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<DetailInvoice> cq = cb.createQuery(DetailInvoice.class);
        Root<DetailInvoice> di = cq.from(DetailInvoice.class);
        cq.select(di);
        cq.where(cb.equal(di.get(DetailInvoice_.invoiceId), mi));

        Query q = em.createQuery(cq);
        return (List<DetailInvoice>) q.getResultList();
    }
}
