package com.g3bookstore.backingbeans;

import com.g3bookstore.customcontrollers.CustomAdJpaController;
import com.g3bookstore.entities.Ad;
import com.g3bookstore.util.UploadImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Denis Lebedev
 */
@Named("updateAd")
@SessionScoped
public class UpdateAdsBackingBean implements Serializable {

    private static final Logger log = Logger.getLogger(UpdateAdsBackingBean.class.getName());

    @Inject
    private CustomAdJpaController adController;
    private UploadImage upload = new UploadImage();
    private UploadedFile uploadedF;
    private boolean requestToUpload;

    private Ad selectedAd;
    private List<Ad> adList;

    @PostConstruct
    public void init() {
        adList = adController.getAds();
        requestToUpload = false;
        log.log(Level.INFO, "Ad list size:{0}", adList.size());
    }

    public Ad getAd() {
        log.log(Level.INFO, "Current obj:{0}", selectedAd);
        return selectedAd;
    }

    public void setAd(Ad selectedAd) {
        log.log(Level.INFO, "Setting obj:{0}", selectedAd);
        this.selectedAd = selectedAd;
    }

    public List<Ad> getAds() {
        return adList;
    }

    public void setAds(List<Ad> adList) {
        this.adList = adList;
    }

    public void onRowEdit(RowEditEvent event) throws Exception {
        Ad temp = (Ad) event.getObject();
        this.selectedAd = temp;

        if (requestToUpload) {
            try {
                upload.addAdBanner(uploadedF, selectedAd);
            } catch (IOException e) {
                log.log(Level.SEVERE, "Reading image issue:{0}", e.getMessage());
            }
            requestToUpload = false;
        }

        log.log(Level.INFO, "Given new title:{0}", temp.getTitle());
        log.log(Level.INFO, "Given new link:{0}", temp.getAdLink());
        log.log(Level.INFO, "Updating ad");
        adController.update(temp);

    }

    public void onRowCancel(RowEditEvent event) throws Exception {
        requestToUpload = false;
    }

    /**
     * Create and upload two images with specific size. One for the small cover
     * and one for the big cover.
     *
     * @param event
     * @throws IOException
     */
    public void handleFileUpload(FileUploadEvent event) throws IOException {
        requestToUpload = true;
        uploadedF = event.getFile();

    }

}
