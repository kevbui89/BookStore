package com.g3bookstore.backingbeans;

import com.g3bookstore.customcontrollers.CustomAdJpaController;
import com.g3bookstore.entities.Ad;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * This class will take care of fetching data from the database into a bean and
 * display it.
 *
 * @author Kevin Bui
 */
@Named("AdBean")
@SessionScoped
public class AdBackingBean implements Serializable {

    @Inject
    private CustomAdJpaController adController;

    private Logger log = Logger.getLogger(AdBackingBean.class.getName());

    /**
     * Returns all the adds from the database
     *
     * @return
     */
    public List<Ad> getAds() {
        return (List<Ad>) adController.getAds();
    }

    /**
     * Returns the list of ads chosen by the administrator
     *
     * @return
     */
    public List<Ad> getChosenAds() {
        return (List<Ad>) adController.getChosenAd();
    }
}
