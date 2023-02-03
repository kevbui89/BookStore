package com.g3bookstore.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;

/**
 * Utility class to get the message bundle.
 *
 * @author Werner
 */
public class MessageProvider {

    private ResourceBundle bundle;

    public ResourceBundle getBundle() {
        if (bundle == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            bundle = context.getApplication().getResourceBundle(context, "msgs");
        }
        return bundle;
    }

    /**
     * Retrieves the value of a given key for the message bundle.
     *
     * @param key
     * @return
     */
    public String getValue(String key) {

        String result = null;
        try {
            result = getBundle().getString(key);
        } catch (MissingResourceException e) {
            result = "???" + key + "??? not found";
        }
        return result;
    }

}
