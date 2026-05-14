package kr.hnu.ice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class NewsDAO {
    final String driver = "org.mariadb.jdbc.Driver";
    final String dbname = "mywebdb";
    final String url = "jdbc:mariadb://localhost:3307/" + dbname;
    final String user = "root";
    final String password = "";

    public Connection connect() throws Exception {
        Class.forName(driver);
        return DriverManager.getConnection(url, user, password);
    }

    public void addNews(News news) throws Exception {
        String sql = "insert into news(title, img, content) values(?, ?, ?)";

        try (
            Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, news.getTitle());
            pstmt.setString(2, news.getImg());
            pstmt.setString(3, news.getContent());
            pstmt.executeUpdate();
        }
    }

    public List<News> getAll() throws Exception {
        List<News> newsList = new ArrayList<News>();

        String sql = "select aid, title, img, DATE_FORMAT(date, '%Y-%m-%d %H:%i:%s') as date, content "
                   + "from news order by aid desc";

        try (
            Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()
        ) {
            while (rs.next()) {
                News news = new News();

                news.setAid(rs.getInt("aid"));
                news.setTitle(rs.getString("title"));
                news.setImg(rs.getString("img"));
                news.setDate(rs.getString("date"));
                news.setContent(rs.getString("content"));

                newsList.add(news);
            }
        }

        return newsList;
    }

    public News getNews(int aid) throws Exception {
        News news = null;

        String sql = "select aid, title, img, DATE_FORMAT(date, '%Y-%m-%d %H:%i:%s') as date, content "
                   + "from news where aid = ?";

        try (
            Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setInt(1, aid);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    news = new News();

                    news.setAid(rs.getInt("aid"));
                    news.setTitle(rs.getString("title"));
                    news.setImg(rs.getString("img"));
                    news.setDate(rs.getString("date"));
                    news.setContent(rs.getString("content"));
                }
            }
        }

        return news;
    }

    public void updateNews(News news) throws Exception {
        String sql = "update news set title = ?, img = ?, content = ? where aid = ?";

        try (
            Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, news.getTitle());
            pstmt.setString(2, news.getImg());
            pstmt.setString(3, news.getContent());
            pstmt.setInt(4, news.getAid());

            if (pstmt.executeUpdate() == 0) {
                throw new Exception("뉴스 수정 중 오류가 발생했습니다.");
            }
        }
    }

    public void delNews(int aid) throws Exception {
        String sql = "delete from news where aid = ?";

        try (
            Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setInt(1, aid);

            if (pstmt.executeUpdate() == 0) {
                throw new Exception("뉴스 삭제 중 오류가 발생했습니다.");
            }
        }
    }
}