package com.company.RssReaderBot.db.entities;

import com.company.RssReaderBot.models.ItemModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

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
    private Date date;

    @Getter @Setter
    private String mediaUrl;

    @Getter @Setter
    private String sourceLink;

    public Item(FavoritesItem favoritesItem, ItemModel itemModel) {
        this.favoritesItem = favoritesItem;
        this.title = itemModel.getTitle();
        this.description = itemModel.getDescription();
        this.date = itemModel.getPubDate();
        this.mediaUrl = itemModel.getMediaUrl();
        this.sourceLink = itemModel.getGuid();
    }

    public Item() {

    }

    @Override
    public String toString() {
        return "ItemModel{" +
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
