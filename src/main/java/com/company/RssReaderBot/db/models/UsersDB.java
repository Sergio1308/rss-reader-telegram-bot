package com.company.RssReaderBot.db.models;

public class UsersDB {

    private final long userId;

    private String username;
    private String firstName;
    private String languageCode;

    public UsersDB(long userId) { this.userId = userId; } // todo remove with tester class

    public UsersDB(long userId, String languageCode) {
        this.userId = userId;
        this.languageCode = languageCode;
    }

    public long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    @Override
    public String toString() {
        return "UserDB{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", languageCode='" + languageCode + '\'' +
                '}';
    }
}
