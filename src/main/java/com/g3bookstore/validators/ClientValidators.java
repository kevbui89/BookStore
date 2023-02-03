package com.g3bookstore.validators;

import com.g3bookstore.util.MessageProvider;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;

/**
 *
 * @author Werner castanaza, Kevin Bui
 *
 */
@Named("ClientValidators")
@RequestScoped
public class ClientValidators implements Serializable {

    MessageProvider bundle = new MessageProvider();
    private String msg = "";
    private final Logger log = Logger.getLogger(ClientValidators.class.getName());

    private static final String EMAIL_REGEX = "(?:[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-zA-Z0-9-]*[a-zA-Z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    private static final String USERNAME_ADDRESS = "^[a-zA-Z0-9_\\- ]{6,35}$";
    private static final String USERNAME_ADDRESS2 = "^[a-zA-Z0-9_\\- ]*";
    private static final String LETTERS = "^[a-zA-Z]*$";
    private static final String NAME = "^[A-Za-z]+((\\s)?((\\'|\\-|\\.)?([A-Za-z])+)){2,35}$";
    private static final String POSTALCODE = "^[A-Za-z]\\d[A-Za-z][ -]?\\d[A-Za-z]\\d$";
    private static final String PHONENUMBER = "^([+]?[\\d]+)?$";
    private static final String PASSWORD = "^[a-zA-Z0-9].{7,35}$";

    /**
     * Validation for an email input.
     *
     * @param fc
     * @param c
     * @param value
     */
    public void validateEmail(FacesContext fc, UIComponent c, Object value) {
        String input = ((String) value).trim();
        if (!input.matches(EMAIL_REGEX)) {
            msg = bundle.getValue("error_email");
            log.log(Level.INFO, "Invalid email regex.");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

    /**
     * Validation for a username input
     *
     * @param fc
     * @param c
     * @param value
     */
    public void validateUsername(FacesContext fc, UIComponent c, Object value) {
        String input = ((String) value).trim();
        if (!input.matches(USERNAME_ADDRESS)) {
            msg = bundle.getValue("error_username");
            log.log(Level.INFO, "Invalid username regex.");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

    /**
     * Validation for a title input.
     *
     * @param fc
     * @param c
     * @param value
     */
    public void validateTitle(FacesContext fc, UIComponent c, Object value) {
        String input = ((String) value).replace(".", " ").trim();
        if (!input.matches(LETTERS) && input.length() < 9) {
            msg = bundle.getValue("error_title");
            log.log(Level.INFO, "Invalid title regex.");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

    /**
     * Validation for a name input
     *
     * @param fc
     * @param c
     * @param value
     */
    public void validateName(FacesContext fc, UIComponent c, Object value) {
        String input = ((String) value).replace(".", " ").trim();
        if (!input.matches(NAME)) {
            msg = bundle.getValue("error_name");
            log.log(Level.INFO, "Invalid first/last name regex.");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

    public void validateAddress(FacesContext fc, UIComponent c, Object value) {
        String input = ((String) value).trim();
        if (!input.matches(USERNAME_ADDRESS)) {
            msg = bundle.getValue("error_address");
            log.log(Level.INFO, "Invalid address regex.");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }
    

    public void validateAddress2(FacesContext fc, UIComponent c, Object value) {
        String input = ((String) value).trim();
        if (!input.matches(USERNAME_ADDRESS2)) {
            msg = bundle.getValue("error_address");
            log.log(Level.INFO, "Invalid address regex.");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

    /**
     * VAlidation for a postal code input.
     *
     * @param fc
     * @param c
     * @param value
     */
    public void validatePostalcode(FacesContext fc, UIComponent c, Object value) {
        String input = ((String) value).replace(" ", "").trim();
        if (!input.matches(POSTALCODE)) {
            msg = bundle.getValue("error_postalcode");
            log.log(Level.INFO, "Invalid postal code regex.");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

    /**
     * Validation for a namer input.
     *
     * @param fc
     * @param c
     * @param value
     */
    public void validateNumber(FacesContext fc, UIComponent c, Object value) {
        String input = ((String) value).trim();
        input = input.replace("-", "").replace(" ", "").replace("(", "").replace(")", "");
        if (!input.matches(PHONENUMBER)) {
            msg = bundle.getValue("error_number");
            log.log(Level.INFO, "Invalid number regex.");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

    /**
     * Validation for the password.
     *
     * @param fc
     * @param c
     * @param value
     */
    public void validatePassword(FacesContext fc, UIComponent c, Object value) {
        String input = ((String) value).trim();
        if (!input.matches(PASSWORD)) {
            msg = bundle.getValue("error_password");
            log.log(Level.INFO, "Invalid password regex.");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }
}
