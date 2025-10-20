package com.whatsyourpick.model;

/**
 * 카테고리 모델 클래스
 * 데이터베이스의 Category 테이블과 매핑됩니다.
 */
public class Category {
    private Long id;
    private String name;
    private String imagePath;
    private int contestantCount; // 카테고리별 후보자 수

    public Category() {
    }

    public Category(Long id, String name, String imagePath) {
        this.id = id;
        this.name = name;
        this.imagePath = imagePath;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getContestantCount() {
        return contestantCount;
    }

    public void setContestantCount(int contestantCount) {
        this.contestantCount = contestantCount;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}