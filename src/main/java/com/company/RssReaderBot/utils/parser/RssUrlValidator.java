package com.company.RssReaderBot.utils.parser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;

/**
 * A validator class, handle each possible exception and send short title of it to user,
 * if RSS URL is valid, then returns it for further addition to the DB.
 * Public method validateRssUrl takes string URL as a method argument and
 * returns this URL if it is valid, otherwise it returns a string that contains an error to inform user.
 */
public class RssUrlValidator {

    private HttpURLConnection connection;

    private URL url;

    private boolean isValid;

    private final int connectionReadTimeout;
    private final int connectionConnectTimeout;

    private final List<String> requiredContentTypes;

    public RssUrlValidator() {
        connectionReadTimeout = 5000;
        connectionConnectTimeout = 5000;
        requiredContentTypes = List.of("application/xml", "text/xml", "application/rss+xml");
    }

    public RssUrlValidator(int connectionReadTimeout,
                           int connectionConnectTimeout,
                           List<String> requiredContentTypes) {
        this.connectionReadTimeout = connectionReadTimeout;
        this.connectionConnectTimeout = connectionConnectTimeout;
        this.requiredContentTypes = requiredContentTypes;
    }

    public boolean isValid() {
        return isValid;
    }

    public String validateRssUrl(String urlString) {
        int responseCode;
        isValid = false;
        try {
            url = validateHttpProtocol(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(connectionReadTimeout);
            connection.setConnectTimeout(connectionConnectTimeout);
            responseCode = connection.getResponseCode();
        } catch (UnknownHostException e1) {
            return "Invalid URL <" + urlString + ">. Unable to connect to URL.";
        } catch (IOException e2) {
            return "An error was received while processing the following URL <" + urlString + ">. " + e2.getMessage();
        }
        if (responseCode != HttpURLConnection.HTTP_OK) {
            return validateResponseCode(responseCode);
        }
        return validateMimeType(connection.getContentType(), url.toString());
    }

    private String validateMimeType(String contentType, String urlString) {
        if (requiredContentTypes.stream().anyMatch(contentType::contains)) {
            isValid = true;
            return urlString;
        } else {
            String mimeType = !contentType.contains(";") ?
                    contentType : contentType.substring(0, contentType.indexOf(";"));
            return "This URL is in an unsupported format. Provided MIME type: <" +
                    mimeType + ">. Requires one of: " + String.join(", ", requiredContentTypes);
        }
    }

    private String validateResponseCode(int responseCode) {
        if (isRedirect(responseCode)) {
            String newUrl = connection.getHeaderField("Location");
            try {
                url = validateHttpProtocol(newUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(connectionReadTimeout);
                connection.setConnectTimeout(connectionConnectTimeout);
                responseCode = connection.getResponseCode();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Redirect to URL: " + url + " - " + responseCode); // todo debug
            return validateMimeType(connection.getContentType(), url.toString());
        } else {
            return "Unable to get response from URL <" + url +
                    ">. Received an invalid response code <" + responseCode + ">";
        }
    }

    private boolean isRedirect(int statusCode) {
        return statusCode == HttpURLConnection.HTTP_MOVED_PERM ||
                statusCode == HttpURLConnection.HTTP_MOVED_TEMP ||
                statusCode == HttpURLConnection.HTTP_SEE_OTHER;
    }

    private URL validateHttpProtocol(String url) throws MalformedURLException {
        if (url.startsWith("http:")) {
            return new URL(url.replace("http:", "https:"));
        } else {
            return setProtocol(url);
        }
    }

    private URL setProtocol(String url) throws MalformedURLException{
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            return new URL("https://" + url);
        }
    }
}
