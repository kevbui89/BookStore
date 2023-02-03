package com.g3bookstore.converters;

import com.g3bookstore.entities.Publisher;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author lebed
 */
@FacesConverter(value = "publisherConverter")
public class PublisherConverter implements Converter {
    
    private final static Logger log = Logger.getLogger(PublisherConverter.class.getName());
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && value.trim().length()   > 0) {
            try {
                Publisher pub = new Publisher();
                
                log.log(Level.INFO, "Value inside the converter:{0}", value);
                
                pub.setPublisherId(Integer.parseInt(value));
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
