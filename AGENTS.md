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

## 핵심 패턴 레퍼런스

| 패턴 | 위치 예시 |
|------|-----------|
| Soft Delete | `domain/club/entity/Club.java` |
| Facade 패턴 | `domain/*/service/Facade*Service.java` |
| Swagger Contract | `domain/*/api/*Api.java` |
| Command/Query DTO | `domain/*/service/dto/command/`, `query/` |
| QueryDSL 쿼리 | `domain/*/repository/*RepositoryImpl.java` |

---

## 코드 컨벤션

> 상세 내용은 @CONVENTIONS.md 참조

### 핵심 요약

**클래스 네이밍**
- API Interface: `{Role}{Domain}Api` (예: `ClubFeedApi`, `AdminNoticeApi`)
- Controller: `{Role}{Domain}Controller`
- Facade Service: `Facade{Role}{Domain}Service` / `...ServiceImpl`
- Domain Service: `{Domain}Service` 인터페이스 + `General{Domain}Service` 구현체
- Request/Response DTO: `{Action}{Entity}Request`, `{Context}{Entity}Response`
- Command/Query DTO: `{Action}{Entity}Command`, `{Entity}{Context}Query`
- Exception: `{Domain}Exception` (부모) + static final inner class (구체 예외)

**메서드 네이밍**
- 생성: `create(Command)` → `Long` id 또는 `void`
- 단건 조회 (예외): `getById(Long id)`, 단건 조회 (Optional): `findById(Long id)`
- 삭제: `delete(Long id)`, 수정: `update(Command)`
- DTO 변환: Request→`toCommand()`, Command→`toEntity()`, Query→`Response.from()`

**어노테이션 규칙**
- `@Builder`는 생성자에, 생성자 접근자는 `private`
- 모든 연관관계는 `FetchType.LAZY`
- Service 클래스: `@Transactional(readOnly = true)`, 쓰기 메서드만 `@Transactional` 오버라이드
- HTTP 상태: POST→201, GET→200, PUT/PATCH/DELETE→204

**DTO 규칙**
- 모든 DTO는 Java `record` 사용
- Request DTO에 `@NotNull`, `@Min` 등 검증 어노테이션 + 한국어 메시지
- Command/Query/Response DTO에 `@Builder` 추가

**예외 규칙**
- `throw new {Domain}Exception.{Description}Exception()` 형태 사용
- 에러 메시지는 `private static final String MESSAGE` 상수로 선언
