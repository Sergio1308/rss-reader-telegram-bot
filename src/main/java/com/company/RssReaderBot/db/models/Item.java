package com.company.RssReaderBot.db.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(
        name = "item",
        uniqueConstraints = {
                @UniqueConstraint(name = "item_userid_unique", columnNames = "userid"),
                @UniqueConstraint(name = "item_title_unique", columnNames = "title")
        }
)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private int id;

    @JoinColumn(name = "userid", nullable = false)
    @Getter
    private Long userid;

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

    public Item(Long userid) {
        this.userid = userid;
    }

    public Item() {
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", userid=" + userid +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", mediaUrl='" + mediaUrl + '\'' +
                ", sourceLink='" + sourceLink + '\'' +
                '}';
    }
}
