package com.g3bookstore.converters;

import com.g3bookstore.entities.Author;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author lebed
 */
@FacesConverter("authorConverter")
public class AuthorConverter implements Converter {
    
    private final static Logger log = Logger.getLogger(AuthorConverter.class.getName());
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {

        if (value != null && value.trim().length() > 0) {

            try {
                Author auth = new Author();
                
                log.log(Level.INFO, "Author OBJ:{0}", auth);
                log.log(Level.INFO, "Value of the converter:{0}", value);
                
                auth.setAuthorId(Integer.parseInt(value));
                return auth;
            } catch (NumberFormatException e) {

            }
        } else {
            return null;
        }
        
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null) {
            return String.valueOf(((Author) value).getAuthorId());
        } else {
            return null;
        }
    }

}
