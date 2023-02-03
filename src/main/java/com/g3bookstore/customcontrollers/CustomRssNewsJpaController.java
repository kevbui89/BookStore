package com.g3bookstore.customcontrollers;

import com.g3bookstore.controllers.RssNewsJpaController;
import com.g3bookstore.controllers.exceptions.IllegalOrphanException;
import com.g3bookstore.controllers.exceptions.NonexistentEntityException;
import com.g3bookstore.controllers.exceptions.RollbackFailureException;
import com.g3bookstore.entities.MasterInvoice;
import com.g3bookstore.entities.RssNews;
import com.g3bookstore.entities.RssNews_;
import com.g3bookstore.rssnews.FeedMessage;
import com.g3bookstore.rssnews.RssNewsXMLParser;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Custom controller for Rss News.
 *
 * @author Alessandro Ciotola, Kevin Bui
 */
@Named("theNew")
@RequestScoped
public class CustomRssNewsJpaController implements Serializable {

    @Inject
    private RssNewsJpaController newsJpaController;

    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em = null;

    /**
     * Allow to create a news
     * 
     * @param news
     * @throws Exception 
     */
    public void create(RssNews news) throws Exception {
        newsJpaController.create(news);
    }
    
    /**
     * Allow to delete a news
     * 
     * @param id
     * @throws NonexistentEntityException
     * @throws RollbackFailureException
     * @throws Exception 
     */
    public void delete(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        newsJpaController.destroy(id);
    }
    
    /**
     * Allow to update a news.
     * 
     * @param news
     * @throws IllegalOrphanException
     * @throws NonexistentEntityException
     * @throws RollbackFailureException
     * @throws Exception 
     */
    public void update(RssNews news) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        newsJpaController.edit(news);
    }
    
    /**
     * Method which will return all rss news in the database
     *
     * @return
     */
    public List<RssNews> getRssNews() {
        return newsJpaController.findRssNewsEntities();
    }

    /**
     * Method which will find a specific Rss news by ID
     *
     * @param id
     * @return
     */
    public RssNews getRssNewsByID(int id) {
        return newsJpaController.findRssNews(id);
    }

    /**
     * Method which will search through the Rss News table for specific news by
     * title.
     *
     * @param title
     * @return The News object
     */
    public RssNews getRssNewsByTitle(String title) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<RssNews> cq = cb.createQuery(RssNews.class);
        Root<RssNews> rss = cq.from(RssNews.class);
        cq.select(rss);
        cq.where(cb.equal(rss.get(RssNews_.title), title));

        Query q = em.createQuery(cq);
        return (RssNews) q.getSingleResult();
    }
    
    /**
     * Get the current active RSSFeed
     * 
     * @return 
     */
    public RssNews getActiveRssNews() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<RssNews> cq = cb.createQuery(RssNews.class);
        Root<RssNews> rss = cq.from(RssNews.class);
        cq.select(rss);
        cq.where(cb.equal(rss.get(RssNews_.display), 1));

        Query q = em.createQuery(cq);
        return (RssNews) q.getSingleResult();
    }

    /**
     * Returns a list of FeedMessage objects from the database
     *
     * @return The list of FeedMessage
     */
    public List<RssNews> getFeed() {
        
        String query = "select r from RssNews r where r.display = TRUE"; 
        
        TypedQuery<RssNews> q = em.createQuery(query, RssNews.class); 
        List<RssNews> rn = q.getResultList();
        
        return rn;
    }

    /**
     * Returns a list of String links
     *
     * @return The list of String links
     */
    public List<String> getLinks() {
        List<String> links = new ArrayList<String>();

        List<RssNews> rn = getFeed();
        for (RssNews fm : rn) {
            links.add(fm.getLink());
        }

        return links;
    }

    /**
     * Returns the FeedMessage objects containing the RSS Feeds.
     *
     * @return The list of FeedMessage objects
     */
    public List<FeedMessage> getFeedToDisplay() {
        List<String> links = getLinks();
        String[] array = new String[links.size()];
        RssNewsXMLParser parser = new RssNewsXMLParser();
        return parser.readFeed(links.toArray(array));
    }
}
