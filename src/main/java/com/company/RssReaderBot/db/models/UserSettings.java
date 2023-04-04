package com.company.RssReaderBot.db.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(
        name = "user_settings",
        uniqueConstraints = {
                @UniqueConstraint(name = "user_settings_userid_unique", columnNames = "userid"),
        }
)
public class UserSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private int id;

    @JoinColumn(name = "userid", nullable = false)
    @Getter
    private Long userid;

    @Column(columnDefinition = "boolean default true")
    @Getter @Setter
    private boolean displayTitle;

    @Column(columnDefinition = "boolean default true")
    @Getter @Setter
    private boolean displayDescription;

    @Column(columnDefinition = "boolean default true")
    @Getter @Setter
    private boolean displayMedia;

    @Column(columnDefinition = "boolean default true")
    @Getter @Setter
    private boolean displayDate;

    @Column(columnDefinition = "boolean default true")
    @Getter @Setter
    private boolean displayLink;

    public UserSettings(Long userid) {
        this.userid = userid;
    }

    public UserSettings() {
    }

    @Override
    public String toString() {
        return "UserSettings{" +
                "id=" + id +
                ", userid=" + userid +
                ", displayTitle=" + displayTitle +
                ", displayDescription=" + displayDescription +
                ", displayMedia=" + displayMedia +
                ", displayDate=" + displayDate +
                ", displayLink=" + displayLink +
                '}';
    }
}
