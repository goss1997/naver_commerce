# Naver Commerce Integration Project

네이버 커머스 API와 통합하여 판매자 검증 및 주문 정보 조회, 반품 처리 등의 기능을 수행하는 Spring Boot 기반 백엔드 애플리케이션입니다.

## 📁 프로젝트 구조

```
.
├── build.gradle                # Gradle 빌드 설정
├── settings.gradle            # Gradle 프로젝트 설정
├── HELP.md                    # Spring Initializr 기본 도움말
├── src
│   ├── main
│   │   ├── java/com/naver/commerce
│   │   │   ├── controller/            # API 컨트롤러 (Auth, Commerce API 연동)
│   │   │   ├── dto/                   # 요청/응답 DTO
│   │   │   ├── entity/                # 주문/반편 관련 도메인 엔티티
│   │   │   ├── jwt/                   # JWT 토큰 검증
│   │   │   ├── repository/            # Spring Data JPA Repository
│   │   │   ├── scheduler/             # 주문 동기화 스케줄러
│   │   │   ├── service/               # 비즈니스 로직 처리
│   │   │   └── util/                  # 날짜, RSA, 서명 유틸
│   │   └── resources/application.yml  # 환경변수 설정 파일
│   └── test/java/...                  # 테스트 클래스
```

## 🚀 주요 기능

- 네이버 커먼스 API 연동
  - 주문 조회, 상세 조회
  - 주문 상태 변경 감지(반품 상태 추척)
- JWT & JWE 인증 처리 및 토큰 검증
- RSA 암호화 및 서명 생성
- 주문 동기화 스케줄러
- Spring Data JPA를 통한 DB 연동

## 🔧 실행 방법

1. 필수 환경변수 설정
   ```yaml
   naver:
     api:
       base-url: ${NAVER_API_BASE_URL}
       client-id: ${NAVER_API_CLIENT_ID}
       client-secret: ${NAVER_API_CLIENT_SECRET}
       account-id: ${NAVER_API_ACCOUNT_ID}
       public-key: ${NAVER_API_PUBLIC_KEY}
   ```

2. Gradle 빌드
   ```bash
   ./gradlew build
   ```

3. 애플리케이션 실행
   ```bash
   ./gradlew bootRun
   ```

## 📆 기술 스택

- Java 17+
- Spring Boot
- Spring Web / JPA / Scheduler
- Gradle
- Naver Commerce Open API
- JWT 인증, RSA 기반 암호화

