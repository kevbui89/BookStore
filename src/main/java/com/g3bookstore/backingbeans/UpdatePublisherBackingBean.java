package com.g3bookstore.backingbeans;

import com.g3bookstore.customcontrollers.CustomPublisherJpaController;
import com.g3bookstore.entities.Publisher;
import com.g3bookstore.util.MessageProvider;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.RowEditEvent;

/**
 * This class allow to update a publisher name by managing the events of the
 * table.
 *
 * @author Denis Lebedev
 */
@Named("updatePublisher")
@SessionScoped
public class UpdatePublisherBackingBean implements Serializable {

    private static final Logger log = Logger.getLogger(UpdateAuthorBackingBean.class.getName());

    @Inject
    private CustomPublisherJpaController pubController;

    private List<Publisher> pubList;

    @PostConstruct
    public void init() {
        pubList = pubController.getPublishers();
    }

    public List<Publisher> getPublishers() {
        return pubList;
    }

    public void setPublishers(List<Publisher> pubList) {
        this.pubList = pubList;
    }

    public void onRowEdit(RowEditEvent event) throws Exception {
        log.log(Level.INFO, "Update the publisher object");
        Publisher pub = (Publisher) event.getObject();
        log.log(Level.INFO, "New Name:{0}", pub.getPublisher());

        pubController.update(pub);

    }
}
