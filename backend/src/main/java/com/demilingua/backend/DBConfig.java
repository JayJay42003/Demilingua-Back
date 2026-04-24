package com.demilingua.backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class DBConfig {

    // Leemos las variables del application.properties
    private static String url;
    private static String username;
    private static String password;

    @Value("${spring.datasource.url}")
    public void setUrl(String url) { DBConfig.url = url; }

    @Value("${spring.datasource.username}")
    public void setUsername(String username) { DBConfig.username = username; }

    @Value("${spring.datasource.password}")
    public void setPassword(String password) { DBConfig.password = password; }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}