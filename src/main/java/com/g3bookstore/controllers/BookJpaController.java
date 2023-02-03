package com.g3bookstore.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.g3bookstore.controllers.exceptions.IllegalOrphanException;
import com.g3bookstore.controllers.exceptions.NonexistentEntityException;
import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import com.g3bookstore.entities.Publisher;
import com.g3bookstore.entities.EbookFormat;
import com.g3bookstore.entities.Author;
import com.g3bookstore.entities.Book;
import com.g3bookstore.entities.Genre;
import com.g3bookstore.entities.DetailInvoice;
import com.g3bookstore.entities.Review;

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
@Named("Book")
@RequestScoped
public class BookJpaController implements Serializable
{
    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em = null;
    
    @Resource
    private UserTransaction utx = null;
    
    public void create(Book book) throws RollbackFailureException, Exception {
        if (book.getAuthorList() == null) {
            book.setAuthorList(new ArrayList<Author>());
        }
        if (book.getGenreList() == null) {
            book.setGenreList(new ArrayList<Genre>());
        }
        if (book.getDetailInvoiceList() == null) {
            book.setDetailInvoiceList(new ArrayList<DetailInvoice>());
        }
        if (book.getReviewList() == null) {
            book.setReviewList(new ArrayList<Review>());
        }
        try {
            utx.begin();
            Publisher publisherId = book.getPublisherId();
            if (publisherId != null) {
                publisherId = em.getReference(publisherId.getClass(), publisherId.getPublisherId());
                book.setPublisherId(publisherId);
            }
            EbookFormat formatId = book.getFormatId();
            if (formatId != null) {
                formatId = em.getReference(formatId.getClass(), formatId.getFormatId());
                book.setFormatId(formatId);
            }
            List<Author> attachedAuthorList = new ArrayList<Author>();
            for (Author authorListAuthorToAttach : book.getAuthorList()) {
                authorListAuthorToAttach = em.getReference(authorListAuthorToAttach.getClass(), authorListAuthorToAttach.getAuthorId());
                attachedAuthorList.add(authorListAuthorToAttach);
            }
            book.setAuthorList(attachedAuthorList);
            List<Genre> attachedGenreList = new ArrayList<Genre>();
            for (Genre genreListGenreToAttach : book.getGenreList()) {
                genreListGenreToAttach = em.getReference(genreListGenreToAttach.getClass(), genreListGenreToAttach.getGenreId());
                attachedGenreList.add(genreListGenreToAttach);
            }
            book.setGenreList(attachedGenreList);
            List<DetailInvoice> attachedDetailInvoiceList = new ArrayList<DetailInvoice>();
            for (DetailInvoice detailInvoiceListDetailInvoiceToAttach : book.getDetailInvoiceList()) {
                detailInvoiceListDetailInvoiceToAttach = em.getReference(detailInvoiceListDetailInvoiceToAttach.getClass(), detailInvoiceListDetailInvoiceToAttach.getDetailId());
                attachedDetailInvoiceList.add(detailInvoiceListDetailInvoiceToAttach);
            }
            book.setDetailInvoiceList(attachedDetailInvoiceList);
            List<Review> attachedReviewList = new ArrayList<Review>();
            for (Review reviewListReviewToAttach : book.getReviewList()) {
                reviewListReviewToAttach = em.getReference(reviewListReviewToAttach.getClass(), reviewListReviewToAttach.getReviewId());
                attachedReviewList.add(reviewListReviewToAttach);
            }
            book.setReviewList(attachedReviewList);
            em.persist(book);
            if (publisherId != null) {
                publisherId.getBookList().add(book);
                publisherId = em.merge(publisherId);
            }
            if (formatId != null) {
                formatId.getBookList().add(book);
                formatId = em.merge(formatId);
            }
            for (Author authorListAuthor : book.getAuthorList()) {
                authorListAuthor.getBookList().add(book);
                authorListAuthor = em.merge(authorListAuthor);
            }
            for (Genre genreListGenre : book.getGenreList()) {
                genreListGenre.getBookList().add(book);
                genreListGenre = em.merge(genreListGenre);
            }
            for (DetailInvoice detailInvoiceListDetailInvoice : book.getDetailInvoiceList()) {
                Book oldBookIdOfDetailInvoiceListDetailInvoice = detailInvoiceListDetailInvoice.getBookId();
                detailInvoiceListDetailInvoice.setBookId(book);
                detailInvoiceListDetailInvoice = em.merge(detailInvoiceListDetailInvoice);
                if (oldBookIdOfDetailInvoiceListDetailInvoice != null) {
                    oldBookIdOfDetailInvoiceListDetailInvoice.getDetailInvoiceList().remove(detailInvoiceListDetailInvoice);
                    oldBookIdOfDetailInvoiceListDetailInvoice = em.merge(oldBookIdOfDetailInvoiceListDetailInvoice);
                }
            }
            for (Review reviewListReview : book.getReviewList()) {
                Book oldBookIdOfReviewListReview = reviewListReview.getBookId();
                reviewListReview.setBookId(book);
                reviewListReview = em.merge(reviewListReview);
                if (oldBookIdOfReviewListReview != null) {
                    oldBookIdOfReviewListReview.getReviewList().remove(reviewListReview);
                    oldBookIdOfReviewListReview = em.merge(oldBookIdOfReviewListReview);
                }
            }
            utx.commit();
        } catch (Exception ex)
        {
            try{
                utx.rollback();
            } 
            catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } 
    }

