# AGENTS.md — ddingdong-be

명지대학교 동아리 통합 플랫폼 백엔드. 학생·동아리 임원·총동아리연합회를 위한 동아리 정보 및 행정 처리 시스템.

---

## 기술 스택

- **언어/프레임워크**: Java 21, Spring Boot 3.4.9, Gradle
- **데이터**: MySQL, Flyway (마이그레이션), Spring Data JPA, QueryDSL
- **인증**: Spring Security, JWT (auth0 java-jwt)
- **인프라**: AWS S3 (파일), AWS SES (이메일), Docker, Elastic Beanstalk
- **테스트**: JUnit 5, TestContainers, RestAssured, Fixture Monkey
- **모니터링**: Sentry, Prometheus, Actuator

---

## 명령어

```bash
# 빌드 (테스트 스킵)
./gradlew clean build -x test

# 로컬 실행 (Docker 필요 — compose-dev.yaml)
./gradlew bootRun --args='--spring.profiles.active=local'

# 전체 테스트 (Docker 필수 — TestContainers 사용)
./gradlew test

# 특정 테스트 클래스 실행
./gradlew test --tests "*ClassName*"

# 테스트 커버리지
./gradlew test jacocoTestReport
```

---

## 프로젝트 구조

```
src/main/java/ddingdong/ddingdongBE/
├── auth/           # 인증/인가 (JWT 필터, 로그인)
├── common/         # 공통 유틸, 설정, 예외 핸들러, 필터
├── file/           # S3 파일 업로드/다운로드
├── sse/            # Server-Sent Events
└── domain/         # 핵심 비즈니스 도메인
    ├── activityreport/   # 동아리 활동보고서
    ├── banner/           # 메인 배너
    ├── club/             # 동아리 관리
    ├── clubmember/       # 동아리 회원
    ├── documents/        # 제출 서류
    ├── fixzone/          # 시설 수리 요청
    ├── form/             # 모집 지원 폼
    ├── formapplication/  # 폼 지원 내역
    ├── game/             # 로고 짝 맞추기 게임
    ├── notice/           # 공지사항
    └── user/             # 사용자 계정
```

각 도메인의 내부 패키지 구조:
```
{domain}/
├── api/                  # Swagger 인터페이스 (Contract-first)
├── controller/           # REST 컨트롤러 + request/response DTO
├── service/              # 비즈니스 로직 + command/query DTO
├── repository/           # JPA Repository (+ 복잡 쿼리 DTO)
├── entity/               # JPA 엔티티
└── infrastructure/       # 외부 시스템 연동 (필요 시)
```

---

## 아키텍처 패턴

- **계층 구조**: Controller → Service → Repository
- **Contract-first API**: `api/` 패키지에 Swagger 인터페이스 정의 후 Controller가 구현
- **DTO 분리**: Service 계층은 `command`(쓰기), `query`(읽기) DTO로 분리
- **Soft Delete**: `@SQLDelete` + `@SQLRestriction` 사용, 물리 삭제 금지
- **Facade 패턴**: 여러 도메인 서비스를 조합할 때 `Facade{Domain}Service` 사용

---

## 에이전트 행동 규칙

- 새 도메인은 반드시 위 패키지 구조를 따른다
- `api/` 인터페이스 없이 Controller 구현 금지
- 환경변수/시크릿 하드코딩 금지 — `@Value` 또는 `application.yml` 사용
- Flyway 마이그레이션 파일은 기존 파일 수정 금지, 새 파일로만 추가
- 엔티티 직접 삭제 금지 — Soft Delete 패턴 사용
- 코드 변경 후 반드시 `./gradlew test` 실행 확인
