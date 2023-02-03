package com.g3bookstore.backingbeans;

import com.g3bookstore.customcontrollers.CustomBookJpaController;
import com.g3bookstore.customcontrollers.CustomEbookFormatJpaController;
import com.g3bookstore.customcontrollers.CustomPublisherJpaController;
import com.g3bookstore.entities.Author;
import com.g3bookstore.entities.Book;
import com.g3bookstore.entities.EbookFormat;
import com.g3bookstore.entities.Genre;
import com.g3bookstore.entities.Publisher;
import com.g3bookstore.util.MessageProvider;
import com.g3bookstore.util.UploadImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.UploadedFile;

/**
 * This class ensure the interaction between the page and the creation of a
 * book. Also set default values for the book object.
 *
 * @author Denis Lebedev
 */
@Named("createBook")
@SessionScoped
public class CreateBookBackingBean implements Serializable {

    private static final Logger log = Logger.getLogger(CreateBookBackingBean.class.getName());

    @Inject
    private CustomBookJpaController bookController;

    @Inject
    private CustomEbookFormatJpaController formatController;

    @Inject
    private CustomPublisherJpaController pubController;

    private MessageProvider bundle = new MessageProvider();
    private UploadImage upload = new UploadImage();
    private UploadedFile uploadedF;
    
    private Book book;
    private Publisher publisher;
    private EbookFormat format;
    private List<Genre> genreList;
    private List<Author> authorList;
    private boolean uploaded;

    @PostConstruct
    public void init() {
        uploaded = false;
        genreList = new ArrayList();
        authorList = new ArrayList();
    }

    public void setSelectedGenre(List<Genre> genreList) {
        this.genreList = genreList;
    }

    public List<Genre> getSelectedGenre() {
        return genreList;
    }

    public void setSelectedAuthor(List<Author> authorList) {
        this.authorList = authorList;
    }

    public List<Author> getSelectedAuthor() {
        return authorList;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public EbookFormat getEbookFormat() {
        return format;
    }

    public void setEbookFormat(EbookFormat format) {
        this.format = format;
    }

    public Book getBook() {
        if (book == null) {
            book = new Book();
        }
        return book;
    }

    /**
     * Control the flow of process of the Wizard.
     *
     * @param event
     * @return
     */
    public String onFlowProcess(FlowEvent event) {

        String current = event.getOldStep();

        if (current.equals("genreInfo") && genreList.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage("genreInfo", new FacesMessage(bundle.getValue("empty_selector")));
            return current;

        } else if (current.equals("authorInfo") && authorList.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage("authorInfo", new FacesMessage(bundle.getValue("empty_selector")));
            return current;

        } else if (current.equals("authorInfo") && !authorList.isEmpty()) {
            //Ensure that the elements exist before finishing
            fillObject();
        } else if (current.equals("uploadImg") && !uploaded) {
            FacesContext.getCurrentInstance().addMessage("uploadImg", new FacesMessage(bundle.getValue("required_upload")));
            return current;
        }

        return event.getNewStep();
    }

    /**
     * Allow to create a book with given data.
     *
     * @return
     * @throws Exception
     */
    public String createBook() throws Exception {
        
        try {
            upload.addBookCover(uploadedF, book);
        } catch (IOException e) {
            log.log(Level.SEVERE, "Reading image issue:{0}", e.getMessage());
        }
        
        //Make sure default information are setted for the book
        book.setInventoryDate(new Date());
        book.setRemoved(false);
        book.setTotalSale(0);

        log.log(Level.INFO, "Current publisher id:{0}", publisher);
        log.log(Level.INFO, "Current format id:{0}", format);

        log.log(Level.INFO, "Selected authors:{0}", authorList);
        log.log(Level.INFO, "Selected genre{0}", genreList);

        book.setAuthorList(authorList);
        book.setGenreList(genreList);
        book.setPublisherId(publisher);
        book.setFormatId(format);
        bookController.create(book);

        return "manager.xhtml";
    }

    /**
     * Create and upload two images with specific size. One for the small cover
     * and one for the big cover.
     *
     * @param event
     */
    public void handleFileUpload(FileUploadEvent event) {
        uploaded = true;

        uploadedF = event.getFile();

    }

    /**
     * Find the object inside the database and fill them up with data.
     */
    private void fillObject() {
        if (publisher != null && publisher.getPublisherId() != null) {

            log.log(Level.INFO, "New Author object with the id:{0}", publisher.getPublisherId());
            publisher = pubController.getPublisherByID(publisher.getPublisherId());
        }

        if (format != null && format.getFormatId() != null) {

            log.log(Level.INFO, "New Author object with the id:{0}", format.getFormatId());
            format = formatController.getEbookFormatByID(format.getFormatId());
        }
    }
}
