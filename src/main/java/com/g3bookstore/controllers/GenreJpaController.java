package com.g3bookstore.controllers;

import com.g3bookstore.controllers.exceptions.NonexistentEntityException;
import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import com.g3bookstore.entities.Book;
import com.g3bookstore.entities.Genre;

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
@Named("Genre")
@RequestScoped
public class GenreJpaController implements Serializable 
{
    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em = null;
    
    @Resource
    private UserTransaction utx = null;

    public void create(Genre genre) throws RollbackFailureException, Exception
    {
        if (genre.getBookList() == null) {
            genre.setBookList(new ArrayList<Book>());
        }
        try {
            utx.begin();
            List<Book> attachedBookList = new ArrayList<Book>();
            for (Book bookListBookToAttach : genre.getBookList()) {
                bookListBookToAttach = em.getReference(bookListBookToAttach.getClass(), bookListBookToAttach.getBookId());
                attachedBookList.add(bookListBookToAttach);
            }
            genre.setBookList(attachedBookList);
            em.persist(genre);
            for (Book bookListBook : genre.getBookList()) {
                bookListBook.getGenreList().add(genre);
                bookListBook = em.merge(bookListBook);
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

    public void edit(Genre genre) throws NonexistentEntityException, RollbackFailureException, Exception 
    {
        try {
            utx.begin();
            Genre persistentGenre = em.find(Genre.class, genre.getGenreId());
            List<Book> bookListOld = persistentGenre.getBookList();
            List<Book> bookListNew = genre.getBookList();
            List<Book> attachedBookListNew = new ArrayList<Book>();
            for (Book bookListNewBookToAttach : bookListNew) {
                bookListNewBookToAttach = em.getReference(bookListNewBookToAttach.getClass(), bookListNewBookToAttach.getBookId());
                attachedBookListNew.add(bookListNewBookToAttach);
            }
            bookListNew = attachedBookListNew;
            genre.setBookList(bookListNew);
            genre = em.merge(genre);
            for (Book bookListOldBook : bookListOld) {
                if (!bookListNew.contains(bookListOldBook)) {
                    bookListOldBook.getGenreList().remove(genre);
                    bookListOldBook = em.merge(bookListOldBook);
                }
            }
            for (Book bookListNewBook : bookListNew) {
                if (!bookListOld.contains(bookListNewBook)) {
                    bookListNewBook.getGenreList().add(genre);
                    bookListNewBook = em.merge(bookListNewBook);
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
                Integer id = genre.getGenreId();
                if (findGenre(id) == null) {
                    throw new NonexistentEntityException("The genre with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception 
    {
        try {
            utx.begin();
            Genre genre;
            try {
                genre = em.getReference(Genre.class, id);
                genre.getGenreId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The genre with id " + id + " no longer exists.", enfe);
            }
            List<Book> bookList = genre.getBookList();
            for (Book bookListBook : bookList) {
                bookListBook.getGenreList().remove(genre);
                bookListBook = em.merge(bookListBook);
            }
            em.remove(genre);
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

    public List<Genre> findGenreEntities() {
        return findGenreEntities(true, -1, -1);
    }

    public List<Genre> findGenreEntities(int maxResults, int firstResult) {
        return findGenreEntities(false, maxResults, firstResult);
    }

    private List<Genre> findGenreEntities(boolean all, int maxResults, int firstResult) 
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Genre.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public Genre findGenre(Integer id) {        
        return em.find(Genre.class, id);
    }

    public int getGenreCount() 
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Genre> rt = cq.from(Genre.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }    
}