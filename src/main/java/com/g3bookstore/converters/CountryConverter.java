package com.g3bookstore.converters;

import com.g3bookstore.customcontrollers.CustomCountryJpaController;
import com.g3bookstore.entities.Country;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Werner
 */
@Named("CountryConverter")
@ApplicationScoped
public class CountryConverter implements Converter {

    @Inject
    private CustomCountryJpaController controller;

    /**
     * Converts a String with a Country name to the Country object.
     *
     * @param context FacesContext object
     * @param component UICompoment object
     * @param value The string to be converted
     * @return the Province object with the selected name
     */
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        List<Country> list = controller.getCountry();
        Country chosen = null;
        for (Country c : list) {
            if (c.getCountry().equals(value)) {
                chosen = c;
            }
        }

        /**
         * Cookies
         */
        if (chosen == null) {
            return null;
        }
        return chosen;
    }

    /**
     * Converts a country object to a string
     *
     * @param context FacesContext object
     * @param component UICompoment object
     * @param value The object to be converted
     * @return The name of the selected province as a string
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value instanceof Country) {
            Country country = (Country) value;
            return country.getCountry();
        }
        return null;
    }

}
