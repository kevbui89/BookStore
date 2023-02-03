package com.g3bookstore.backingbeans;

import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import com.g3bookstore.customcontrollers.CustomAdJpaController;
import com.g3bookstore.entities.Ad;
import com.g3bookstore.util.MessageProvider;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Allow to manage the ads and ensure only 2 ads are selected at the same time.
 *
 * @author Denis Lebedev
 */
@Named("manageAds")
@SessionScoped
public class ManageAdsBackingBean implements Serializable {

    @Inject
    private CustomAdJpaController adController;

    private MessageProvider bundle = new MessageProvider();

    private List<Ad> adList;

    @PostConstruct
    public void init() {
        adList = new ArrayList();
    }

    public void setSelectedAd(List<Ad> adList) {
        this.adList = adList;
    }

    public List<Ad> getSelectedAd() {
        return adList;
    }

    public String updateAd() throws RollbackFailureException, Exception {
        if (adList.size() != 2) {
            FacesContext.getCurrentInstance().addMessage("updateAds",
                    new FacesMessage(bundle.getValue("manage_ads")));
        } else {
            List<Ad> choosenAd = adController.getChosenAd();

            for (Ad ad : choosenAd) {
                ad.setChosen(false);
                adController.update(ad);
            }

            for (Ad ad : adList) {
                ad.setChosen(true);
                adController.update(ad);
            }

            return"manager.xhtm";
        }
        return null;
    }
}
