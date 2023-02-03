package com.g3bookstore.backingbeans;

import com.g3bookstore.customcontrollers.CustomRssNewsJpaController;
import com.g3bookstore.entities.RssNews;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author lebed
 */
@Named("rssNews")
@RequestScoped
public class CreateNewsBackingBean1 {

    @Inject
    private CustomRssNewsJpaController customRssNewsController;

    private RssNews rssNews;
    private List<RssNews> rssFeeds;
    private RssNews selectedFeed;

    public List<RssNews> getRssFeeds() {
        rssFeeds = customRssNewsController.getRssNews();
        return rssFeeds;
    }

    public void setRssFeeds(List<RssNews> rssFeeds) {
        this.rssFeeds = rssFeeds;
    }

    public RssNews getRssNews() {
        if (rssNews == null) {
            rssNews = new RssNews();
        }
        return rssNews;
    }

    public RssNews getSelectedFeed() {
        return selectedFeed;
    }

    public void setSelectedFeed(RssNews selectedFeed) {
        this.selectedFeed = selectedFeed;
    }

    public void setRssNews(RssNews rssNews) {
        this.rssNews = rssNews;
    }

    public String createRssNews() throws Exception {
        RssNews oldRss = customRssNewsController.getActiveRssNews();
        oldRss.setDisplay(false);
        customRssNewsController.update(oldRss);
        rssNews.setDisplay(true);
        customRssNewsController.create(rssNews);
        return null;
    }

    public String changeFeed() throws Exception {
        RssNews oldRss = customRssNewsController.getActiveRssNews();
        if (oldRss == selectedFeed || selectedFeed == null) {
            return null;
        }
        oldRss.setDisplay(false);
        customRssNewsController.update(oldRss);
        selectedFeed.setDisplay(true);
        customRssNewsController.update(selectedFeed);
        return null;
    }

}
