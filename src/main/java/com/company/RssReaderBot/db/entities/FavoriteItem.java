package com.company.RssReaderBot.db.entities;

import com.company.RssReaderBot.models.ItemModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "favorite_items")
public class FavoriteItem {

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

    @Getter @Setter
    private String itemTitle;

    @Column(columnDefinition = "text")
    @Getter @Setter
    private String itemDescription;

    @Column(columnDefinition = "timestamp")
    @Getter @Setter
    private Date itemDate;

    @Getter @Setter
    private String itemMediaUrl;

    @Getter @Setter
    private String itemSourceLink;

    public FavoriteItem(UserDB user) {
        this.user = user;
    }

    public FavoriteItem(UserDB user, ItemModel itemModel) {
        this.user = user;
        this.itemTitle = itemModel.getTitle();
        this.itemDescription = itemModel.getDescription();
        this.itemDate = itemModel.getPubDate();
        this.itemMediaUrl = itemModel.getMediaUrl();
        this.itemSourceLink = itemModel.getSourceLink();
    }

    public FavoriteItem() {

    }

    @Override
    public String toString() {
        return "FavoriteItem{" +
                "id=" + id +
                ", user=" + user +
                ", itemTitle='" + itemTitle + '\'' +
                ", itemDescription='" + itemDescription + '\'' +
                ", itemDate=" + itemDate +
                ", itemMediaUrl='" + itemMediaUrl + '\'' +
                ", itemSourceLink='" + itemSourceLink + '\'' +
                '}';
    }
}
