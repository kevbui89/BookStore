package com.g3bookstore.controllers;

import com.g3bookstore.controllers.exceptions.IllegalOrphanException;
import com.g3bookstore.controllers.exceptions.NonexistentEntityException;
import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import com.g3bookstore.entities.Book;
import com.g3bookstore.entities.Publisher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Generated controller updated to use CDI
 * 
 * @author Alessandro, Denis, Kevin, Werner
 */
@Named("Publisher")
@RequestScoped
public class PublisherJpaController implements Serializable 
{
    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em = null;
    
    @Resource
    private UserTransaction utx = null;

    public void create(Publisher publisher) throws RollbackFailureException, Exception 
    {
        if (publisher.getBookList() == null) {
            publisher.setBookList(new ArrayList<Book>());
        }
        try {
            utx.begin();
            List<Book> attachedBookList = new ArrayList<Book>();
            for (Book bookListBookToAttach : publisher.getBookList()) {
                bookListBookToAttach = em.getReference(bookListBookToAttach.getClass(), bookListBookToAttach.getBookId());
                attachedBookList.add(bookListBookToAttach);
            }
            publisher.setBookList(attachedBookList);
            em.persist(publisher);
            for (Book bookListBook : publisher.getBookList()) {
                Publisher oldPublisherIdOfBookListBook = bookListBook.getPublisherId();
                bookListBook.setPublisherId(publisher);
                bookListBook = em.merge(bookListBook);
                if (oldPublisherIdOfBookListBook != null) {
                    oldPublisherIdOfBookListBook.getBookList().remove(bookListBook);
                    oldPublisherIdOfBookListBook = em.merge(oldPublisherIdOfBookListBook);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } 
    }

    public void edit(Publisher publisher) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception 
    {
        try {
            utx.begin();
            Publisher persistentPublisher = em.find(Publisher.class, publisher.getPublisherId());
            List<Book> bookListOld = persistentPublisher.getBookList();
            List<Book> bookListNew = publisher.getBookList();
            List<String> illegalOrphanMessages = null;
            for (Book bookListOldBook : bookListOld) {
                if (!bookListNew.contains(bookListOldBook)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Book " + bookListOldBook + " since its publisherId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Book> attachedBookListNew = new ArrayList<Book>();
            for (Book bookListNewBookToAttach : bookListNew) {
                bookListNewBookToAttach = em.getReference(bookListNewBookToAttach.getClass(), bookListNewBookToAttach.getBookId());
                attachedBookListNew.add(bookListNewBookToAttach);
            }
            bookListNew = attachedBookListNew;
            publisher.setBookList(bookListNew);
            publisher = em.merge(publisher);
            for (Book bookListNewBook : bookListNew) {
                if (!bookListOld.contains(bookListNewBook)) {
                    Publisher oldPublisherIdOfBookListNewBook = bookListNewBook.getPublisherId();
                    bookListNewBook.setPublisherId(publisher);
                    bookListNewBook = em.merge(bookListNewBook);
                    if (oldPublisherIdOfBookListNewBook != null && !oldPublisherIdOfBookListNewBook.equals(publisher)) {
                        oldPublisherIdOfBookListNewBook.getBookList().remove(bookListNewBook);
                        oldPublisherIdOfBookListNewBook = em.merge(oldPublisherIdOfBookListNewBook);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = publisher.getPublisherId();
                if (findPublisher(id) == null) {
                    throw new NonexistentEntityException("The publisher with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception 
    {
        try {
            utx.begin();
            Publisher publisher;
            try {
                publisher = em.getReference(Publisher.class, id);
                publisher.getPublisherId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The publisher with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Book> bookListOrphanCheck = publisher.getBookList();
            for (Book bookListOrphanCheckBook : bookListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Publisher (" + publisher + ") cannot be destroyed since the Book " + bookListOrphanCheckBook + " in its bookList field has a non-nullable publisherId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(publisher);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        }
    }

    public List<Publisher> findPublisherEntities() {
        return findPublisherEntities(true, -1, -1);
    }

    public List<Publisher> findPublisherEntities(int maxResults, int firstResult) {
        return findPublisherEntities(false, maxResults, firstResult);
    }

    private List<Publisher> findPublisherEntities(boolean all, int maxResults, int firstResult) 
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Publisher.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public Publisher findPublisher(Integer id)
    {
        return em.find(Publisher.class, id);
    }

    public int getPublisherCount() 
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Publisher> rt = cq.from(Publisher.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }  
}