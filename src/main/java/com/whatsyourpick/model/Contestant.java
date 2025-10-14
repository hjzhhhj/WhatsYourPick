package com.whatsyourpick.model;

/**
 * 대전 후보자 모델 클래스
 * 데이터베이스의 Contestant 테이블과 매핑됩니다.
 */
public class Contestant {
    private Long id;
    private String name;
    private String imagePath;
    private Long categoryId;

    public Contestant() {
    }

    public Contestant(Long id, String name, String imagePath, Long categoryId) {
        this.id = id;
        this.name = name;
        this.imagePath = imagePath;
        this.categoryId = categoryId;
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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "Contestant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", categoryId=" + categoryId +
                '}';
    }
}