package com.company.RssReaderBot.parser;

import com.company.RssReaderBot.entities.Item;
import com.company.RssReaderBot.entities.ItemUrl;
import com.company.RssReaderBot.entities.ItemDate;
import org.springframework.beans.factory.annotation.Value;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class to work with node items, parsed from RSS,
 * and return it as a list of items
 */
public class RssParser {

    private static String rssUrl;

    private NodeList nodeList;
    // https://validator.w3.org/feed/docs/rss2.html required
    private static final String ITEM = "item";
    private static final String TITLE = "title";
    private static final String ENCLOSURE = "enclosure";
    private static final String URL = "url";
    private static final String LINK = "link";
    private static final String DESCRIPTION = "description";
    private static final String PUB_DATE = "pubDate";

    public RssParser() { nodeList = null; }

    public static String getRssUrl() {
        return rssUrl;
    }

    public static void setRssUrl(String url) {
        rssUrl = url;
    }

    public void parseRss() {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(rssUrl);
            doc.getDocumentElement().normalize();

            nodeList = doc.getElementsByTagName(ITEM);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public String getUserRssURL() { return null; } // todo

    public NodeList getNodeList() {
        return nodeList;
    }

    public List<Item> getAllElementsList(NodeList nodeList) {
        List<Item> elementList = new ArrayList<>();
        for (int temp = 0; temp < nodeList.getLength(); temp++) {
            Node node = nodeList.item(temp);
            Element element = (Element) node;
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                ItemUrl itemUrl;
                // todo without try catch
                try {
                    itemUrl = new ItemUrl(element.getElementsByTagName(ENCLOSURE).item(0).
                            getAttributes().getNamedItem(URL).getTextContent());
                } catch (NullPointerException e) {
                    itemUrl = new ItemUrl(element.getElementsByTagName(LINK).item(0).
                            getTextContent());
                }
                String currDate = element.getElementsByTagName(PUB_DATE).item(0).getTextContent();
                ItemDate date = new ItemDate();
                date.formatRSSDate(currDate);
                Item currItem = new Item(
                        element.getElementsByTagName(TITLE).item(0).getTextContent(),
                        element.getElementsByTagName(DESCRIPTION).item(0).getTextContent(),
                        itemUrl,
                        date
                );
                elementList.add(currItem);
            }
        }
        return elementList;
    }

    public List<Item> getElementListByTitle(NodeList nodeList, String title) {
        List<Item> itemList = new ArrayList<>();
        title = title.toLowerCase();
        for (int temp = 0; temp < nodeList.getLength(); temp++) {
            Node node = nodeList.item(temp);
            Element element = (Element) node;
            String currentNodeTitle = element.getElementsByTagName(TITLE).item(0).getTextContent().toLowerCase();
            if (node.getNodeType() == Node.ELEMENT_NODE && currentNodeTitle.contains(title)) {
                ItemUrl itemUrl;
                // todo without try catch
                try {
                    itemUrl = new ItemUrl(element.getElementsByTagName(ENCLOSURE).item(0).
                            getAttributes().getNamedItem(URL).getTextContent());
                } catch (NullPointerException e) {
                    itemUrl = new ItemUrl(element.getElementsByTagName(LINK).item(0).
                            getTextContent());
                }
                String currDate = element.getElementsByTagName(PUB_DATE).item(0).getTextContent();
                ItemDate date = new ItemDate();
                date.formatRSSDate(currDate);

                Item currItem = new Item(
                        element.getElementsByTagName(TITLE).item(0).getTextContent(),
                        element.getElementsByTagName(DESCRIPTION).item(0).getTextContent(),
                        itemUrl,
                        date
                );
                itemList.add(currItem);
            }
        }
        return itemList;
    }

    public List<Item> getElementListByDate(NodeList nodeList, Date date) {
        // todo
        List<Item> elementList = new ArrayList<>();
        for (int temp = 0; temp < nodeList.getLength(); temp++) {
            Node node = nodeList.item(temp);
            Element element = (Element) node;
            String strDate = element.getElementsByTagName(PUB_DATE).item(0).getTextContent();
            /*
            LocalDateTime elemDate = new EpisodeDate(strDate).getDate();
            System.out.println("element date: " + elemDate);
            System.out.println();
            System.out.println("selected date: " + date);*/
            /*if (node.getNodeType() == Node.ELEMENT_NODE) {
                Audio audio = new Audio(element.getElementsByTagName(ENCLOSURE).item(0).
                        getAttributes().getNamedItem(URL).getTextContent());
                String currDate = element.getElementsByTagName(PUB_DATE).item(0).getTextContent();
                EpisodeDate episodeDate = new EpisodeDate(date);
                Episode currEpisode = new Episode(
                        element.getElementsByTagName(TITLE).item(0).getTextContent(),
                        element.getElementsByTagName(DESCRIPTION).item(0).getTextContent(),
                        audio,
                        episodeDate
                );
                elementList.add(currEpisode);
            }*/
        }
        return elementList;
    }

    // debug
    public void printNodeList(NodeList nodeList) {
        System.out.println("Node length: " + nodeList.getLength());
        for (int temp = 0; temp < nodeList.getLength(); temp++) {
            Node node = nodeList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                System.out.println(element.getElementsByTagName(TITLE).item(0).getTextContent());
                System.out.println(element.getElementsByTagName(DESCRIPTION).item(0).getTextContent());
                System.out.println(element.getElementsByTagName(ENCLOSURE).item(0).
                        getAttributes().getNamedItem(URL).getTextContent());
            }
        }
    }

    public static void main(String[] args) {
        RssParser parser = new RssParser();
        parser.parseRss();
        List<Item> itemList = parser.getAllElementsList(parser.getNodeList());
        System.out.println(itemList.size());
        for (Item item : itemList) {
            System.out.println(item.getTitle());
            System.out.println(item.getAudio().getUrl() + "\n----------------------------------");
        }
    }
}
