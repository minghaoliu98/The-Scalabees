package org.example;

import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.Statement;

public class Main {


    private static BasicDataSource dataSource = null;
    private final static String QUEUE_NAME = "Queue1";
    private final static String RABBITMQ_URL = "54.186.65.165";
    public static void main(String[] argv) throws Exception {
        String MYSQL_URL = "db-machts.czqeoccmut85.us-west-2.rds.amazonaws.com";
        dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://" + MYSQL_URL + ":3306/sys");

        dataSource.setUsername("admin");
        dataSource.setPassword("password");

        System.out.println(dataSource.getUrl());

        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) from tinder");
        System.out.println(resultSet);

    }
}