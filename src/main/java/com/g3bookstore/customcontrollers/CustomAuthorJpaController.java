package com.g3bookstore.customcontrollers;

import com.g3bookstore.controllers.AuthorJpaController;
import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import com.g3bookstore.entities.Author;
import com.g3bookstore.entities.Author_;
import com.g3bookstore.entities.Book;
import com.g3bookstore.entities.Book_;
import static com.g3bookstore.entities.Client_.email;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Custom controller for Authors.
 *
 * @author Kevin Bui
 */
@Named("theAuthor")
@RequestScoped
public class CustomAuthorJpaController implements Serializable {

    @Inject
    private AuthorJpaController authorJpaController;

    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em = null;

    /**
     * Method which will return all authors in the database
     *
     * @return
     */
    public List<Author> getAuthors() {
        return authorJpaController.findAuthorEntities();
    }
    
    /**
     * Allow to create an author
     * 
     * @param author
     * @throws Exception 
     */
    public void create(Author author) throws Exception {
        authorJpaController.create(author);
    }
    
    /**
     * Allow to update an Author
     * 
     * @param author
     * @throws RollbackFailureException
     * @throws Exception 
     */
    public void update(Author author) throws RollbackFailureException, Exception {
        authorJpaController.edit(author);
    }
    
    /**
     * Allow to delete an Author
     * 
     * @param id
     * @throws RollbackFailureException
     * @throws Exception 
     */
    public void delete(Integer id) throws RollbackFailureException, Exception {
        authorJpaController.destroy(id);
    }
    
    /**
     * Method which will find a specific author by ID
     *
     * @param id
     * @return
     */
    public Author getAuthorByID(int id) {
        return authorJpaController.findAuthor(id);
    }

    /**
     * Method which will search through the author table and return all the
     * books from a specific author.
     *
     * @param author The author object
     * @return The book objects
     */
    public List<Book> getAllBooksByAuthor(Author author) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Book> cq = cb.createQuery(Book.class);
        Root<Book> books = cq.from(Book.class);
        cq.select(books);
        cq.where(cb.equal(books.get(Book_.authorList), author));

        Query q = em.createQuery(cq);
        return ((List<Book>) q.getResultList());
    }

    /**
     * Method will search through the book tables and return the authors for a
     * specific book
     *
     * @param book The book object
     * @return
     */
    public List<Author> getAuthorsForBook(Book book) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Book> cq = cb.createQuery(Book.class);
        Root<Book> books = cq.from(Book.class);
        cq.select(books);
        cq.where(cb.equal(books.get(Book_.authorList), book));

        Query q = em.createQuery(cq);
        return ((List<Author>) q.getResultList());
    }
    
    /**
     * Allow to find an author by the given name if only it is exactly the same.
     * 
     * @param author
     * @return 
     */
    public List<Author> getAuthorsByExactName(String author) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Author> cq = cb.createQuery(Author.class);
        Root<Author> root = cq.from(Author.class);
        cq.select(root);
        cq.where(cb.equal(root.get(Author_.name), author));

        Query q = em.createQuery(cq);
        return ((List<Author>)q.getResultList());
    }
}
