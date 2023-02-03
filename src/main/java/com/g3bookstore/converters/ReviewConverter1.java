package com.g3bookstore.converters;

import com.g3bookstore.entities.Publisher;
import com.g3bookstore.entities.Review;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Werner
 */
@FacesConverter(value = "reviewConverter")
public class ReviewConverter1 implements Converter {
    
    private final static Logger log = Logger.getLogger(ReviewConverter1.class.getName());
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        System.out.println("HELLO THERE MY GUY");
        if (value != null && value.trim().length()   > 0) {
            try {
                Review pub = new Review();
                
                log.log(Level.INFO, " what up my guy", value);
                
                boolean bool = Boolean.parseBoolean(value);
                if(bool) {
                    return null;
                }
                return pub;
            } catch (NumberFormatException e) {
                throw new ConverterException();
            }
        } else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null) {
            return String.valueOf(((Publisher) value).getPublisherId());
        } else {
            return null;
        }
    }

}
