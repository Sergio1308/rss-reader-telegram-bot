package com.company.RssReaderBot.db.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Config {
    @Getter
    @Value("${db.url}")
    private String dbUrl;

    @Getter
    @Value("${db.host}")
    private String dbHost;

    @Getter
    @Value("${db.port}")
    private String dbPort;

    @Getter
    @Value("${db.user}")
    private String dbUser;

    @Getter
    @Value("${db.pass}")
    private String dbPass;

    @Getter
    @Value("${db.name}")
    private String dbName;

    @Getter
    @Value("${db.property}")
    private String dbProperty;
}
