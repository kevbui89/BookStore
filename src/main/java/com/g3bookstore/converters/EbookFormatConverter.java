package com.g3bookstore.converters;

import com.g3bookstore.customcontrollers.CustomEbookFormatJpaController;
import com.g3bookstore.entities.EbookFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

/**
 *
 * @author lebed
 */
@FacesConverter(value = "ebookFormatConverter")
public class EbookFormatConverter implements Converter {

    private final static Logger log = Logger.getLogger(EbookFormatConverter.class.getName());

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                EbookFormat format = new EbookFormat();
                
                log.log(Level.INFO, "Format OBJ:{0}", format);
                log.log(Level.INFO, "Value of the converter:{0}", value);
                
                format.setFormatId(Integer.parseInt(value));
                return format;
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
            return String.valueOf(((EbookFormat) value).getFormatId());
        } else {
            return null;
        }
    }

}
