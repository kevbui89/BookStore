package com.g3bookstore.backingbeans;

import com.g3bookstore.customcontrollers.CustomBookJpaController;
import com.g3bookstore.entities.Book;
import com.g3bookstore.util.MessageProvider;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Responsible for backing search pages.
 *
 * @author Alessandro Ciotola, Kevin Bui
 * @version 13/02/2018
 * @since 1.8
 */
@Named("searchBackingBean")
@ManagedBean
@SessionScoped
public class SearchBackingBean implements Serializable {

    @Inject
    private CustomBookJpaController bookJpaController;

    private String choice = "";
    private String keyword = "";
    private String category = "";
    private int size = 0;
    private boolean generalSearch;

    /**
     * Returns the current search choice of the user.
     *
     * @return the current search choice of the user.
     */
    public String getChoice() {
        return choice;
    }

    /**
     * Returns the current keyword search of the user.
     *
     * @return the current keyword search of the user.
     */
    public String getKeyword() {
        return keyword;
    }
    
    /**
     * Returns the current category search of the user.
     * 
     * @return The current category search of the user.
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the current search choice of the user.
     *
     * @param choice
     */
    public void setChoice(String choice) {
        this.choice = choice;
    }

    /**
     * Sets the current keyword search of the user.
     *
     * @param keyword
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Method which returns the size of the list.
     *
     * @return
     */
    public int getListSize() {
        return size;
    }
    
    /**
     * Returns a list of all the book searched by keyword
     * 
     * @return 
     */
    public String generalSearch() {
        generalSearch = true;
        List<Book> bookList = bookJpaController.getAllBooksByKeyword(keyword);
        size = bookList.size();

        if (bookList.size() == 1) {
            return "book.xhtml?faces-redirect=true&bookid=" + bookList.get(0).getBookId();
        } else {
            return "search_results";
        }
    }

    /**
     * Fetches books based on user search keys and the chosen option.
     *
     * @return the book page if one book is found, else goes to the search page
     * results
     *
     */
    public String search() {
        generalSearch = false;
        List<Book> bookList = getSearchResults();
        size = bookList.size();

        if (bookList.size() == 1) {
            return "book.xhtml?faces-redirect=true&bookid=" + bookList.get(0).getBookId();
        } else {
            return "search_results";
        }
    }
    
    /**
     * Returns true if it is a general search, else false
     * 
     * @return 
     */
    public boolean checkGeneralSearch() {
        return generalSearch;
    }

    /**
     * Method which returns a list of books depending on what the user is trying
     * to search by.
     *
     * @return
     */
    public List<Book> getSearchResults() {
        List<Book> bookList;

        switch (choice) {
            case "Author":
                bookList = bookJpaController.getBookByAuthor(keyword);
                category = new MessageProvider().getValue("select_author");
                break;
            case "ISBN":
                bookList = bookJpaController.getBookByIsbn(keyword);
                category = new MessageProvider().getValue("isbn");
                break;
            case "Publisher":
                bookList = bookJpaController.getBookByPublisher(keyword);
                category = new MessageProvider().getValue("select_publisher");
                break;
            default:
                bookList = bookJpaController.getBookByTitle(keyword);
                category = new MessageProvider().getValue("select_title");
                break;
        }
        size = bookList.size();
        return bookList;
    }
    
    /**
     * Returns all the books by keyword
     * 
     * @return 
     */
    public List<Book> getGeneralSearchResults() {
        List<Book> bookList = bookJpaController.getAllBooksByKeyword(keyword);
        size = bookList.size();
        
        return bookList;
    }

}
