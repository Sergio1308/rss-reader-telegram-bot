package com.company.RssReaderBot.db.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "users_username_unique", columnNames = "username")
        }
)
public class UserDB {

    @Id
    @Column(columnDefinition = "bigint")
    @Getter
    private Long userid;

    @Getter @Setter
    private String username;

    @Getter @Setter
    private String firstName;

    @Column(length = 10, nullable = false)
    @Getter @Setter
    private String languageCode;

    @Column(
            columnDefinition = "timestamp with time zone"
    )
    @CreationTimestamp
    @Getter
    private Timestamp createdAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Getter @Setter
    private UserSettings userSettings;

    public UserDB(long userId, String languageCode, Timestamp createdAt) {
        this.userid = userId;
        this.languageCode = languageCode;
        this.createdAt = createdAt;
    }

    public UserDB(Long userid, String languageCode) {
        this.userid = userid;
        this.languageCode = languageCode;
    }

    public UserDB() {

    }

    @Override
    public String toString() {
        return "UserDB{" +
                "userId=" + userid +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", languageCode='" + languageCode + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
