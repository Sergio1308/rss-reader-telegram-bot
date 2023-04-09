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
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", referencedColumnName = "userid")
    @Getter @Setter
    private UserDB user;

    @Column(columnDefinition = "boolean default true")
    @Getter @Setter
    private boolean displayTitle = true;

    @Column(columnDefinition = "boolean default true")
    @Getter @Setter
    private boolean displayDescription = true;

    @Column(columnDefinition = "boolean default true")
    @Getter @Setter
    private boolean displayMedia = true;

    @Column(columnDefinition = "boolean default true")
    @Getter @Setter
    private boolean displayDate = true;

    @Column(columnDefinition = "boolean default true")
    @Getter @Setter
    private boolean displayLink = true;

    public UserSettings(UserDB user) {
        this.user = user;
    }

    public UserSettings() {
        super();
    }

    @Override
    public String toString() {
        return "UserSettings{" +
                "id=" + id +
                ", user=" + user +
                ", displayTitle=" + displayTitle +
                ", displayDescription=" + displayDescription +
                ", displayMedia=" + displayMedia +
                ", displayDate=" + displayDate +
                ", displayLink=" + displayLink +
                '}';
    }
}
