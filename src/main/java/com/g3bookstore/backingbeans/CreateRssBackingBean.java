package com.g3bookstore.backingbeans;

import com.g3bookstore.customcontrollers.CustomRssNewsJpaController;
import com.g3bookstore.entities.RssNews;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * This class allow to create an Rss feed by interacting with
 * the data from the form.
 * 
 * @author Denis Lebedev
 */
@Named("createRss")
@RequestScoped
public class CreateRssBackingBean {
    
    @Inject
    private CustomRssNewsJpaController rssController;
    
    private RssNews rss;
    
    public RssNews getRssNews() {
        if(rss == null) {
            rss = new RssNews();
        }
        return rss;
    }
    
    public String createRssNews() throws Exception {
        rss.setDisplay(false);
        rssController.create(rss);
        return "manager.xhtml";
    }
}
