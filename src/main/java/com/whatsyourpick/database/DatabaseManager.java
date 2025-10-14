package com.whatsyourpick.database;

import com.whatsyourpick.model.Category;
import com.whatsyourpick.model.Contestant;
import java.util.List;

/**
 * 데이터베이스 매니저 인터페이스
 * 나중에 실제 데이터베이스 연결 시 구현할 인터페이스입니다.
 */
public interface DatabaseManager {

    /**
     * 모든 카테고리 목록을 가져옵니다.
     * @return 카테고리 리스트
     */
    List<Category> getAllCategories();

    /**
     * 특정 카테고리의 모든 대전 후보자를 가져옵니다.
     * @param categoryId 카테고리 ID
     * @return 대전 후보자 리스트
     */
    List<Contestant> getContestantsByCategory(Long categoryId);

    /**
     * 카테고리 ID로 카테고리를 조회합니다.
     * @param categoryId 카테고리 ID
     * @return 카테고리 객체
     */
    Category getCategoryById(Long categoryId);

    /**
     * 데이터베이스 연결을 초기화합니다.
     */
    void initialize();

    /**
     * 데이터베이스 연결을 종료합니다.
     */
    void close();
}