package com.whatsyourpick.database;

import com.whatsyourpick.model.Category;
import com.whatsyourpick.model.Contestant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 더미 데이터 매니저
 * 실제 데이터베이스 연결 전까지 테스트용 더미 데이터를 제공합니다.
 * TODO: 실제 데이터베이스 연결 시 DatabaseManager를 구현한 새로운 클래스로 교체하세요.
 */
public class DummyDataManager implements DatabaseManager {

    private List<Category> categories;
    private List<Contestant> contestants;

    public DummyDataManager() {
        initialize();
    }

    @Override
    public void initialize() {
        // 카테고리 초기화
        categories = new ArrayList<>();
        categories.add(new Category(1L, "여자 아이돌", "images/categories/female-idol.jpg"));
        categories.add(new Category(2L, "남자 아이돌", "images/categories/male-idol.jpg"));
        categories.add(new Category(3L, "음식", "images/categories/food.jpg"));
        categories.add(new Category(4L, "여행지", "images/categories/travel.jpg"));
        categories.add(new Category(5L, "남자 배우", "images/categories/male-actor.jpg"));
        categories.add(new Category(6L, "여자 배우", "images/categories/female-actor.jpg"));
        categories.add(new Category(7L, "영화/드라마", "images/categories/movie-drama.jpg"));
        categories.add(new Category(8L, "OST", "images/categories/ost.jpg"));
        categories.add(new Category(9L, "애니메이션", "images/categories/animation.jpg"));

        // 대전 후보자 초기화 (테스트용 더미 데이터)
        contestants = new ArrayList<>();

        // 카테고리 1: 여자 아이돌 (64개 샘플)
        for (int i = 1; i <= 64; i++) {
            contestants.add(new Contestant((long) i, "여자아이돌 " + i, "images/contestants/female-idol-" + i + ".jpg", 1L));
        }

        // 카테고리 2: 남자 아이돌 (64개 샘플)
        for (int i = 1; i <= 64; i++) {
            contestants.add(new Contestant(64L + i, "남자아이돌 " + i, "images/contestants/male-idol-" + i + ".jpg", 2L));
        }

        // 카테고리 3: 음식 (64개 샘플)
        for (int i = 1; i <= 64; i++) {
            contestants.add(new Contestant(128L + i, "음식 " + i, "images/contestants/food-" + i + ".jpg", 3L));
        }

        // 카테고리 4: 여행지 (64개 샘플)
        for (int i = 1; i <= 64; i++) {
            contestants.add(new Contestant(192L + i, "여행지 " + i, "images/contestants/travel-" + i + ".jpg", 4L));
        }

        // 카테고리 5: 남자 배우 (64개 샘플)
        for (int i = 1; i <= 64; i++) {
            contestants.add(new Contestant(256L + i, "남자배우 " + i, "images/contestants/male-actor-" + i + ".jpg", 5L));
        }

        // 카테고리 6: 여자 배우 (64개 샘플)
        for (int i = 1; i <= 64; i++) {
            contestants.add(new Contestant(320L + i, "여자배우 " + i, "images/contestants/female-actor-" + i + ".jpg", 6L));
        }

        // 카테고리 7: 영화/드라마 (64개 샘플)
        for (int i = 1; i <= 64; i++) {
            contestants.add(new Contestant(384L + i, "영화/드라마 " + i, "images/contestants/movie-drama-" + i + ".jpg", 7L));
        }

        // 카테고리 8: OST (64개 샘플)
        for (int i = 1; i <= 64; i++) {
            contestants.add(new Contestant(448L + i, "OST " + i, "images/contestants/ost-" + i + ".jpg", 8L));
        }

        // 카테고리 9: 애니메이션 (64개 샘플)
        for (int i = 1; i <= 64; i++) {
            contestants.add(new Contestant(512L + i, "애니메이션 " + i, "images/contestants/animation-" + i + ".jpg", 9L));
        }
    }

    @Override
    public List<Category> getAllCategories() {
        return new ArrayList<>(categories);
    }

    @Override
    public List<Contestant> getContestantsByCategory(Long categoryId) {
        return contestants.stream()
                .filter(c -> c.getCategoryId().equals(categoryId))
                .collect(Collectors.toList());
    }

    @Override
    public Category getCategoryById(Long categoryId) {
        return categories.stream()
                .filter(c -> c.getId().equals(categoryId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void close() {
        // 더미 데이터이므로 특별한 처리가 필요 없습니다.
    }
}