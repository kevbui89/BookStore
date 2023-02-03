package com.g3bookstore.customcontrollers;

import com.g3bookstore.controllers.PublisherJpaController;
import com.g3bookstore.controllers.exceptions.IllegalOrphanException;
import com.g3bookstore.controllers.exceptions.NonexistentEntityException;
import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import com.g3bookstore.entities.Book;
import com.g3bookstore.entities.Book_;
import com.g3bookstore.entities.MasterInvoice;
import com.g3bookstore.entities.Publisher;
import com.g3bookstore.entities.Publisher_;
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
 * Custom controller for Publishers.
 *
 * @author Kevin Bui, Denis Lebedev
 */
@Named("thePublisher")
@RequestScoped
public class CustomPublisherJpaController implements Serializable {

    @Inject
    private PublisherJpaController publisherJpaController;

    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em = null;

    /**
     * Allow to create a Publisher
     * 
     * @param pub
     * @throws Exception 
     */
    public void create(Publisher pub) throws Exception {
        publisherJpaController.create(pub);
    }
    
    /**
     * Allow to delete a Publisher
     * 
     * @param id
     * @throws NonexistentEntityException
     * @throws RollbackFailureException
     * @throws Exception 
     */
    public void delete(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        publisherJpaController.destroy(id);
    }
    
    /**
     * Allow to update a Publisher.
     * 
     * @param pub
     * @throws IllegalOrphanException
     * @throws NonexistentEntityException
     * @throws RollbackFailureException
     * @throws Exception 
     */
    public void update(Publisher pub) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        publisherJpaController.edit(pub);
    }
    /**
     * Method which will return all publishers in the database
     *
     * @return
     */
    public List<Publisher> getPublishers() {
        return publisherJpaController.findPublisherEntities();
    }

    /**
     * Method which will find a specific publisher by ID
     *
     * @param id
     * @return
     */
    public Publisher getPublisherByID(int id) {
        return publisherJpaController.findPublisher(id);
    }

    /**
     * Method which will search through the genre table and return all the books
     * from a specific publisher.
     *
     * @param id The publisher object
     * @return The book objects
     */
    public List<Book> getAllBooksByPublisher(Publisher p) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Book> cq = cb.createQuery(Book.class);
        Root<Book> books = cq.from(Book.class);
        cq.select(books);
        cq.where(cb.equal(books.get(Book_.publisherId), p));

        Query q = em.createQuery(cq);
        return ((List<Book>) q.getResultList());
    }
    
    /**
     * Allow to find a publisher by the given name if only it is exactly the same.
     * 
     * @param pub
     * @return 
     */
    public List<Publisher> getPublisherByExactName(String pub) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Publisher> cq = cb.createQuery(Publisher.class);
        Root<Publisher> root = cq.from(Publisher.class);
        cq.select(root);
        cq.where(cb.equal(root.get(Publisher_.publisher), pub));

        Query q = em.createQuery(cq);
        return ((List<Publisher>)q.getResultList());
    }
}
