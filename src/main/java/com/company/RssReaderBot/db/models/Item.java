package com.company.RssReaderBot.db.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(
        name = "item",
        uniqueConstraints = {
                @UniqueConstraint(name = "item_feed_id_unique", columnNames = "feed_id"),
                @UniqueConstraint(name = "item_title_unique", columnNames = "title")
        }
)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private int id;

    @JoinColumn(name = "feed_id", referencedColumnName = "id")
    @ManyToOne
    @Getter
    private RssFeed rssFeed;

    @Getter @Setter
    private String title;

    @Column(columnDefinition = "text")
    @Getter @Setter
    private String description;

    @Column(columnDefinition = "timestamp")
    @Getter @Setter
    private String date;

    @Getter @Setter
    private String mediaUrl;

    @Getter @Setter
    private String sourceLink;

    public Item() {
        super();
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", feedId=" + rssFeed.getId() +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", mediaUrl='" + mediaUrl + '\'' +
                ", sourceLink='" + sourceLink + '\'' +
                '}';
    }
}
