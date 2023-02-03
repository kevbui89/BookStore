package com.g3bookstore.customcontrollers;

import com.g3bookstore.controllers.AuthorJpaController;
import com.g3bookstore.controllers.BookJpaController;
import com.g3bookstore.controllers.GenreJpaController;
import com.g3bookstore.controllers.exceptions.NonexistentEntityException;
import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import com.g3bookstore.entities.Author;
import com.g3bookstore.entities.Author_;
import com.g3bookstore.entities.Book;
import com.g3bookstore.entities.Book_;
import com.g3bookstore.entities.Genre;
import com.g3bookstore.entities.Genre_;
import com.g3bookstore.entities.Publisher;
import com.g3bookstore.entities.Publisher_;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;

/**
 * Custom controller for Books.
 *
 * @author Alessandro Ciotola, Kevin Bui, Denis Lebedev
 */
@Named("theBook")
@RequestScoped
public class CustomBookJpaController implements Serializable {

    @Inject
    private BookJpaController bookJpaController;

    @Inject
    private GenreJpaController genreJpaController;

    @Inject
    private AuthorJpaController authorJpaController;

    private Logger log = Logger.getLogger(CustomBookJpaController.class.getName());

    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em = null;

    /**
     * Allow to create a Book entity.
     *
     * @param book
     * @throws Exception
     */
    public void create(Book book) throws Exception {
        bookJpaController.create(book);
    }

    /**
     * Allow to update a Book entity.
     *
     * @param book
     * @throws NonexistentEntityException
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void update(Book book) throws NonexistentEntityException, RollbackFailureException, Exception {
        bookJpaController.edit(book);
    }

    /**
     * Allow to delete a Book entity
     *
     * @param id
     * @throws NonexistentEntityException
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void delete(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        bookJpaController.destroy(id);
    }

    /**
     * Method which will return all books in the database
     *
     * @return
     */
    public List<Book> getBooks() {
        return bookJpaController.findBookEntities();
    }

    /**
     * Method which will find a specific book by ID
     *
     * @param id
     * @return
     */
    public Book getBookByID(int id) {
        return bookJpaController.findBook(id);
    }

    /**
     * Returns a range of books from the database
     *
     * @param maxResult
     * @param startResult
     * @return
     */
    public List<Book> getSpecificRangeBooks(int maxResult, int startResult) {
        return (List<Book>) bookJpaController.findBookEntities(maxResult, startResult);
    }

    /**
     * Method which will search through the book table for books using the title
     * of the book.
     *
     * @param title
     * @return The Book objects
     */
    public List<Book> getBookByTitle(String title) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Book> cq = cb.createQuery(Book.class);
        Root<Book> book = cq.from(Book.class);
        cq.select(book);
        cq.where(cb.like(book.get(Book_.title), "%" + title + "%"));

