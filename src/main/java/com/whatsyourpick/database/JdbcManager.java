package com.whatsyourpick.database;

import com.whatsyourpick.model.Category;
import com.whatsyourpick.model.Contestant;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdbcManager implements DatabaseManager {

    private Connection conn;

    private static final Map<String, String> categoryImagePaths = new HashMap<>();

    static {
        categoryImagePaths.put("여자아이돌", "images/categories/female-idol.jpg");
        categoryImagePaths.put("남자아이돌", "images/categories/male-idol.jpg");
        categoryImagePaths.put("음식", "images/categories/food.jpg");
        categoryImagePaths.put("여행지", "images/categories/travel.jpg");
        categoryImagePaths.put("남자배우", "images/categories/male-actor.jpg");
        categoryImagePaths.put("여자배우", "images/categories/female-actor.jpg");
        categoryImagePaths.put("드라마", "images/categories/movie-drama.jpg");
        categoryImagePaths.put("OST", "images/categories/ost.jpg");
        categoryImagePaths.put("애니메이션", "images/categories/animation.jpg");
    }

    @Override
    public void initialize() {
        try {
            // MySQL JDBC 드라이버 로드
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/idealcup?useSSL=false&serverTimezone=Asia/Seoul";
            String user = "root";
            String password = "00000000";

            conn = DriverManager.getConnection(url, user, password);
            System.out.println("✅ 데이터베이스 연결 성공!");

        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL JDBC 드라이버를 찾을 수 없습니다: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("❌ 데이터베이스 연결 실패: " + e.getMessage());
            System.err.println("   URL, 사용자명, 비밀번호를 확인하세요.");
            e.printStackTrace();
        }
    }

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        
        if (conn == null) {
            System.err.println("❌ 데이터베이스 연결이 없습니다.");
            return categories;
        }
        
        String sql = "SELECT category, COUNT(*) as count FROM contestant GROUP BY category";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            long id = 1L;
            while (rs.next()) {
                String name = rs.getString("category");
                int count = rs.getInt("count");
                String imagePath = categoryImagePaths.getOrDefault(name, "images/categories/default.jpg");

                Category category = new Category(id++, name, imagePath);
                category.setContestantCount(count);
                categories.add(category);
                
                System.out.println("📂 카테고리 로드: " + name + " (" + count + "개)");
            }
        } catch (SQLException e) {
            System.err.println("❌ 카테고리 조회 오류: " + e.getMessage());
            e.printStackTrace();
        }
        return categories;
    }

    @Override
    public List<Contestant> getContestantsByCategory(String categoryName) {
        List<Contestant> contestants = new ArrayList<>();
        
        if (conn == null) {
            System.err.println("❌ 데이터베이스 연결이 없습니다.");
            return contestants;
        }
        
        String sql = "SELECT id, name, image_path FROM contestant WHERE category = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, categoryName);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    long id = rs.getLong("id");
                    String name = rs.getString("name");
                    String imagePath = rs.getString("image_path");

                    contestants.add(new Contestant(id, name, imagePath, null));
                }
                System.out.println("✅ " + categoryName + " 후보자 " + contestants.size() + "명 로드");
            }
        } catch (SQLException e) {
            System.err.println("❌ 후보자 조회 오류: " + e.getMessage());
            e.printStackTrace();
        }
        return contestants;
    }

    @Override
    public Category getCategoryByName(String categoryName) {
        if (conn == null) {
            System.err.println("❌ 데이터베이스 연결이 없습니다.");
            return null;
        }
        
        String sql = "SELECT COUNT(*) as count FROM contestant WHERE category = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, categoryName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt("count");
                    String imagePath = categoryImagePaths.getOrDefault(categoryName, "images/categories/default.jpg");
                    Category category = new Category(null, categoryName, imagePath);
                    category.setContestantCount(count);
                    return category;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ 카테고리 조회 오류: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void close() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("✅ 데이터베이스 연결 종료");
            }
        } catch (SQLException e) {
            System.err.println("❌ 데이터베이스 연결 종료 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }
}