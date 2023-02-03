package com.g3bookstore.customcontrollers;

import com.g3bookstore.controllers.GenreJpaController;
import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import com.g3bookstore.entities.Book;
import com.g3bookstore.entities.Book_;
import com.g3bookstore.entities.Genre;
import com.g3bookstore.entities.Genre_;
import java.io.Serializable;
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
 * Custom controller for Genres.
 *
 * @author Kevin Bui, Denis Lebedev
 */
@Named("theGenre")
@RequestScoped
public class CustomGenreJpaController implements Serializable {

    @Inject
    private GenreJpaController genreJpaController;
    
    private List<Book> booksOrderedBySales = new ArrayList<Book>();

    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em = null;

    /**
     * Method which will return all genres in the database
     *
     * @return
     */
    public List<Genre> getGenres() {
        return genreJpaController.findGenreEntities();
    }
    
    /**
     * Allow to create a genre
     * 
     * @param genre
     * @throws Exception 
     */
    public void create(Genre genre) throws Exception {
        genreJpaController.create(genre);
    }
    
    /**
     * Allow to update a genre
     * 
     * @param genre
     * @throws RollbackFailureException
     * @throws Exception 
     */
    public void update(Genre genre) throws RollbackFailureException, Exception {
        genreJpaController.edit(genre);
    }
    
    /**
     * Allow to delete a genre
     * 
     * @param id
     * @throws RollbackFailureException
     * @throws Exception 
     */
    public void delete(Integer id) throws RollbackFailureException, Exception {
        genreJpaController.destroy(id);
    }
    
    /**
     * Method which will find a specific genre by ID
     *
     * @param id
     * @return
     */
    public Genre getGenreByID(int id) {
        return genreJpaController.findGenre(id);
    }

    /**
     * Checks if the genre list is empty
     *
     * @return
     */
    public int isEmpty() {
        return genreJpaController.getGenreCount();
    }

    /**
     * Method which will search through the genre table and return all the books
     * in the specific genre.
     *
     * @param id The book object
     * @return The Review objects
     */
    public List<Book> getAllBooksByGenre(Genre g) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Book> cq = cb.createQuery(Book.class);
        Root<Book> books = cq.from(Book.class);
        cq.select(books);
        cq.where(cb.equal(books.get(Book_.genreList), g));

        Query q = em.createQuery(cq);
        return ((List<Book>) q.getResultList());
    }

    /**
     * Returns the top sellers of a genre
     *
     * @param g The genre
     * @return The list of book top sellers
     */
    public List<Book> getTopSellersByGenreDescending(Genre g) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Book> cq = cb.createQuery(Book.class);
        Root<Book> book = cq.from(Book.class);
        cq.select(book);
        cq.orderBy(cb.desc(book.get(Book_.totalSale)));
        cq.where(cb.equal(book.get(Book_.genreList), g));

        Query q = em.createQuery(cq);
        return (List<Book>) q.getResultList();
    }
    
    /**
     * Returns the top 5 seller by genre
     * 
     * @param g
     * @return 
     */
    public List<Book> getTopSellersByGenre(Genre g) {
        List<Book> allBooksDesc = getTopSellersByGenreDescending(g);
        
        if (allBooksDesc.size() < 5) {
            return allBooksDesc;
        }
        
        List<Book> topSales = new ArrayList<Book>();
        for (int i = 0; i < 5; i++) {
            topSales.add(allBooksDesc.get(i));
        }

        return topSales;
    }

    /**
     * Returns the top sellers of a genre
     *
     * @param g The genre
     * @return The list of book top sellers
     */
    public List<Book> getAllBooksAfterTopByGenre(Genre g) {
        List<Book> allBooksDesc = getTopSellersByGenreDescending(g);
        
        if (allBooksDesc.size() < 5) {
            return null;
        }

        List<Book> allBooks = new ArrayList<Book>();
        for (int i = 5; i < allBooksDesc.size(); i++) {
            allBooks.add(allBooksDesc.get(i));
        }

        return allBooks;
    }
    
    /**
     * Allow to find a genre by the given name if only it is exactly the same.
     * 
     * @param genre
     * @return 
     */
    public List<Genre> getGenreByExactName(String genre) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Genre> cq = cb.createQuery(Genre.class);
        Root<Genre> root = cq.from(Genre.class);
        cq.select(root);
        cq.where(cb.equal(root.get(Genre_.genre), genre));

        Query q = em.createQuery(cq);
        return ((List<Genre>)q.getResultList());
    }
}
