package com.company.RssReaderBot.db.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(
        name = "rss_feed",
        uniqueConstraints = {
                @UniqueConstraint(name = "rss_feed_url_unique", columnNames = "url"),
        }
)
public class RssFeed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userid")
    @Getter
    private UserDB user;

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
    private boolean posting = true;

    @Column(columnDefinition = "integer default 15")
    @Getter @Setter
    private int interval = 15;

    @Getter @Setter
    private String lastEnteredTitle;

    public RssFeed(UserDB user, String url, String title) {
        this.user = user;
        this.url = url;
        this.title = title;
    }

    public RssFeed() {
        super();
    }

    @Override
    public String toString() {
        return "RssFeed{" +
                "id=" + id +
                ", user=" + user +
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
