package com.g3bookstore.converters;

import com.g3bookstore.customcontrollers.CustomProvinceJpaController;
import com.g3bookstore.entities.Province;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author knock
 */
@Named("ProvinceConverter")
@ApplicationScoped
public class ProvinceConverter implements Converter {
    @Inject
    private CustomProvinceJpaController controller;
    
    /**
     * Converts a String with a Province name to the Province object.
     * @param context   FacesContext object
     * @param component UICompoment object
     * @param value     The string to be converted
     * @return          the Province object with the selected name
     */
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        List<Province> list = controller.getProvinces();
        Province chosen = null;
        for(Province p : list)
            if(p.getProvince().equals(value))
                chosen = p;
        
        /**
         * Cookies
         */
        if(chosen == null) {
            return null;
        }
        return chosen;
    }
    
    /**
     * Converts a province object to a string
     * @param context       FacesContext object
     * @param component     UICompoment object
     * @param value         The object to be converted
     * @return              The name of the selected province as a string
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if(value instanceof Province) {
            Province province = (Province)value;
            return province.getProvince();
        } else {
            return null;
        }
        /**
         * Cookies
         */
    }  
}
