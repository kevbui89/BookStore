package com.g3bookstore.customcontrollers;

import com.g3bookstore.controllers.EbookFormatJpaController;
import com.g3bookstore.controllers.exceptions.IllegalOrphanException;
import com.g3bookstore.controllers.exceptions.NonexistentEntityException;
import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import com.g3bookstore.entities.Ad;
import com.g3bookstore.entities.Book;
import com.g3bookstore.entities.Book_;
import com.g3bookstore.entities.EbookFormat;
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
 * Custom controller for Ebook Formats.
 *
 * @author Kevin Bui
 */
@Named("theEbookFormat")
@RequestScoped
public class CustomEbookFormatJpaController implements Serializable {

    @Inject
    private EbookFormatJpaController ebookFormatJpaController;

    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em = null;

    /**
     * Allow to create a valid ebookFormat.
     * 
     * @author Denis Lebedev
     * @param ebookFormat
     * @throws Exception 
     */
     public void create(EbookFormat ebookFormat) throws Exception {
        ebookFormatJpaController.create(ebookFormat);
    }
    
    /**
     * Allow to update an ebookFormat.
     * 
     * @author Denis Lebedev
     * @param ebookFormat
     * @throws NonexistentEntityException
     * @throws RollbackFailureException
     * @throws Exception 
     */
    public void update(EbookFormat ebookFormat) throws NonexistentEntityException, RollbackFailureException, Exception {
        ebookFormatJpaController.edit(ebookFormat);
    }
    
    /**
     * Allow to delete an ebookFormat.
     * 
     * @author Denis Lebedev
     * @param id
     * @throws IllegalOrphanException
     * @throws NonexistentEntityException
     * @throws RollbackFailureException
     * @throws Exception 
     */
public void delete(Integer id)  throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        ebookFormatJpaController.destroy(id);
    }
    
    /**
     * Method which will return all ebook formats in the database
     *
     * @return
     */
    public List<EbookFormat> getEbookFormats() {
        return ebookFormatJpaController.findEbookFormatEntities();
    }

    /**
     * Method which will find a specific ebook format by ID
     *
     * @param id
     * @return
     */
    public EbookFormat getEbookFormatByID(int id) {
        return ebookFormatJpaController.findEbookFormat(id);
    }

    /**
     * Method which will search through the genre table and return all the books
     * from a specific format.
     *
     * @param id The ebook format object
     * @return The book objects
     */
    public List<Book> getAllBooksByFormat(EbookFormat ebf) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Book> cq = cb.createQuery(Book.class);
        Root<Book> books = cq.from(Book.class);
        cq.select(books);
        cq.where(cb.equal(books.get(Book_.formatId), ebf));

        Query q = em.createQuery(cq);
        return ((List<Book>) q.getResultList());
    }
}
