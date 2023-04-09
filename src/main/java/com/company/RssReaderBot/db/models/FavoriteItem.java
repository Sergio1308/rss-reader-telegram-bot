package com.company.RssReaderBot.db.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(
        name = "favorite_item",
        uniqueConstraints = {
                @UniqueConstraint(name = "favorite_item_userid_unique", columnNames = "userid"),
                @UniqueConstraint(name = "favorite_item_feed_id_unique", columnNames = "feed_id"),
                @UniqueConstraint(name = "favorite_item_item_id_unique", columnNames = "item_id")
        }
)
public class FavoriteItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", nullable = false)
    @Getter
    private UserDB user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    @Getter @Setter
    private RssFeed feed;

    @JoinColumn(name = "item_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @Getter @Setter
    private Item item;

    public FavoriteItem() {
        super();
    }

    @Override
    public String toString() {
        return "FavoritesItems{" +
                "id=" + id +
                ", user=" + user +
                ", feed=" + feed +
                ", item=" + item +
                '}';
    }
}
