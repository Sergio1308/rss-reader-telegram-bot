package com.company.RssReaderBot.db.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(
        name = "rss_feed",
        uniqueConstraints = {
                @UniqueConstraint(name = "rss_feed_userid_unique", columnNames = "userid"),
                @UniqueConstraint(name = "rss_feed_url_unique", columnNames = "url"),
                @UniqueConstraint(name = "rss_feed_title_unique", columnNames = "title")
        }
)
public class RssFeed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private int id;

    @JoinColumn(name = "userid")
    @Getter
    private Long userid;

    @Column(columnDefinition = "varchar(1000)")
    @Getter @Setter
    private String url;

    @Getter @Setter
    private String title;

    @Column(columnDefinition = "text")
    @Getter @Setter
    private String customTitle;

    @Column(columnDefinition = "text")
    @Getter @Setter
    private String customHashtags;

    @Column(columnDefinition = "boolean default true")
    @Getter @Setter
    private boolean posting;

    @Column(columnDefinition = "integer default 15")
    @Getter @Setter
    private int interval;

    @Getter @Setter
    private String lastEnteredTitle;

    public RssFeed(Long userid) {
        this.userid = userid;
    }

    public RssFeed() {
    }

    @Override
    public String toString() {
        return "RssFeed{" +
                "id=" + id +
                ", userid=" + userid +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", customTitle='" + customTitle + '\'' +
                ", customHashtags='" + customHashtags + '\'' +
                ", posting=" + posting +
                ", interval=" + interval +
                ", lastEnteredTitle='" + lastEnteredTitle + '\'' +
                '}';
    }
}
