package com.g3bookstore.backingbeans;

import com.g3bookstore.customcontrollers.CustomRssNewsJpaController;
import com.g3bookstore.rssnews.FeedMessage;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * This class will take care of fetching the RSS Feed from the database 
 * 
 * @author Kevin
 */
@Named("RssNewsBackingBean")
@SessionScoped
public class RssNewsBackingBean implements Serializable {
    
    @Inject
    private CustomRssNewsJpaController rssController;
    
    private Logger log = Logger.getLogger(RssNewsBackingBean.class.getName());
    
    /**
     * Returns all the RSS FeedMessages
     * 
     * @return The list of RSS FeedMessages
     */
    public List<FeedMessage> getRssFeedMessages() {
        return rssController.getFeedToDisplay();
    }    
}
