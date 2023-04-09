package com.company.RssReaderBot.services.parser;

import com.company.RssReaderBot.entities.Item;
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
    private static final String EMPTY_ELEMENT = "no data";

    @Getter @Setter
    private String rssUrl;

    private String feedTitle;

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

    public String getFeedTitle(String url) {
        try {
            Document doc = getDocument(url);
            Element channel = (Element) doc.getElementsByTagName(CHANNEL).item(0);
            feedTitle = getOptionalSubElementValue(channel, TITLE);
            return feedTitle;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Document getDocument(String url) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(url);
        doc.getDocumentElement().normalize();
        return doc;
    }

    public List<Item> getAllElementsList() {
        List<Item> itemsList = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            Element element = (Element) node;
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Item currentItem = createItem(element);
                itemsList.add(currentItem);
            }
        }
        return itemsList;
    }

    public List<Item> getElementListByTitle(String title) {
        List<Item> itemsList = new ArrayList<>();
        title = title.toLowerCase();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String currentNodeTitle = element.getElementsByTagName(TITLE).item(0).getTextContent().toLowerCase();
                if (currentNodeTitle.contains(title)) {
                    Item currentItem = createItem(element);
                    itemsList.add(currentItem);
                }
            }
        }
        return itemsList;
    }

    public List<Item> getElementListByDate(String dateString) {
        List<Item> elementList = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            Element element = (Element) node;
            String currentItemDate = getOptionalSubElementValue(element, PUB_DATE);
            if (currentItemDate.contains(dateString)) {
                Item currentItem = createItem(element);
                elementList.add(currentItem);
            }
        }
        return elementList;
    }

    private Item createItem(Element element) {
        String itemTitle = getOptionalSubElementValue(element, TITLE);
        String itemDescription = getOptionalSubElementValue(element, DESCRIPTION);
//        System.out.println(Optional.ofNullable(element.getElementsByTagName(GUID).item(0).getAttributes().getNamedItem("isPermaLink"))
//                .map(Node::getTextContent).orElse(EMPTY_ELEMENT));
        String pubDate = getOptionalSubElementValue(element, PUB_DATE);
        String guid = getOptionalSubElementValue(element, GUID);
        // check enclosure (mediaUrl), if not null - insert, then go for link/guid, if null - do not use \n?
        // check link first, then if not - check guid ispermalink=true

        String mediaUrl = Optional.ofNullable(element.getElementsByTagName(LINK).item(0))
                .map(Node::getTextContent)
                .orElseGet(() -> element.getElementsByTagName(ENCLOSURE).item(0)
                        .getAttributes().getNamedItem(URL).getTextContent());

        return new Item(itemTitle, itemDescription, pubDate, mediaUrl, guid);
    }

    private String getOptionalSubElementValue(Element element, String tag) {
        return Optional.ofNullable(element.getElementsByTagName(tag).item(0))
                .map(Node::getTextContent).orElse(EMPTY_ELEMENT);
    }
}
