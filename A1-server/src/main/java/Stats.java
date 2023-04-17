import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.Arrays;

public class Stats extends HttpServlet {
    private static Connection conn;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        int id = Integer.valueOf(req.getPathInfo().substring(1));
        String result = "Some thing wrong";
        res.setContentType("text/plain");
        try {
            PreparedStatement preparedStmt = conn.prepareStatement("SELECT count(*)  as c, like_or_dislike as l From tinder WHERE swiper = ? GROUP BY like_or_dislike ORDER BY like_or_dislike;");
            preparedStmt.setInt (1, id);
            ResultSet rs  = preparedStmt.executeQuery();
            int dislikeCount = 0;
            int likeCount = 0;
            while (rs.next()) {
                if (rs.getBoolean("l")) {
                    likeCount = rs.getInt("c");
                } else {
                    dislikeCount = rs.getInt("c");
                }
            }
            result = id + " has " + likeCount + " likes | " + dislikeCount + " dislikes";
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        res.getWriter().write(result);
        res.setStatus(HttpServletResponse.SC_OK);

    }
    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String MYSQL_URL = "db-machts.czqeoccmut85.us-west-2.rds.amazonaws.com";
            conn = DriverManager.getConnection("jdbc:mysql://" + MYSQL_URL + "/sys", "admin", "password");
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
