package com.g3bookstore.backingbeans;

import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import com.g3bookstore.customcontrollers.CustomBookJpaController;
import com.g3bookstore.customcontrollers.CustomEbookFormatJpaController;
import com.g3bookstore.customcontrollers.CustomPublisherJpaController;
import com.g3bookstore.entities.Book;
import com.g3bookstore.util.MessageProvider;
import com.g3bookstore.util.UploadImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.UploadedFile;

/**
 * This class allow to update a book safely and respect validation.
 *
 * @author Denis Lebedev
 */
@Named("updateBook")
@SessionScoped
public class UpdateBookBackingBean implements Serializable {

    private static final Logger log = Logger.getLogger(UpdateBookBackingBean.class.getName());

    private UploadImage upload = new UploadImage();

    @Inject
    private CustomBookJpaController bookController;

    @Inject
    private CustomPublisherJpaController pubController;

    @Inject
    private CustomEbookFormatJpaController formatController;

    private MessageProvider bundle = new MessageProvider();
    private Book book;
    private String oldIsbn;

    public Book getBook() {
        if (book == null) {
            book = new Book();
        }
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    /**
     * Control the flow of process of the Wizard.
     *
     * @param event
     * @return
     */
    public String onFlowProcess(FlowEvent event) {
        String current = event.getOldStep();

        if (current.equals("singleBook") && (book == null || book.getBookId() == null)) {
            log.log(Level.INFO, "A book wasnt selected");
            FacesContext.getCurrentInstance().addMessage("singleBook", new FacesMessage(bundle.getValue("required_bookU")));
            return current;
        } else if (current.equals("singleBook") && (book != null || book.getBookId() != null)) {
            log.log(Level.INFO, "logged the old isbn");
            //Saving the isbn to avoid uniqueness issue where this is id would exist.
            oldIsbn = book.getIsbn();
        } else if (current.equals("simpleDetailU") && !oldIsbn.equals(book.getIsbn())) {
            //The isbn changed we look for uniqueness now
            if (bookController.getBookByExactIsbn(book.getIsbn()).size() == 1) {
                log.log(Level.INFO, "New isbn incoming:{0}", book.getIsbn());
                FacesContext.getCurrentInstance().addMessage("genreInfoU", new FacesMessage(bundle.getValue("unique_isbn")));
                return current;
            }

        } else if (current.equals("genreInfoU") && book.getGenreList().isEmpty()) {
            log.log(Level.INFO, "No genre were selected");
            FacesContext.getCurrentInstance().addMessage("genreInfoU", new FacesMessage(bundle.getValue("empty_selector")));
            return current;

        } else if (current.equals("authorInfoU") && book.getAuthorList().isEmpty()) {
            log.log(Level.INFO, "No author were selected");
            FacesContext.getCurrentInstance().addMessage("authorInfoU", new FacesMessage(bundle.getValue("empty_selector")));
            return current;

        } else if (current.equals("authorInfoU") && !book.getAuthorList().isEmpty()) {
            //Ensure that the elements exist before finishing
            fillObject();
        }

        return event.getNewStep();
    }

    /**
     * Update the entity with the new fields.
     *
     * @return
     * @throws RollbackFailureException
     * @throws Exception
     */
    public String updateBook() throws RollbackFailureException, Exception {
        bookController.update(book);
        return "manager.xhtml";
    }

    /**
     * Create and upload two images with specific size. One for the small cover
     * and one for the big cover.
     *
     * @param event
     * @throws IOException
     */
    public void handleFileUpload(FileUploadEvent event) throws IOException {

        UploadedFile uploadedF = event.getFile();
        try {
            upload.addBookCover(uploadedF, book);
        } catch (IOException e) {
            log.log(Level.SEVERE, "Reading image issue:{0}", e.getMessage());
        }
    }

    /**
     * Find the two object inside the database to ensure they exist and valid
     */
    private void fillObject() {
        if (book.getPublisherId() != null && book.getPublisherId().getPublisherId() != null) {

            log.log(Level.INFO, "New Author object with the id:{0}", book.getPublisherId().getPublisherId());
            book.setPublisherId(pubController.getPublisherByID(book.getPublisherId().getPublisherId()));
        }

        if (book.getFormatId() != null && book.getFormatId().getFormatId() != null) {

            log.log(Level.INFO, "New Author object with the id:{0}", book.getFormatId().getFormatId());
            book.setFormatId(formatController.getEbookFormatByID(book.getFormatId().getFormatId()));
        }
    }
}
