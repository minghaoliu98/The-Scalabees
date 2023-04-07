package dao;

import org.apache.commons.dbcp2.BasicDataSource;

/**
 * Establishes connection to the database
 */
public class DBCPDataSource {
  private static BasicDataSource dataSource;

  // NEVER store sensitive information below in plain text!
  private static final String AWS = "db.czqeoccmut85.us-west-2.rds.amazonaws.com";
  private static final String PORT = "3306";
  private static final String DATABASE = "sys";
  private static final String ADMIN = "admin";
  private static final String PASSWORD = "password";

  static {
    // https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-reference-jdbc-url-format.html
    dataSource = new BasicDataSource();
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    String url = String.format("jdbc:mysql://%s:%s/%s?serverTimezone=UTC&rewriteBatchedStatements=true", AWS, PORT , DATABASE);
    dataSource.setUrl(url);
    dataSource.setUsername(ADMIN);
    dataSource.setPassword(PASSWORD);
    dataSource.setInitialSize(10);
    dataSource.setMaxTotal(30);
  }

  public static BasicDataSource getDataSource() {
    return dataSource;
  }
}

