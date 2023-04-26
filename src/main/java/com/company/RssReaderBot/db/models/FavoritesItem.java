package com.company.RssReaderBot.db.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(
        name = "favorites_item",
        uniqueConstraints = {
                @UniqueConstraint(name = "favorites_item_userid_unique", columnNames = "userid"),
                @UniqueConstraint(name = "favorites_item_feed_id_unique", columnNames = "id"),
        }
)
public class FavoritesItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", nullable = false)
    @Getter
    private UserDB user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id", referencedColumnName = "id")
    @Getter @Setter
    private RssFeed feed;

    @OneToOne(mappedBy = "favoritesItem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Getter @Setter
    private Item item;

    public FavoritesItem() {
        super();
    }

    @Override
    public String toString() {
        return "FavoritesItems{" +
                "id=" + id +
                ", user=" + user +
                ", feed=" + feed +
                '}';
    }
}