    public void edit(Book book) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception 
    {
        try {
            utx.begin();
            Book persistentBook = em.find(Book.class, book.getBookId());
            Publisher publisherIdOld = persistentBook.getPublisherId();
            Publisher publisherIdNew = book.getPublisherId();
            EbookFormat formatIdOld = persistentBook.getFormatId();
            EbookFormat formatIdNew = book.getFormatId();
            List<Author> authorListOld = persistentBook.getAuthorList();
            List<Author> authorListNew = book.getAuthorList();
            List<Genre> genreListOld = persistentBook.getGenreList();
            List<Genre> genreListNew = book.getGenreList();
            List<DetailInvoice> detailInvoiceListOld = persistentBook.getDetailInvoiceList();
            List<DetailInvoice> detailInvoiceListNew = book.getDetailInvoiceList();
            List<Review> reviewListOld = persistentBook.getReviewList();
            List<Review> reviewListNew = book.getReviewList();
            List<String> illegalOrphanMessages = null;
            for (DetailInvoice detailInvoiceListOldDetailInvoice : detailInvoiceListOld) {
                if (!detailInvoiceListNew.contains(detailInvoiceListOldDetailInvoice)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DetailInvoice " + detailInvoiceListOldDetailInvoice + " since its bookId field is not nullable.");
                }
            }
            for (Review reviewListOldReview : reviewListOld) {
                if (!reviewListNew.contains(reviewListOldReview)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Review " + reviewListOldReview + " since its bookId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (publisherIdNew != null) {
                publisherIdNew = em.getReference(publisherIdNew.getClass(), publisherIdNew.getPublisherId());
                book.setPublisherId(publisherIdNew);
            }
            if (formatIdNew != null) {
                formatIdNew = em.getReference(formatIdNew.getClass(), formatIdNew.getFormatId());
                book.setFormatId(formatIdNew);
            }
            List<Author> attachedAuthorListNew = new ArrayList<Author>();
            for (Author authorListNewAuthorToAttach : authorListNew) {
                authorListNewAuthorToAttach = em.getReference(authorListNewAuthorToAttach.getClass(), authorListNewAuthorToAttach.getAuthorId());
                attachedAuthorListNew.add(authorListNewAuthorToAttach);
            }
            authorListNew = attachedAuthorListNew;
            book.setAuthorList(authorListNew);
            List<Genre> attachedGenreListNew = new ArrayList<Genre>();
            for (Genre genreListNewGenreToAttach : genreListNew) {
                genreListNewGenreToAttach = em.getReference(genreListNewGenreToAttach.getClass(), genreListNewGenreToAttach.getGenreId());
                attachedGenreListNew.add(genreListNewGenreToAttach);
            }
            genreListNew = attachedGenreListNew;
            book.setGenreList(genreListNew);
            List<DetailInvoice> attachedDetailInvoiceListNew = new ArrayList<DetailInvoice>();
            for (DetailInvoice detailInvoiceListNewDetailInvoiceToAttach : detailInvoiceListNew) {
                detailInvoiceListNewDetailInvoiceToAttach = em.getReference(detailInvoiceListNewDetailInvoiceToAttach.getClass(), detailInvoiceListNewDetailInvoiceToAttach.getDetailId());
                attachedDetailInvoiceListNew.add(detailInvoiceListNewDetailInvoiceToAttach);
            }
            detailInvoiceListNew = attachedDetailInvoiceListNew;
            book.setDetailInvoiceList(detailInvoiceListNew);
            List<Review> attachedReviewListNew = new ArrayList<Review>();
            for (Review reviewListNewReviewToAttach : reviewListNew) {
                reviewListNewReviewToAttach = em.getReference(reviewListNewReviewToAttach.getClass(), reviewListNewReviewToAttach.getReviewId());
                attachedReviewListNew.add(reviewListNewReviewToAttach);
            }
            reviewListNew = attachedReviewListNew;
            book.setReviewList(reviewListNew);
            book = em.merge(book);
            if (publisherIdOld != null && !publisherIdOld.equals(publisherIdNew)) {
                publisherIdOld.getBookList().remove(book);
                publisherIdOld = em.merge(publisherIdOld);
            }
            if (publisherIdNew != null && !publisherIdNew.equals(publisherIdOld)) {
                publisherIdNew.getBookList().add(book);
                publisherIdNew = em.merge(publisherIdNew);
            }
            if (formatIdOld != null && !formatIdOld.equals(formatIdNew)) {
                formatIdOld.getBookList().remove(book);
                formatIdOld = em.merge(formatIdOld);
            }
            if (formatIdNew != null && !formatIdNew.equals(formatIdOld)) {
                formatIdNew.getBookList().add(book);
                formatIdNew = em.merge(formatIdNew);
            }
            for (Author authorListOldAuthor : authorListOld) {
                if (!authorListNew.contains(authorListOldAuthor)) {
                    authorListOldAuthor.getBookList().remove(book);
                    authorListOldAuthor = em.merge(authorListOldAuthor);
                }
            }
            for (Author authorListNewAuthor : authorListNew) {
                if (!authorListOld.contains(authorListNewAuthor)) {
                    authorListNewAuthor.getBookList().add(book);
                    authorListNewAuthor = em.merge(authorListNewAuthor);
                }
            }
            for (Genre genreListOldGenre : genreListOld) {
                if (!genreListNew.contains(genreListOldGenre)) {
                    genreListOldGenre.getBookList().remove(book);
                    genreListOldGenre = em.merge(genreListOldGenre);
                }
            }
            for (Genre genreListNewGenre : genreListNew) {
                if (!genreListOld.contains(genreListNewGenre)) {
                    genreListNewGenre.getBookList().add(book);
                    genreListNewGenre = em.merge(genreListNewGenre);
                }
            }
            for (DetailInvoice detailInvoiceListNewDetailInvoice : detailInvoiceListNew) {
                if (!detailInvoiceListOld.contains(detailInvoiceListNewDetailInvoice)) {
                    Book oldBookIdOfDetailInvoiceListNewDetailInvoice = detailInvoiceListNewDetailInvoice.getBookId();
                    detailInvoiceListNewDetailInvoice.setBookId(book);
                    detailInvoiceListNewDetailInvoice = em.merge(detailInvoiceListNewDetailInvoice);
                    if (oldBookIdOfDetailInvoiceListNewDetailInvoice != null && !oldBookIdOfDetailInvoiceListNewDetailInvoice.equals(book)) {
                        oldBookIdOfDetailInvoiceListNewDetailInvoice.getDetailInvoiceList().remove(detailInvoiceListNewDetailInvoice);
                        oldBookIdOfDetailInvoiceListNewDetailInvoice = em.merge(oldBookIdOfDetailInvoiceListNewDetailInvoice);
                    }
                }
            }
            for (Review reviewListNewReview : reviewListNew) {
                if (!reviewListOld.contains(reviewListNewReview)) {
                    Book oldBookIdOfReviewListNewReview = reviewListNewReview.getBookId();
                    reviewListNewReview.setBookId(book);
                    reviewListNewReview = em.merge(reviewListNewReview);
                    if (oldBookIdOfReviewListNewReview != null && !oldBookIdOfReviewListNewReview.equals(book)) {
                        oldBookIdOfReviewListNewReview.getReviewList().remove(reviewListNewReview);
                        oldBookIdOfReviewListNewReview = em.merge(oldBookIdOfReviewListNewReview);
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
                Integer id = book.getBookId();
                if (findBook(id) == null) {
                    throw new NonexistentEntityException("The book with id " + id + " no longer exists.");
                }
            }
            throw ex;        
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();
            Book book;
            try {
                book = em.getReference(Book.class, id);
                book.getBookId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The book with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<DetailInvoice> detailInvoiceListOrphanCheck = book.getDetailInvoiceList();
            for (DetailInvoice detailInvoiceListOrphanCheckDetailInvoice : detailInvoiceListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Book (" + book + ") cannot be destroyed since the DetailInvoice " + detailInvoiceListOrphanCheckDetailInvoice + " in its detailInvoiceList field has a non-nullable bookId field.");
            }
            List<Review> reviewListOrphanCheck = book.getReviewList();
            for (Review reviewListOrphanCheckReview : reviewListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Book (" + book + ") cannot be destroyed since the Review " + reviewListOrphanCheckReview + " in its reviewList field has a non-nullable bookId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Publisher publisherId = book.getPublisherId();
            if (publisherId != null) {
                publisherId.getBookList().remove(book);
                publisherId = em.merge(publisherId);
            }
            EbookFormat formatId = book.getFormatId();
            if (formatId != null) {
                formatId.getBookList().remove(book);
                formatId = em.merge(formatId);
            }
            List<Author> authorList = book.getAuthorList();
            for (Author authorListAuthor : authorList) {
                authorListAuthor.getBookList().remove(book);
                authorListAuthor = em.merge(authorListAuthor);
            }
            List<Genre> genreList = book.getGenreList();
            for (Genre genreListGenre : genreList) {
                genreListGenre.getBookList().remove(book);
                genreListGenre = em.merge(genreListGenre);
            }
            em.remove(book);
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

    public List<Book> findBookEntities() {
        return findBookEntities(true, -1, -1);
    }

    public List<Book> findBookEntities(int maxResults, int firstResult) {
        return findBookEntities(false, maxResults, firstResult);
    }

    private List<Book> findBookEntities(boolean all, int maxResults, int firstResult)
    {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Book.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
    }

    public Book findBook(Integer id)
    {
        return em.find(Book.class, id);
    }

    public int getBookCount()
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Book> rt = cq.from(Book.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();  
    }
}
