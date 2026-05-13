package kr.hnu.ice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    private Connection conn;
    private PreparedStatement pstmt;

    private final String jdbcDriver = "org.mariadb.jdbc.Driver";
    private final String jdbcUrl = "jdbc:mariadb://localhost:3306/test";
    private final String dbUser = "root";
    private final String dbPass = "";

    public void connect() {
        try {
            Class.forName(jdbcDriver);
            conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (pstmt != null) {
                pstmt.close();
            }

            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insert(Student student) {
        connect();

        String sql = "insert into student(name, univ, birthdate, email) values(?, ?, ?, ?)";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getUniv());
            pstmt.setString(3, student.getBirthdate());
            pstmt.setString(4, student.getEmail());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public void update(Student student) {
        connect();

        String sql = "update student set name = ?, univ = ?, birthdate = ?, email = ? where id = ?";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getUniv());
            pstmt.setString(3, student.getBirthdate());
            pstmt.setString(4, student.getEmail());
            pstmt.setInt(5, student.getId());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public void delete(int id) {
        connect();

        String sql = "delete from student where id = ?";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public Student getStudent(int id) {
        connect();

        Student student = null;

        String sql = "select * from student where id = ?";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                student = new Student();

                student.setId(rs.getInt("id"));
                student.setName(rs.getString("name"));
                student.setUniv(rs.getString("univ"));
                student.setBirthdate(rs.getString("birthdate"));
                student.setEmail(rs.getString("email"));
            }

            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return student;
    }

    public List<Student> getAll() {
        connect();

        List<Student> students = new ArrayList<Student>();

        String sql = "select * from student order by id desc";

        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Student student = new Student();

                student.setId(rs.getInt("id"));
                student.setName(rs.getString("name"));
                student.setUniv(rs.getString("univ"));
                student.setBirthdate(rs.getString("birthdate"));
                student.setEmail(rs.getString("email"));

                students.add(student);
            }

            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return students;
    }
}