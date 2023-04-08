package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import model.Swipe;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 * Swipe DAO that allows us to insert swipe records into the database
 */
public class SwipeDao {
  private static BasicDataSource dataSource;

  public SwipeDao() {
    dataSource = DBCPDataSource.getDataSource();
  }


  /**
   * Inserts a list of swipes in a single batch to the database
   * @param swipes list of swipes
   */
  public void createSwipes(List<Swipe> swipes) {
    Connection conn = null;
    PreparedStatement preparedStatement = null;
    String insertQueryStatement = "INSERT INTO tinder (swiper, swipee, t ,like_or_dislike) " +
            "VALUES (?,?,?,?)";
    try {
      conn = dataSource.getConnection();
      preparedStatement = conn.prepareStatement(insertQueryStatement);
      for (Swipe swipe : swipes) {
        preparedStatement = conn.prepareStatement(insertQueryStatement);
        preparedStatement.setInt(1, swipe.getSwiper());
        preparedStatement.setInt(2, swipe.getSwipee());
        preparedStatement.setInt(3, swipe.getTime());
        preparedStatement.setBoolean(4, swipe.getSwipeDirection());
        preparedStatement.addBatch();
      }
      System.out.println(preparedStatement.toString());
      // execute insert SQL statement
      preparedStatement.executeBatch();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        if (conn != null) {
          conn.close();
        }
        if (preparedStatement != null) {
          preparedStatement.close();
        }
      } catch (SQLException se) {
        se.printStackTrace();
      }
    }
  }
}
