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
 * @author Werner castanaza
 *
 */
@Named("SalePriceValidator")
@RequestScoped
public class SalePriceValidators implements Serializable {

    MessageProvider bundle = new MessageProvider();
    private String msg = "";
    private static final Logger log = Logger.getLogger(SalePriceValidators.class.getName());

    private static final String CURRENCY = "[0-9]+([,.][0-9]{1,2})?";

    /**
     * Validation for currency.
     *
     * @param fc
     * @param c
     * @param value
     */
    public void validateCurrency(FacesContext fc, UIComponent c, Object value) {
        String input = ((String) (value + "")).trim();
        if (!input.matches(CURRENCY)) {
            msg = bundle.getValue("error_number");
            log.log(Level.INFO, "Invalid password regex.");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }
}
