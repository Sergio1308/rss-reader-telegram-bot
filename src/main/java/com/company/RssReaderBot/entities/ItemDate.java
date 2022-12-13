package com.company.RssReaderBot.entities;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
// todo:
public class ItemDate {
    LocalDateTime dateTime;
    LocalDate date;
    Date episodeDate;
    String episodeDateString;

    public ItemDate(String strDate) {
//        this.date = LocalDateTime.from(DateTimeFormatter.RFC_1123_DATE_TIME.parse(strDate));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yyyy");
        this.date = LocalDate.parse(strDate, formatter);
    }

    public ItemDate() { }

    public void parseDate(String sDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = null;
        try {
            date1 = formatter.parse(sDate);
            DateFormat formatterOutput = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
            this.episodeDateString = formatterOutput.format(date1);
//            return formatterOutput.format(date1);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public void formatRSSDate(String sDate) {
        DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
        Date date1 = null;
        try {
            date1 = formatter.parse(sDate);
            this.episodeDate = date1;
//            return date1;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public Date getItemDate() {
        return episodeDate;
    }

    public String getEpisodeDateString() {
        return episodeDateString;
    }

    public ItemDate(LocalDateTime date) {
        this.dateTime = date;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public LocalDate getDate() { return date; }

    public static void main(String[] args) {
//        String d = "Tue, 20 Sep 2022";
        String d = "27/09/2022";
        String pubd = "Tue, 27 Sep 2022 22:55:16 GMT";
        ItemDate itemDate = new ItemDate();
        itemDate.formatRSSDate(pubd);
        System.out.println("formatted java date: " + itemDate.getItemDate());
        Timestamp timestamp = new java.sql.Timestamp(itemDate.getItemDate().getTime());
        System.out.println("java date to sql: " + timestamp);
//        System.out.println("sql to java date: " + new java.util.Date(timestamp.getTime()));
//        String e1 = new EpisodeDate().parseDate(d);

//        LocalDateTime e1 = new ItemDate(new LocalDateTime.of()).getDate();
//        System.out.println(pubd);
//        System.out.println(e1);
//        System.out.println(pubd.contains(e1));
    }
}

