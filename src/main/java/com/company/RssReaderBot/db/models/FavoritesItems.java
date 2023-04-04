package com.company.RssReaderBot.db.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(
        name = "favorites_items",
        uniqueConstraints = {
                @UniqueConstraint(name = "favorites_items_userid_unique", columnNames = "userid"),
                @UniqueConstraint(name = "favorites_items_feed_id_unique", columnNames = "feedId"),
                @UniqueConstraint(name = "favorites_items_item_id_unique", columnNames = "itemId")
        }
)
public class FavoritesItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private int id;

    @JoinColumn(name = "userid", nullable = false)
    @Getter
    private Long userid;

    @JoinColumn(name = "feed_id")
    @Getter @Setter
    private int feedId;

    @JoinColumn(name = "item_id")
    @Getter @Setter
    private int itemId;

    public FavoritesItems(Long userid) {
        this.userid = userid;
    }

    public FavoritesItems() {

    }

    @Override
    public String toString() {
        return "FavoritesItems{" +
                "id=" + id +
                ", userid=" + userid +
                ", feedId=" + feedId +
                ", item_id=" + itemId +
                '}';
    }
}
