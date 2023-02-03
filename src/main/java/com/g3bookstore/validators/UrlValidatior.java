package com.g3bookstore.validators;

import com.g3bookstore.util.MessageProvider;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;

/**
 * Allow validate an url length and if it is valid
 * 
 * @author Denis Lebedev
 */
@Named("urlValidator")
@RequestScoped
public class UrlValidatior {

    private MessageProvider bundle = new MessageProvider();
    private final Logger log = Logger.getLogger(ClientValidators.class.getName());
    private final int URLLENGTH = 200;
    
    /**
     * Validate length and if the url is valid
     * 
     * @param fc
     * @param c
     * @param value 
     */
    public void validateUrl(FacesContext fc, UIComponent c, Object value) {
        String input = ((String) value).trim();
        
        if(input.length() > URLLENGTH)
            throw new ValidatorException(new FacesMessage(bundle.getValue("validate_ad_url")));
        
        try {
            URL url = new URL(input);
            URLConnection conn = url.openConnection();
            conn.connect();
        } catch (IOException  e) {
            throw new ValidatorException(new FacesMessage(bundle.getValue("error_url")));
        }
    }
}
