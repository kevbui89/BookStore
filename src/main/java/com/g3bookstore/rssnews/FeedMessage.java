package com.g3bookstore.rssnews;

/**
 * Represents an RSS message
 *
 * @author http://www.vogella.com/tutorials/RSSFeed/article.html
 * @author Kevin Bui
 */
public class FeedMessage {

    private String title;
    private String description;
    private String link;
    private String author;
    private String guid;
    private String language;
    private String copyright;
    private String pubDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }
    
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
    
    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }
    
    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pd) {
        this.pubDate = pd;
    }

    /**
     * Returns the string img link of the FeedMessage
     *
     * @param fm The FeedMessage object
     * @return
     */
    public String getImageLinkFromFeed() {
        
        int start = description.indexOf("'") + 1;
        int end = description.indexOf("'", start);
        
        if (start == -1 || end == -1){
            return "";
        }

        return description.substring(start, end);
    }

    /**
     * Returns the descriptions from the FeedMessage
     * 
     * @return 
     */
    public String getDescriptionFromFeed() {
        int start = description.indexOf("<p>") + 3;
        int end = description.indexOf("</p>");
        
        if (start == -1 || end == -1){
            return "";
        }

        return description.substring(start, end);

    }

    @Override
    public String toString() {
        return "FeedMessage [title=" + title + ", description=" + description
                + ", link=" + link + ", author=" + author + ", guid=" + guid
                + ", language=" + language + ", copyright=" + copyright
                + ", pubDate=" + pubDate + "]";
    }

}
