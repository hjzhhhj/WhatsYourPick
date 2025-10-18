package com.whatsyourpick.util;

import java.io.*;
import java.nio.file.*;
import java.sql.*;

public class ImageExporter {

    /**
     * DB의 BLOB 이미지를 파일로 추출
     */
    public static void exportImageToFile(Connection conn, long contestantId, String outputPath)
            throws SQLException, IOException {

        String sql = "SELECT name, category, image_type, image_data FROM contestant_blob WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, contestantId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    String category = rs.getString("category");
                    String imageType = rs.getString("image_type");
                    byte[] imageData = rs.getBytes("image_data");

                    // 파일 확장자 결정
                    String extension = imageType.equals("image/jpeg") ? "jpg" :
                                     imageType.equals("image/png") ? "png" : "jpg";

                    // 출력 경로 생성
                    Path outputDir = Paths.get(outputPath, category);
                    Files.createDirectories(outputDir);

                    Path outputFile = outputDir.resolve(name + "." + extension);

                    // 파일로 저장
                    Files.write(outputFile, imageData);

                    System.out.println("이미지 저장 완료: " + outputFile);
                }
            }
        }
    }

    /**
     * 특정 카테고리의 모든 이미지 추출
     */
    public static void exportCategoryImages(Connection conn, String category, String outputPath)
            throws SQLException, IOException {

        String sql = "SELECT id, name, image_type, image_data FROM contestant_blob WHERE category = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, category);

            try (ResultSet rs = pstmt.executeQuery()) {
                Path outputDir = Paths.get(outputPath, category);
                Files.createDirectories(outputDir);

                int count = 0;
                while (rs.next()) {
                    String name = rs.getString("name");
                    String imageType = rs.getString("image_type");
                    byte[] imageData = rs.getBytes("image_data");

                    String extension = imageType.equals("image/jpeg") ? "jpg" :
                                     imageType.equals("image/png") ? "png" : "jpg";

                    Path outputFile = outputDir.resolve(name + "." + extension);
                    Files.write(outputFile, imageData);
                    count++;
                }

                System.out.println(category + " 카테고리 " + count + "개 이미지 저장 완료");
            }
        }
    }

    /**
     * 모든 이미지 추출
     */
    public static void exportAllImages(Connection conn, String outputPath) throws SQLException, IOException {
        String sql = "SELECT DISTINCT category FROM contestant_blob";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String category = rs.getString("category");
                exportCategoryImages(conn, category, outputPath);
            }
        }
    }

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/idealcup";
        String user = "root";
        String password = "your_password";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // 모든 이미지를 src/main/resources/images 폴더로 추출
            String outputPath = "src/main/resources/images";
            exportAllImages(conn, outputPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}