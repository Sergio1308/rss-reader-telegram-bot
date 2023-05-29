package com.company.RssReaderBot.utils.parser;

import com.company.RssReaderBot.models.ItemModel;
import com.company.RssReaderBot.utils.DateUtils;
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

    public void parseRss(String url) {
        try {
            Document doc = getDocument(url);
            nodeList = doc.getElementsByTagName(ITEM);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public void parseFeedTitle(String url) {
        try {
            Document doc = getDocument(url);
            Element channel = (Element) doc.getElementsByTagName(CHANNEL).item(0);
            feedTitle = getOptionalSubElementValue(channel, TITLE);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

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

    public List<ItemModel> getElementListByDate(String dateString) {
        List<ItemModel> elementList = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            Element element = (Element) node;
            String currentItemDate = getOptionalSubElementValue(element, PUB_DATE);
            if (currentItemDate.contains(dateString)) {
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
     * @param element current RSS feed item
     * @return ItemModel object that represents feed item
     */
    private ItemModel createItem(Element element) {
        String itemTitle = getOptionalSubElementValue(element, TITLE);
        String itemDescription = getOptionalSubElementValue(element, DESCRIPTION);
        Date pubDate = DateUtils.parseDate(getOptionalSubElementValue(element, PUB_DATE)).orElse(null);

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

    private String getOptionalSubElementValue(Element element, String tag) {
        return Optional.ofNullable(element.getElementsByTagName(tag).item(0))
                .map(Node::getTextContent).orElse(null);
    }
}
