package com.g3bookstore.controllers;

import com.g3bookstore.controllers.exceptions.IllegalOrphanException;
import com.g3bookstore.controllers.exceptions.NonexistentEntityException;
import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import com.g3bookstore.entities.Book;
import com.g3bookstore.entities.EbookFormat;

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
@Named("Ebook")
@RequestScoped
public class EbookFormatJpaController implements Serializable 
{
    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em = null;
    
    @Resource
    private UserTransaction utx = null;

    public void create(EbookFormat ebookFormat) throws RollbackFailureException, Exception 
    {
        if (ebookFormat.getBookList() == null) {
            ebookFormat.setBookList(new ArrayList<Book>());
        }
        try {
            utx.begin();
            List<Book> attachedBookList = new ArrayList<Book>();
            for (Book bookListBookToAttach : ebookFormat.getBookList()) {
                bookListBookToAttach = em.getReference(bookListBookToAttach.getClass(), bookListBookToAttach.getBookId());
                attachedBookList.add(bookListBookToAttach);
            }
            ebookFormat.setBookList(attachedBookList);
            em.persist(ebookFormat);
            for (Book bookListBook : ebookFormat.getBookList()) {
                EbookFormat oldFormatIdOfBookListBook = bookListBook.getFormatId();
                bookListBook.setFormatId(ebookFormat);
                bookListBook = em.merge(bookListBook);
                if (oldFormatIdOfBookListBook != null) {
                    oldFormatIdOfBookListBook.getBookList().remove(bookListBook);
                    oldFormatIdOfBookListBook = em.merge(oldFormatIdOfBookListBook);
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

    public void edit(EbookFormat ebookFormat) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception 
    {
        try {
            utx.begin();
            EbookFormat persistentEbookFormat = em.find(EbookFormat.class, ebookFormat.getFormatId());
            List<Book> bookListOld = persistentEbookFormat.getBookList();
            List<Book> bookListNew = ebookFormat.getBookList();
            List<String> illegalOrphanMessages = null;
            for (Book bookListOldBook : bookListOld) {
                if (!bookListNew.contains(bookListOldBook)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Book " + bookListOldBook + " since its formatId field is not nullable.");
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
            ebookFormat.setBookList(bookListNew);
            ebookFormat = em.merge(ebookFormat);
            for (Book bookListNewBook : bookListNew) {
                if (!bookListOld.contains(bookListNewBook)) {
                    EbookFormat oldFormatIdOfBookListNewBook = bookListNewBook.getFormatId();
                    bookListNewBook.setFormatId(ebookFormat);
                    bookListNewBook = em.merge(bookListNewBook);
                    if (oldFormatIdOfBookListNewBook != null && !oldFormatIdOfBookListNewBook.equals(ebookFormat)) {
                        oldFormatIdOfBookListNewBook.getBookList().remove(bookListNewBook);
                        oldFormatIdOfBookListNewBook = em.merge(oldFormatIdOfBookListNewBook);
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
                Integer id = ebookFormat.getFormatId();
                if (findEbookFormat(id) == null) {
                    throw new NonexistentEntityException("The ebookFormat with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception 
    {
        try {
            utx.begin();
            EbookFormat ebookFormat;
            try {
                ebookFormat = em.getReference(EbookFormat.class, id);
                ebookFormat.getFormatId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ebookFormat with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Book> bookListOrphanCheck = ebookFormat.getBookList();
            for (Book bookListOrphanCheckBook : bookListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This EbookFormat (" + ebookFormat + ") cannot be destroyed since the Book " + bookListOrphanCheckBook + " in its bookList field has a non-nullable formatId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(ebookFormat);
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

    public List<EbookFormat> findEbookFormatEntities() {
        return findEbookFormatEntities(true, -1, -1);
    }

    public List<EbookFormat> findEbookFormatEntities(int maxResults, int firstResult) {
        return findEbookFormatEntities(false, maxResults, firstResult);
    }

    private List<EbookFormat> findEbookFormatEntities(boolean all, int maxResults, int firstResult) 
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(EbookFormat.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public EbookFormat findEbookFormat(Integer id) 
    {
        return em.find(EbookFormat.class, id);
    }

    public int getEbookFormatCount() 
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<EbookFormat> rt = cq.from(EbookFormat.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }    
}