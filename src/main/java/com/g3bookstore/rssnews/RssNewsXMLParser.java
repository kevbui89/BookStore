package com.g3bookstore.rssnews;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;

/**
 * Retrieves the XML tags from an XML page and parses the information to display
 * them on the website.
 *
 * A lot of the methods were taken from this article
 *
 * @author http://www.vogella.com/tutorials/RSSFeed/article.html
 * @author Kevin Bui
 */
public class RssNewsXMLParser {

    static final String TITLE = "title";
    static final String DESCRIPTION = "description";
    static final String CHANNEL = "channel";
    static final String LANGUAGE = "language";
    static final String COPYRIGHT = "copyright";
    static final String LINK = "link";
    static final String AUTHOR = "author";
    static final String ITEM = "item";
    static final String PUB_DATE = "pubDate";
    static final String GUID = "guid";

    private Logger log = Logger.getLogger(RssNewsXMLParser.class.getName());

    /**
     * Default constructor
     */
    public RssNewsXMLParser() {
    }

    /**
     * Takes an array of URLs and gets the feed for each one of them
     *
     * @param links RSS feed links.
     * @return Feeds from all the links
     */
    public List<FeedMessage> readFeed(String[] links) {
        // Check if the links are null or 0
        if (links == null || links.length == 0) {
            return new ArrayList<FeedMessage>();
        }

        List<FeedMessage> fm = new ArrayList<>();
        URL url;
        for (String urlString : links) {
            try {
                url = new URL(urlString);
                fm.addAll(parseURL(url));
            } catch (MalformedURLException | XMLStreamException ex) {
                log.log(Level.SEVERE, "Data>>>{0}", ex);
            }
        }

        return fm;
    }

    /**
     * Reads the feed from the url and parses the response.
     *
     *
     * @param url The url to parse
     * @return An array of FeedMessage
     * @throws XMLStreamException
     */
    private List<FeedMessage> parseURL(URL url) throws XMLStreamException {

        List<FeedMessage> feed = new ArrayList<>();
        String description = "";
        String title = "";
        String link = "";
        String language = "";
        String copyright = "";
        String author = "";
        String pubdate = "";
        String guid = "";

        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        InputStream in = read(url);

        if (in != null) {
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
            // Read the XML document
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                if (event.isStartElement()) {
                    String localPart = event.asStartElement().getName()
                            .getLocalPart();
                    switch (localPart) {
                        case TITLE:
                            title = getCharacterData(event, eventReader);
                            break;
                        case DESCRIPTION:
                            description = getCharacterData(event, eventReader).trim();
                            break;
                        case LINK:
                            link = getCharacterData(event, eventReader);
                            break;
                        case GUID:
                            guid = getCharacterData(event, eventReader);
                            break;
                        case LANGUAGE:
                            language = getCharacterData(event, eventReader);
                            break;
                        case AUTHOR:
                            author = getCharacterData(event, eventReader);
                            break;
                        case PUB_DATE:
                            pubdate = getCharacterData(event, eventReader);
                            break;
                        case COPYRIGHT:
                            copyright = getCharacterData(event, eventReader);
                            break;
                    }
                } else if (event.isEndElement()) {
                    if (event.asEndElement().getName().getLocalPart().equalsIgnoreCase(ITEM)) {
                        FeedMessage message = new FeedMessage();
                        message.setAuthor(author);
                        message.setDescription(description);
                        message.setGuid(guid);
                        message.setLink(link);
                        message.setTitle(title);
                        message.setLanguage(language);
                        message.setCopyright(copyright);
                        message.setPubDate(pubdate);
                        feed.add(message);
                        event = eventReader.nextEvent();
                    }
                }
            }
        }

        return feed;
    }

    /**
     * Parses the characters from an event into a String
     *
     * @param event The XML event tag
     * @param eventReader The XML tag reader
     * @return The String of the XML
     * @throws XMLStreamException
     */
    private String getCharacterData(XMLEvent event, XMLEventReader eventReader)
            throws XMLStreamException {
        String result = "";
        event = eventReader.nextEvent();
        while (event instanceof Characters && !event.isEndElement()) {
            result += event.asCharacters().getData();
            event = eventReader.nextEvent();
        }
        
        return result;
    }

    /**
     * Reads the RSS feed XML from the url provided
     *
     * @param url
     * @return InputStream of the XML of the feed
     */
    private InputStream read(URL url) {
        try {
            return url.openStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
