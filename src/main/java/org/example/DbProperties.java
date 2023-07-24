package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DbProperties {
    private final String url;
    private final String root;
    private final String password;

    public DbProperties() {
        InputStream is = getClass().getClassLoader().getResourceAsStream("db.properties");
        Properties props = new Properties();
        try {
            props.load(is);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        url = props.getProperty("db.url");
        root = props.getProperty("db.user");
        password = props.getProperty("db.password");
    }

    public String getRoot() {
        return root;
    }

    public String getPassword() {
        return password;
    }

    public String getUrl() {
        return url;
    }
}
