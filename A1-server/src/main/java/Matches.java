import com.rabbitmq.client.ConnectionFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

public class Matches extends HttpServlet {
    private static Connection conn;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        int id = Integer.valueOf(req.getPathInfo().substring(1));
        String result = "Some thing wrong";
        res.setContentType("text/plain");
        try {
            PreparedStatement preparedStmt = conn.prepareStatement("SELECT swiper From tinder WHERE swipee = ? AND like_or_dislike = true order BY t DESC LIMIT 100");
            preparedStmt.setInt (1, id);
            ResultSet rs  = preparedStmt.executeQuery();
            result = "User: " + id + " most recent likes: ";
            while(rs.next()) {
                result += rs.getString("swiper") + " ";
            }
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
            String MYSQL_URL = "db.czqeoccmut85.us-west-2.rds.amazonaws.com";
            conn = DriverManager.getConnection("jdbc:mysql://" + MYSQL_URL + "/sys", "admin", "password");
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
