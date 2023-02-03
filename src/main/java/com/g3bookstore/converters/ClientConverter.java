package com.g3bookstore.converters;

import com.g3bookstore.entities.Client;
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
@FacesConverter(value = "clientConverter")
public class ClientConverter implements Converter {

    private final Logger log = Logger.getLogger(ClientConverter.class.getName());
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                Client client = new Client();
                
                log.log(Level.INFO, "Client OBJ:{0}", client);
                log.log(Level.INFO, "Value of the converter:{0}", value);
                
                client.setClientId(Integer.parseInt(value));
                return client;
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
            return String.valueOf(((Client) value).getClientId());
        } else {
            return null;
        }
    }

}
