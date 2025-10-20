DROP DATABASE IF EXISTS idealcup;
CREATE DATABASE idealcup CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE idealcup;

-- 참가자 테이블
CREATE TABLE contestant (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(120) NOT NULL,
    category ENUM(
        '남자배우',
        '여자배우',
        '남자아이돌',
        '여자아이돌',
        '여행지',
        '드라마', 
        '음식',
        '애니메이션'
    ) NOT NULL,
    image_path VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_name_category (name, category),
    KEY idx_category (category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

SHOW TABLES;
DESCRIBE contestant;