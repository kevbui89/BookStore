package com.g3bookstore.validators;

import com.g3bookstore.util.MessageProvider;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;

/**
 * Class responsible for validating the credit card information.
 *
 * @author Alessandro Ciotola
 * @version 09/03/2018
 * @since 1.8
 */
@Named("cardValidators")
@RequestScoped
public class CreditCardValidators {

    private final Logger log = Logger.getLogger(CreditCardValidators.class.getName());
    MessageProvider bundle = new MessageProvider();
    private String msg = "";

    private static final String CARDSECURITY = "^[0-9]+";

    /**
     * Validation for the credit card number.
     * 
     * *Code taken from semester 3 Hotel project in Software Dev 3*
     *
     * @param fc
     * @param c
     * @param value
     */
    public void validateCreditCardNumber(FacesContext fc, UIComponent c, Object value) {
        String number = (value.toString()).trim();

        if (number.length() != 16 && number.length() != 15 || !number.matches(CARDSECURITY)) {
            throwCardException("error_cardNumber", "Invalid card number");
        }

        int[] temp = new int[number.length()];
        int[] resized = new int[number.length()];

        int intNumber = 0;
        String singleNumber;

        for (int start = 0; start < number.length(); start++) {
            if (start == number.length() - 1) {
                singleNumber = number.substring(start);
                intNumber = Integer.parseInt(singleNumber);                
            } 
            else if (start != number.length()) {
                int index = start + 1;
                singleNumber = number.substring(start, index);
                intNumber = Integer.parseInt(singleNumber);
            }
            temp[start] = intNumber;
        }

        for (int j = 0; j < resized.length; j++) {
            resized[j] = temp[j];
        }

        int total = 0;

        for (int odd = resized.length - 2; odd >= 0; odd = odd - 2) {
            resized[odd] = resized[odd] * 2;
        }

        for (int subtract = 0; subtract < resized.length; subtract++) {
            if (resized[subtract] > 9) {
                resized[subtract] = resized[subtract] - 9;
            }
        }

        for (int add = 0; add < resized.length; add++) {
            total += resized[add];
            if (add == resized.length - 1) {
                if (total % 10 != 0) {
                    throwCardException("error_cardNumber", "Invalid card number");
                }
            }
        }
    }

    /**
     * Validation for the credit card security number.
     *
     * @param fc
     * @param c
     * @param value
     */
    public void validateCreditCardSecurityNumber(FacesContext fc, UIComponent c, Object value) {
        String number = (value.toString()).trim();
        if (!number.matches(CARDSECURITY)) {
            throwCardException("error_cardSecurityNumber", "Invalid card number security regex.");
        }
    }

    /**
     * Method responsible for throwing the exceptions/displaying the error
     *
     * @param msg1
     * @param msg2
     */
    private void throwCardException(String msg1, String msg2) {
        msg = bundle.getValue(msg1);
        log.log(Level.INFO, msg2);
        throw new ValidatorException(new FacesMessage(msg));
    }
}
