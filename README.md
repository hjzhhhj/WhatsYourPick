# What's Your Pick? - 이상형 월드컵

사진을 비교하며 토너먼트 방식으로 최종 이상형을 뽑는 Java Swing 기반 데스크톱 게임입니다.

## 프로젝트 개요

- **프로젝트명**: 이상형 월드컵 (What's Your Pick?)
- **기간**: 2025. 08. 12 ~ 2025. 11. 18
- **개발 환경**: IntelliJ IDEA, VSCode
- **프로그래밍 언어**: Java (Swing)

## 주요 기능

### 1. 토너먼트 진행

- 4강, 8강, 16강, 32강, 64강 중 원하는 규모 선택
- 무작위로 사진이 배치되어 라운드별로 두 장 중 하나를 선택
- 최종 1장을 남길 때까지 반복 진행

### 2. 카테고리 선택

다양한 주제를 제공합니다:

- 여자 아이돌
- 남자 아이돌
- 음식
- 여행지
- 남자 배우
- 여자 배우
- 영화/드라마
- OST
- 애니메이션

### 3. 결과 확인

- 최종 우승 사진과 정보 표시
- ANOTHER GAMES: 다른 카테고리 선택
- RESTART: 같은 카테고리로 재시작

## 프로젝트 실행 방법

### 방법 1: IntelliJ IDEA 사용 (권장)

1. IntelliJ IDEA에서 프로젝트 열기
2. 프로젝트 폴더의 `pom.xml` 파일을 Maven 프로젝트로 인식시키기
3. Maven 탭에서 `Reload All Maven Projects` 클릭 (중요!)
4. Maven 탭에서 `Lifecycle` > `clean` > `package` 실행
5. `src/main/java/com/whatsyourpick/Main.java` 우클릭 > Run 'Main.main()'

### 방법 2: Maven 명령어 사용

Maven이 설치되어 있다면:

```bash
# 프로젝트 빌드
mvn clean package

# 실행
java -jar target/whats-your-pick-1.0-SNAPSHOT.jar
```

### 방법 3: 수동 컴파일 (Maven이 없는 경우)

```bash
# 1. classes 디렉토리 생성
mkdir -p target/classes

# 2. 소스 컴파일
javac -d target/classes -cp "lib/*" -sourcepath src/main/java src/main/java/com/whatsyourpick/**/*.java

# 3. 리소스 복사
cp -r src/main/resources/* target/classes/

# 4. 실행
java -cp "target/classes:lib/*" com.whatsyourpick.Main
```

## 필수 요구사항

- Java 21 이상
- MySQL 데이터베이스 (선택사항)
- 화면 해상도: 최소 1200x800

## 문제 해결

### ❌ MySQL JDBC 드라이버를 찾을 수 없다는 오류

**원인**: Maven 의존성이 제대로 다운로드되지 않음

**해결책** (IntelliJ IDEA):
1. Maven 탭 열기 (View > Tool Windows > Maven)
2. Reload All Maven Projects 버튼 클릭 (왼쪽 위 새로고침 아이콘)
3. 프로젝트 우클릭 > Maven > Reload Project
4. Build > Rebuild Project

**해결책** (명령어):
```bash
mvn clean install
```

### ❌ 폰트 파일을 찾을 수 없다는 오류
### ❌ 배경 이미지를 찾을 수 없다는 오류

**원인**: 리소스 파일이 빌드 결과에 포함되지 않음

**해결책** (IntelliJ IDEA):
1. `src/main/resources` 폴더 우클릭
2. Mark Directory as > Resources Root
3. Build > Rebuild Project
4. 다시 실행

**해결책** (수동):
위의 "방법 3: 수동 컴파일" 섹션의 3번 단계(리소스 복사)를 실행하세요.

### 협업 시 주의사항

프로젝트를 공유할 때 다음 폴더/파일들을 **반드시 포함**해야 합니다:
- `src/` - 소스 코드 및 리소스 파일
- `lib/` - MySQL JDBC 드라이버
- `pom.xml` - Maven 설정 파일

다음 폴더는 제외해도 됩니다 (`.gitignore`에 포함됨):
- `target/` - 빌드 결과물
- `out/` - IDE 빌드 결과물
- `.idea/` - IntelliJ IDEA 설정

### 친구 컴퓨터에서 실행이 안 될 때

1. Java 21 이상이 설치되어 있는지 확인:
   ```bash
   java -version
   ```

2. IntelliJ IDEA에서 프로젝트를 열고:
   - File > Project Structure > Project > SDK를 Java 21로 설정
   - Maven 탭 > Reload All Maven Projects 클릭
   - Build > Rebuild Project

3. 그래도 안 되면 위의 "문제 해결" 섹션 참고

## 데이터베이스 설정

MySQL을 사용하려면 `src/main/java/com/whatsyourpick/database/JdbcManager.java`에서 데이터베이스 연결 정보를 수정하세요.

```java
private static final String URL = "jdbc:mysql://localhost:3306/whatsyourpick";
private static final String USER = "your_username";
private static final String PASSWORD = "your_password";
```
