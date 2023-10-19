package com.company.RssReaderBot.utils.parser;

import com.company.RssReaderBot.models.ItemModel;
import com.company.RssReaderBot.utils.DateUtils;
import com.company.RssReaderBot.utils.InvalidPubDateException;
import com.company.RssReaderBot.utils.InvalidUserEnteredDateException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

/**
 * Class to work with node items, parsed from RSS,
 * and return it as a list of items
 */
@Component
public class RssParser {

    private static final String CHANNEL = "channel";
    private static final String ITEM = "item";
    private static final String TITLE = "title";
    private static final String ENCLOSURE = "enclosure";
    private static final String URL = "url";
    private static final String LINK = "link";
    private static final String DESCRIPTION = "description";
    private static final String PUB_DATE = "pubDate";
    private static final String GUID = "guid";
    private static final String MEDIA_CONTENT = "media:content";
    private static final String MEDIA_THUMBNAIL = "media:thumbnail";

    @Getter @Setter
    private String rssUrl;
    @Getter
    private String feedTitle;
    @Getter @Setter
    private int feedId;

    @Getter
    private NodeList nodeList;

    /**
     * Parses the RSS feed from the provided URL and retrieves the list of items.
     *
     * @param url                       URL of the RSS feed to parse.
     * @throws RssParsingException      If an error occurs while parsing the RSS feed.
     */
    public void parseRss(String url) throws RssParsingException {
        try {
            Document doc = getDocument(url);
            nodeList = doc.getElementsByTagName(ITEM);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RssParsingException("Error parsing RSS: ", e);
        }
    }

    /**
     * Parses the feed title from the provided URL.
     *
     * @param url URL of the RSS feed to parse.
     */
    public void parseFeedTitle(String url) {
        try {
            Document doc = getDocument(url);
            Element channel = (Element) doc.getElementsByTagName(CHANNEL).item(0);
            feedTitle = getOptionalSubElementValue(channel, TITLE);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            // logging: "Error parsing feed title: ", e.getMessage()
            feedTitle = "Noname";
        }
    }

    /**
     * Retrieves an XML Document object from the specified URL.
     *
     * @param url                               URL of the XML document to retrieve and parse.
     * @return                                  Parsed XML Document object.
     * @throws ParserConfigurationException     If a DocumentBuilder cannot be created.
     * @throws SAXException                     If there is an error parsing the XML.
     * @throws IOException                      If an I/O error occurs while fetching or reading the XML.
     */
    private Document getDocument(String url) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(url);
        doc.getDocumentElement().normalize();
        return doc;
    }

    public List<ItemModel> getAllElementsList() {
        List<ItemModel> itemsList = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            Element element = (Element) node;
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                ItemModel currentItemModel = createItem(element);
                itemsList.add(currentItemModel);
            }
        }
        return itemsList;
    }

    public List<ItemModel> getElementListByTitle(String title) {
        List<ItemModel> itemsList = new ArrayList<>();
        title = title.toLowerCase();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String currentNodeTitle = element
                        .getElementsByTagName(TITLE)
                        .item(0)
                        .getTextContent()
                        .toLowerCase();
                if (currentNodeTitle.contains(title)) {
                    ItemModel currentItemModel = createItem(element);
                    itemsList.add(currentItemModel);
                }
            }
        }
        return itemsList;
    }

    public List<ItemModel> getElementListByDate(String dateString) throws InvalidUserEnteredDateException {
        List<ItemModel> elementList = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            Element element = (Element) node;
            String currentItemDate = getOptionalSubElementValue(element, PUB_DATE);

            LocalDate userDate = DateUtils.parseLocalDateFromString(dateString);
            Date itemDate;
            try {
                itemDate = DateUtils.parseDateFromString(currentItemDate);
            } catch (InvalidPubDateException e) {
                // log
                continue;
            }

            if (DateUtils.compareDates(userDate, itemDate)) {
                ItemModel currentItemModel = createItem(element);
                elementList.add(currentItemModel);
            }
        }
        return elementList;
    }

    /**
     * Method that receives the current element's data (sub-elements of the item),
     * such as title, description, pubDate, media, link.
     * If there is no media object (enclosure), media content and thumbnail are checked.
     * If there is no link, guid is checked.
     * If one of the sub-elements is not found, sets to null.
     *
     * @param       element current RSS feed item
     * @return      ItemModel object that represents feed item
     */
    private ItemModel createItem(Element element) {
        String itemTitle = getOptionalSubElementValue(element, TITLE);
        String itemDescription = getOptionalSubElementValue(element, DESCRIPTION);
        Date pubDate;
        try {
            pubDate = DateUtils.parseDateFromString(getOptionalSubElementValue(element, PUB_DATE));
        } catch (InvalidPubDateException e) {
            // log
            pubDate = null;
        }

        String mediaUrl = Optional.ofNullable(
                element.getElementsByTagName(ENCLOSURE).item(0)
                ).map(node -> node.getAttributes().getNamedItem(URL).getTextContent())
                .orElseGet(() -> Optional.ofNullable(
                        element.getElementsByTagName(MEDIA_CONTENT).item(0)
                        ).map(node -> node.getAttributes().getNamedItem(URL).getTextContent())
                        .orElse(getOptionalSubElementValue(element, MEDIA_THUMBNAIL)));
        String sourceLink = Optional.ofNullable(
                element.getElementsByTagName(LINK).item(0)
                ).map(Node::getTextContent)
                .orElse(getOptionalSubElementValue(element, GUID));

        return ItemModel.builder()
                .feedId(feedId)
                .title(itemTitle)
                .description(itemDescription)
                .pubDate(pubDate)
                .mediaUrl(mediaUrl)
                .sourceLink(sourceLink)
                .build();
    }

    /**
     * Retrieves the text content of an optional sub-element within the provided XML element.
     *
     * @param element   XML element to search for the sub-element.
     * @param tag       Tag name of the sub-element to retrieve the text content from.
     * @return          Text content of the sub-element, or null if it is not found.
     */
    private String getOptionalSubElementValue(Element element, String tag) {
        return Optional.ofNullable(element.getElementsByTagName(tag).item(0))
                .map(Node::getTextContent).orElse(null);
    }
}
