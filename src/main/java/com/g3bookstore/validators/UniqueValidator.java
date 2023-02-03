package com.g3bookstore.validators;

import com.g3bookstore.customcontrollers.CustomAuthorJpaController;
import com.g3bookstore.customcontrollers.CustomBookJpaController;
import com.g3bookstore.customcontrollers.CustomGenreJpaController;
import com.g3bookstore.customcontrollers.CustomPublisherJpaController;
import com.g3bookstore.util.MessageProvider;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Allow to validate if the given string is unique for the given category. Also
 * provide an alternative to validate uniqueness and the length together since
 * they are highly tight
 *
 * @author Denis Lebedev
 */
@Named("uniqueValidator")
@RequestScoped
public class UniqueValidator {

    private MessageProvider bundle = new MessageProvider();
    private final static Logger log = Logger.getLogger(UniqueValidator.class.getName());
    private final int GENRELENGTH = 40;
    private final int AUTHORLENGTH = 130;
    private final int PUBLENGTH = 80;
    private final int ISBNLENGTH = 10;

    @Inject
    private CustomAuthorJpaController authorC;

    @Inject
    private CustomBookJpaController bookC;

    @Inject
    private CustomGenreJpaController genreC;

    @Inject
    private CustomPublisherJpaController pubC;
    
    /**
     * Validate that the input is a unique author name.
     * 
     * @param fc
     * @param c
     * @param value 
     */
    public void validateAuthorName(FacesContext fc, UIComponent c, Object value) {
        String input = ((String) value).trim();
        if (authorC.getAuthorsByExactName(input).size() == 1) {
            throw new ValidatorException(new FacesMessage(bundle.getValue("unique_author")));
        }
    }
    
    /**
     * Validate that the author name is validate and in range.
     * 
     * @param fc
     * @param c
     * @param value 
     */
    public void validateAuthorNameAndRange(FacesContext fc, UIComponent c, Object value) {
        String input = ((String) value).trim();

        if (input.length() > AUTHORLENGTH) {
            throw new ValidatorException(new FacesMessage(bundle.getValue("validation_author")));
        }
        
        if(!input.contains("^[aA-zZ]{1,}[a-zA-Z ]*$"))
            throw new ValidatorException(new FacesMessage(bundle.getValue("author_regex")));
            
        if (authorC.getAuthorsByExactName(input).size() == 1) {
            throw new ValidatorException(new FacesMessage(bundle.getValue("unique_author")));
        }
    }
    
    /**
     * Validate that the book isbn is unique.
     * 
     * @param fc
     * @param c
     * @param value 
     */
    public void validateBookIsbn(FacesContext fc, UIComponent c, Object value) {
        String input = ((String) value).trim();
        if (bookC.getBookByExactIsbn(input).size() == 1) {
            throw new ValidatorException(new FacesMessage(bundle.getValue("unique_isbn")));
        }
    }
    
    /**
     * Validate that the book isbn is right, unique, and respect the range.
     * 
     * @param fc
     * @param c
     * @param value 
     */
    public void validateBookIsbnNameAndRange(FacesContext fc, UIComponent c, Object value) {
        String input = ((String) value).trim();

        if (input.length() > ISBNLENGTH || !input.matches("^[aA-zZ0-9]+$")) {
            throw new ValidatorException(new FacesMessage(bundle.getValue("validation_isnb")));
        }

        if (bookC.getBookByExactIsbn(input).size() == 1) {
            throw new ValidatorException(new FacesMessage(bundle.getValue("unique_isbn")));
        }
    }
    
    /**
     * Validate that the genre name is unique.
     * 
     * @param fc
     * @param c
     * @param value 
     */
    public void validateGenreName(FacesContext fc, UIComponent c, Object value) {
        String input = ((String) value).trim();
        if (genreC.getGenreByExactName(input).size() == 1) {
            throw new ValidatorException(new FacesMessage(bundle.getValue("unique_genre")));
        }

    }
    
    /**
     * Validate that the genre is unique and in a valid range.
     * 
     * @param fc
     * @param c
     * @param value 
     */
    public void validateGenreNameAndRange(FacesContext fc, UIComponent c, Object value) {
        String input = ((String) value).trim();

        if (input.length() > GENRELENGTH) {
            throw new ValidatorException(new FacesMessage(bundle.getValue("validation_genre")));
        }
        
         if(!input.contains("^[aA-zZ]{1,}[a-zA-Z ]*$"))
            throw new ValidatorException(new FacesMessage(bundle.getValue("genre_regex")));

        if (genreC.getGenreByExactName(input).size() == 1) {
            throw new ValidatorException(new FacesMessage(bundle.getValue("unique_genre")));
        }
    }
    
    /**
     * Validate that the publisher name is unique.
     * 
     * @param fc
     * @param c
     * @param value 
     */
    public void validatePublisherName(FacesContext fc, UIComponent c, Object value) {
        String input = ((String) value).trim();
        if (pubC.getPublisherByExactName(input).size() == 1) {
            throw new ValidatorException(new FacesMessage(bundle.getValue("unique_publisher")));
        }

    }
    
    /**
     * Validate that the publisher name is unique and in a valid range.
     * 
     * @param fc
     * @param c
     * @param value 
     */
    public void validatePublisherNameAndRange(FacesContext fc, UIComponent c, Object value) {
        String input = ((String) value).trim();

        if (input.length() > PUBLENGTH) {
            throw new ValidatorException(new FacesMessage(bundle.getValue("validation_pub")));
        }

        if (pubC.getPublisherByExactName(input).size() == 1) {
            throw new ValidatorException(new FacesMessage(bundle.getValue("unique_publisher")));
        }
    }
}
