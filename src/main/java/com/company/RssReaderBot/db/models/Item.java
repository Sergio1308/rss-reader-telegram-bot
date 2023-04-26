package com.company.RssReaderBot.db.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(
        name = "item",
        uniqueConstraints = {
                @UniqueConstraint(name = "item_favorites_item_id_unique", columnNames = "favorites_item_id"),
                @UniqueConstraint(name = "item_title_unique", columnNames = "title")
        }
)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Integer id;

    @JoinColumn(name = "favorites_item_id", referencedColumnName = "id")
    @OneToOne
    @Getter
    private FavoritesItem favoritesItem;

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
                ", favoritesItem=" + favoritesItem +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", mediaUrl='" + mediaUrl + '\'' +
                ", sourceLink='" + sourceLink + '\'' +
                '}';
    }
}
