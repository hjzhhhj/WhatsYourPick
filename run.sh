#!/bin/bash

# VSCode 터미널에서 실행하는 간단한 스크립트

echo "🎮 What's Your Pick 실행 중..."

# 1. 기존 빌드 결과 삭제
rm -rf out
mkdir -p out

# 2. Java 소스 컴파일
echo "📦 컴파일 중..."
find src/main/java -name "*.java" | xargs javac -d out -encoding UTF-8

if [ $? -ne 0 ]; then
    echo "❌ 컴파일 오류!"
    exit 1
fi

# 3. 리소스 파일 복사
echo "📁 리소스 복사 중..."
cp -r src/main/resources/* out/

# 4. 프로그램 실행
echo "🚀 프로그램 시작!"
echo ""
java -cp out com.whatsyourpick.Main