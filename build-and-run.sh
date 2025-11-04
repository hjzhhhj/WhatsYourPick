#!/bin/bash

# What's Your Pick? 빌드 및 실행 스크립트 (Mac/Linux용)

echo "==================================="
echo "What's Your Pick? 빌드 및 실행"
echo "==================================="
echo ""

# Java 버전 확인
echo "[1/4] Java 버전 확인..."
if ! command -v java &> /dev/null; then
    echo "❌ Java가 설치되어 있지 않습니다."
    echo "Java 21 이상을 설치해주세요."
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
echo "✅ Java 버전: $(java -version 2>&1 | head -n 1)"

if [ "$JAVA_VERSION" -lt 21 ]; then
    echo "⚠️  Java 21 이상이 필요합니다. 현재 버전: $JAVA_VERSION"
fi
echo ""

# javac 확인
echo "[2/4] Java 컴파일러 확인..."
if ! command -v javac &> /dev/null; then
    echo "❌ javac를 찾을 수 없습니다. JDK가 설치되어 있는지 확인하세요."
    exit 1
fi
echo "✅ javac 사용 가능"
echo ""

# 디렉토리 생성
echo "[3/4] 빌드 디렉토리 준비..."
mkdir -p target/classes
echo "✅ target/classes 디렉토리 생성 완료"
echo ""

# 소스 컴파일
echo "[4/4] 소스 컴파일 중..."
find src/main/java -name "*.java" > sources.txt

javac -d target/classes \
      -cp "lib/*" \
      -sourcepath src/main/java \
      @sources.txt

if [ $? -ne 0 ]; then
    echo "❌ 컴파일 실패"
    rm sources.txt
    exit 1
fi

rm sources.txt
echo "✅ 컴파일 완료"
echo ""

# 리소스 복사
echo "리소스 파일 복사 중..."
cp -r src/main/resources/* target/classes/ 2>/dev/null
echo "✅ 리소스 복사 완료"
echo ""

# 실행
echo "==================================="
echo "프로그램 실행 중..."
echo "==================================="
echo ""

java -cp "target/classes:lib/*" com.whatsyourpick.Main

echo ""
echo "프로그램이 종료되었습니다."