        Query q = em.createQuery(cq);
        return ((List<Book>) q.getResultList());
    }

    /**
     * Method which will search through the book table for books using the
     * author of the book.
     *
     * @param authorName
     * @return The Book 1
     */
    public List<Book> getBookByAuthor(String authorName) {
        List<Book> listBooks = new ArrayList<Book>();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Author> cq = cb.createQuery(Author.class);

        Root<Author> author = cq.from(Author.class);
        cq.select(author);
        cq.where(cb.like(author.get(Author_.name), "%" + authorName + "%"));

        Query q = em.createQuery(cq);
        List<Author> listAuthorNewAuthor = (List<Author>) q.getResultList();

        for (Author a : listAuthorNewAuthor) {
            for (Book b : a.getBookList()) {
                listBooks.add(b);
            }
        }

        return listBooks;
    }

    /**
     * Method which will search through the book table for books using the
     * publisher(s) of the book.
     *
     * @param publisherName
     * @return The Book objects
     */
    public List<Book> getBookByPublisher(String publisherName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Publisher> cq = cb.createQuery(Publisher.class);

        Root<Publisher> publisher = cq.from(Publisher.class);
        cq.select(publisher);
        cq.where(cb.like(publisher.get(Publisher_.publisher), "%" + publisherName + "%"));

        Query q = em.createQuery(cq);
        List<Publisher> publishers = (List<Publisher>) q.getResultList();
        List<Book> pubBooks = new ArrayList<Book>();

        for (Publisher p : publishers) {
            List<Book> books = p.getBookList();
            for (int i = 0; i < books.size(); i++) {
                pubBooks.add(books.get(i));
            }
        }

        return pubBooks;
    }

    /**
     * Method which will search through the book table for books using the isbn
     * of the book.
     *
     * @param isbn
     * @return The Book objects
     */
    public List<Book> getBookByIsbn(String isbn) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Book> cq = cb.createQuery(Book.class);
        Root<Book> book = cq.from(Book.class);
        cq.select(book);
        cq.where(cb.like(book.get(Book_.isbn), "%" + isbn + "%"));

        Query q = em.createQuery(cq);
        return ((List<Book>) q.getResultList());
    }

    /**
     * Method which will search through the book table for books using the total
     * sales of the book.
     *
     * @return The Book objects
     */
    public List<Book> getBookByTotalSales() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Book> cq = cb.createQuery(Book.class);
        Root<Book> book = cq.from(Book.class);
        cq.select(book);
        cq.orderBy(cb.desc(book.get(Book_.totalSale)));

        Query q = em.createQuery(cq);
        return ((List<Book>) q.getResultList());
    }

    /**
     * Method which will search through the book table for books that haven't
     * been sold
     *
     * @return The Book objects
     */
    public List<Book> getBooksNotSold() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Book> cq = cb.createQuery(Book.class);
        Root<Book> book = cq.from(Book.class);
        cq.select(book);
        cq.where(cb.equal(book.get(Book_.totalSale), 0));

        Query q = em.createQuery(cq);
        return ((List<Book>) q.getResultList());
    }

    /**
     * Method which will search through the book table for books that are on
     * sale.
     *
     * @return The Book objects
     */
    public List<Book> getBooksOnSale() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Book> cq = cb.createQuery(Book.class);
        Root<Book> book = cq.from(Book.class);
        cq.select(book);
        cq.where(cb.notEqual(book.get(Book_.salePrice), 0));

        Query q = em.createQuery(cq);
        return ((List<Book>) q.getResultList());
    }

    /**
     * Returns the list of books on sale depending on the size wanted
     *
     * @param size The amount of books on sale to display
     * @return The list of books on sale
     */
    public List<Book> getBooksOnSale(int size) {
        List<Book> list = getBooksOnSale();
        Collections.shuffle(list);

        if (list.size() < 10) {
            return list;
        }

        List<Book> newList = new ArrayList<Book>();
        for (int i = 0; i < size; i++) {
            newList.add(list.get(i));
        }

        return newList;
    }

    /**
     * Method which will search through the book table for books by genres
     *
     * @return The Book objects
     */
    public List<Book> getBooksByGenre(Genre g) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Book> cq = cb.createQuery(Book.class);
        Root<Book> root = cq.from(Book.class);
        Join<Book, Genre> genreJ = root.join(Book_.genreList);
        cq.select(root);
        cq.where(cb.equal(genreJ.get(Genre_.genreId), g.getGenreId()));

        Query q = em.createQuery(cq);
        return q.getResultList();
    }

    public List<Book> test(Genre g) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Book> cq = cb.createQuery(Book.class);
        Root<Book> root = cq.from(Book.class);
        Join<Book, Genre> genreJ = root.join(Book_.genreList);
        cq.select(root);
        cq.where(cb.equal(genreJ.get(Genre_.genreId), g.getGenreId()));

        Query q = em.createQuery(cq);
        return q.getResultList();
    }

    /**
     * Returns a list of similar books to the searched book
     *
     * @param g The genre
     * @return The list of books objects
     */
    public List<Book> getSimilarGenreBooks(Book b) {
        List<Genre> genres = b.getGenreList();
        List<Integer> listIds = new ArrayList<Integer>();
        for (Genre g : genres) {
            listIds.add(g.getGenreId());
        }

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Book> query = cb.createQuery(Book.class);
        Root<Book> book = query.from(Book.class);
        query.select(book);
        query.where(cb
                .and(
                        cb.isMember(b.getGenreList().get(0), book.get(Book_.genreList)),
                        cb.notEqual(book.get(Book_.bookId), b.getBookId())
                )
        );

        List<Book> similarBooks = em.createQuery(query).getResultList();

        // Fills up the similar book array if the book has multiple genres
        if (similarBooks.size() < 4 && genres.size() > 1) {
            similarBooks = fillUpSimilarGenreBooks(genres, similarBooks, listIds, b);
            return getRandomBooks(similarBooks, 4);
        } 

        return getRandomBooks(similarBooks, 4);
    }

    /**
     * Returns a list of similar books to the searched book
     *
     * @param b The book object
     * @return The list of books objects
     */
    public List<Book> getSimilarAuthorBooks(Book b) {

        List<Author> bookAuthors = b.getAuthorList();
        List<String> bookAuthorsStr = new ArrayList<String>();
        for (Author a : bookAuthors) {
            bookAuthorsStr.add(a.getName());
        }

        List<Book> sBooks = new ArrayList<Book>();
        for (int i = 0; i < bookAuthorsStr.size(); i++) {
            List<Book> temp = getBookByAuthor(bookAuthorsStr.get(i));
            for (int k = 0; k < temp.size(); k++) {
                sBooks.add(temp.get(k));
            }
        }

        // Check for duplicates
        for (int i = 0; i < sBooks.size(); i++) {
            if (sBooks.get(i).getBookId() == b.getBookId()) {
                sBooks.remove(i);
            }
        }

        List<Author> authors = authorJpaController.findAuthorEntities();
        List<Integer> listIds = new ArrayList<Integer>();
        for (Author a : authors) {
            listIds.add(a.getAuthorId());
        }

        return getRandomBooks(sBooks, 4);
    }

    /**
     * Selects books based on the date added to the book store
     *
     * @param count amount of books to return
     * @return The list of books
     */
    public List<Book> getMostRecentBooks(int count) {
        if (count < 1) {
            return null;
        }

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Book> cq = cb.createQuery(Book.class);
        Root<Book> root = cq.from(Book.class);
        cq.select(root);
        cq.orderBy(cb.desc(root.get("inventoryDate")));
        return em.createQuery(cq).setMaxResults(count).getResultList();
    }

    /**
     * Returns all the books from a specific keyword
     *
     * @param keyword The search keyword
     * @return The list of all books by keyword
     */
    public List<Book> getAllBooksByKeyword(String keyword) {
        List<Book> allBooksByKeyword = new ArrayList<Book>();
        Set<Book> bookSet = new HashSet<Book>();

        // Get the books from all 4 premade queries
        List<Book> byAuthors = getBookByAuthor(keyword);
        List<Book> byPublishers = getBookByPublisher(keyword);
        List<Book> byISBN = getBookByIsbn(keyword);
        List<Book> byTitle = getBookByTitle(keyword);

        for (int i = 0; i < byAuthors.size(); i++) {
            allBooksByKeyword.add(byAuthors.get(i));
        }
        for (int i = 0; i < byPublishers.size(); i++) {
            allBooksByKeyword.add(byPublishers.get(i));
        }
        for (int i = 0; i < byISBN.size(); i++) {
            allBooksByKeyword.add(byISBN.get(i));
        }
        for (int i = 0; i < byTitle.size(); i++) {
            allBooksByKeyword.add(byTitle.get(i));
        }

        bookSet.addAll(allBooksByKeyword);
        allBooksByKeyword.clear();
        allBooksByKeyword.addAll(bookSet);

        return allBooksByKeyword;
    }

    /**
     * Returns an array of book of similar genre of the specified book. If the
     * list of similar books by genre is below 3, we will find other books in a
     * random different genre to fill up the three spots.
     *
     * @param genres The list of genres
     * @param list The list of books
     * @param ids The list of genre ids
     * @return The list of book objects
     */
    private List<Book> fillUpSimilarGenreBooks(List<Genre> genres, List<Book> list, List<Integer> ids, Book b) {

        List<Book> otherBooks = new ArrayList<Book>();
        // Check if the book is the same as the one displayed on the book page
        int differentId = b.getBookId();

        for (Genre g : genres) {
            otherBooks = getBooksByGenre(g);
            for (int i = 0; i < otherBooks.size(); i++) {
                if (otherBooks.get(i).getBookId() != differentId) {
                    list.add(otherBooks.get(i));
                }
            }
        }
        // Get random genre if the size is still less than 3 after the loop
        if (list.size() < 3) {
            otherBooks = getBooksByGenre(genres.get(getRandomId(ids, "Genre")));
            for (int i = 0; i < otherBooks.size(); i++) {
                if (otherBooks.get(i).getBookId() != differentId) {
                    list.add(otherBooks.get(i));
                }
            }
        }

        return list;
    }

    /**
     * Returns a random category
     *
     * @param category The cagegory not to choose from
     * @param type The type of the size
     * @return
     */
    private int getRandomId(List<Integer> list, String type) {
        int categorySize = genreJpaController.getGenreCount();
        int authorSize = authorJpaController.getAuthorCount();

        log.log(Level.INFO, "Data>>>{0} The author size: ", authorSize);

        boolean invalid = true;
        int randomId = 0;
        int size;

        switch (type) {
            case "Genre":
                size = categorySize;
                break;
            case "Author":
                size = authorSize;
                break;
            default:
                size = 0;
                break;
        }

        while (invalid) {
            randomId = (int) (Math.random() * size) + 1;
            for (int i = 0; i < list.size(); i++) {
                if (!list.contains(randomId)) {
                    invalid = false;
                }
            }
        }
        return randomId;
    }

    /**
     * Returns random books from a list
     *
     * @param books
     * @return
     */
    private List<Book> getRandomBooks(List<Book> books, int size) {

        if (books.size() < size) {
            size = books.size();
        }

        Collections.shuffle(books);

        List<Book> randomBooks = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            randomBooks.add(books.get(i));
        }

        return randomBooks;
    }
    
    /**
     * Allow to find a book by the given isbn if only it is exactly the same.
     * 
     * @param isbn
     * @return 
     */
    public List<Book> getBookByExactIsbn(String isbn) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Book> cq = cb.createQuery(Book.class);
        Root<Book> root = cq.from(Book.class);
        cq.select(root);
        cq.where(cb.equal(root.get(Book_.isbn), isbn));

        Query q = em.createQuery(cq);
        return ((List<Book>)q.getResultList());
    }
}
