package com.g3bookstore.backingbeans;

import com.g3bookstore.customcontrollers.CustomAdJpaController;
import com.g3bookstore.entities.Ad;
import com.g3bookstore.util.MessageProvider;
import com.g3bookstore.util.UploadImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 * This class allow to create an Ad by interacting with the data from the form.
 *
 * @author Denis Lebedev
 */
@Named("createAds")
@SessionScoped
public class CreateAdsBackingBean implements Serializable {

    private final static Logger log = Logger.getLogger(CreateAdsBackingBean.class.getName());

    private MessageProvider bundle = new MessageProvider();
    private UploadImage upload = new UploadImage();

    @Inject
    private CustomAdJpaController adController;

    private Ad ad;
    private boolean uploaded;
    private UploadedFile uploadedF;

    @PostConstruct
    public void init() {
        uploaded = false;
    }

    public Ad getAd() {
        if (ad == null) {
            ad = new Ad();
        }
        return ad;
    }

    /**
     * Create an Ad with a default not found image and set it to not used then
     * go back the main manager page.
     *
     * @return
     * @throws Exception
     */
    public String createAd() throws Exception {

        if (!uploaded) {
            log.log(Level.INFO, "Missing image:{0}", uploaded);
            FacesContext.getCurrentInstance().addMessage("uploadImg", new FacesMessage(bundle.getValue("required_upload")));
            return null;
        } else {

            log.log(Level.INFO, "Creating Ad");

            try {
                upload.addAdBanner(uploadedF, this.getAd());
            } catch (IOException e) {
                log.log(Level.SEVERE, "Reading image issue:{0}", e.getMessage());
            }

            adController.create(ad);
            return "manager.xhtml";
        }
    }

    /**
     * Create and upload an image for the ad.
     *
     * @param event
     * @throws IOException
     */
    public void handleFileUpload(FileUploadEvent event) throws IOException {
        uploaded = true;

        log.log(Level.INFO, "Status:{0}", uploaded);

        log.log(Level.INFO, "Uploading the image");

        uploadedF = event.getFile();

    }
}
