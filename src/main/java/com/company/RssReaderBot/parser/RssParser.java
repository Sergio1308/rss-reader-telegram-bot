package com.company.RssReaderBot.parser;

import com.company.RssReaderBot.entities.Item;
import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Class to work with node items, parsed from RSS,
 * and return it as a list of items
 */
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
    private static final String EMPTY_ELEMENT = "empty";

    @Getter @Setter
    private static String rssUrl;
    @Getter
    private static String channelTitle;

    @Getter
    private NodeList nodeList;

    private DocumentBuilder builder;

    public RssParser() {
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(rssUrl);
            doc.getDocumentElement().normalize();
            nodeList = doc.getElementsByTagName(ITEM);
            Element channel = (Element) doc.getElementsByTagName(CHANNEL).item(0);
            channelTitle = getOptionalSubElementValue(channel, TITLE);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public void parseRss() {
        try {
            Document doc = builder.parse(rssUrl);
            doc.getDocumentElement().normalize();
            nodeList = doc.getElementsByTagName(ITEM);
            Element channel = (Element) doc.getElementsByTagName(CHANNEL).item(0);
            channelTitle = getOptionalSubElementValue(channel, TITLE);
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public List<Item> getAllElementsList(NodeList nodeList) {
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

    public List<Item> getElementListByTitle(NodeList nodeList, String title) {
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

    public List<Item> getElementListByDate(NodeList nodeList, String dateString) {
        List<Item> elementList = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            Element element = (Element) node;
            String currentItemDate = getOptionalSubElementValue(element, PUB_DATE);
            if (currentItemDate.contains(dateString)) {
                Item currentItem = createItem(element);
                System.out.println(currentItem.getTitle());
                elementList.add(currentItem);
            }
        }
        return elementList;
    }

    private Item createItem(Element element) {
        String itemTitle = getOptionalSubElementValue(element, TITLE);
        String itemDescription = getOptionalSubElementValue(element, DESCRIPTION);
        String pubDate = getOptionalSubElementValue(element, PUB_DATE);
        String guid = getOptionalSubElementValue(element, GUID);

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
