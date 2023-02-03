package com.g3bookstore.backingbeans;

import com.g3bookstore.customcontrollers.CustomPublisherJpaController;
import com.g3bookstore.entities.Publisher;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author lebed
 */
@Named("createPublisher")
@RequestScoped
public class CreatePublisherBackingBean {
    
    @Inject
    private CustomPublisherJpaController publisherController;
    
    private Publisher publisher;
    
    public Publisher getPublisher() {
        if(publisher == null)
            publisher = new Publisher();
        return publisher;
    }
    
    public String createPublisher() throws Exception {
        publisherController.create(publisher);
        return null;
    }
}
