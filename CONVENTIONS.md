# CONVENTIONS.md — ddingdong-be 코드 컨벤션

5개 도메인(feed, club, fixzone, form, notice) 크로스 참조 기반으로 추출한 프로젝트 컨벤션.

---

## 1. 클래스 네이밍

| 카테고리 | 패턴 | 예시 |
|---------|------|------|
| Entity | `{EntityName}` | `Feed`, `Club`, `FixZone` |
| Enum | `{Domain}{Type}` | `FeedType`, `FormStatus`, `FieldType` |
| API Interface | `{Role}{Domain}Api` | `ClubFeedApi`, `AdminNoticeApi`, `FeedApi` |
| Controller | `{Role}{Domain}Controller` | `ClubFeedController`, `CentralClubController` |
| Facade Service 인터페이스 | `Facade{Role}{Domain}Service` | `FacadeClubFeedService`, `FacadeAdminFixZoneService` |
| Facade Service 구현체 | `Facade{Role}{Domain}ServiceImpl` | `FacadeClubFeedServiceImpl` |
| Domain Service 인터페이스 | `{Domain}Service` | `FeedService`, `ClubService` |
| Domain Service 구현체 | `General{Domain}Service` | `GeneralFeedService`, `GeneralClubService` |
| Repository | `{Entity}Repository` | `FeedRepository`, `FeedLikeRepository` |
| Request DTO | `{Action}{Entity}Request` | `CreateFeedRequest`, `UpdateFixZoneRequest` |
| Response DTO | `{Context}{Entity}Response` | `MyFeedPageResponse`, `CentralMyFixZoneListResponse` |
| Command DTO | `{Action}{Entity}Command` | `CreateFeedCommand`, `UpdateFormCommand` |
| Query DTO | `{Entity}{Context}Query` | `FeedPageQuery`, `ClubFeedPageQuery` |
| Exception (부모) | `{Domain}Exception` | `FeedException`, `FormException` |
| Exception (구체) | `{Description}Exception` (static inner) | `DuplicatedFeedLikeException`, `FormPeriodException` |
| Repository Projection | `{QueryPurpose}Dto` (interface) | `MonthlyFeedRankingDto`, `ClubFeedRankingDto` |

---

## 2. 메서드 네이밍

### Service 계층 메서드

| 목적 | 패턴 | 반환 타입 | 예시 |
|------|------|---------|------|
| 생성 | `create(...)` | `Long` (id) 또는 `void` | `create(CreateFeedCommand)` |
| 단건 조회 (예외 발생) | `getById(Long id)` | Entity | `getById(feedId)` |
| 단건 조회 (Optional) | `findById(Long id)` | `Optional<Entity>` | `findById(feedId)` |
| 목록 조회 | `getAll{Entity}()` / `get{Entity}Page()` | `List` / `Slice` | `getAllByFeedId()` |
| 수정 | `update(...)` | `void` | `update(UpdateFeedCommand)` |
| 삭제 | `delete(Long id)` | `void` | `delete(feedId)` |
| 개수 조회 | `countBy{Condition}(...)` | `long` | `countByFeedId(feedId)` |
| 존재 여부 | `existsBy{Condition}(...)` | `boolean` | `existsByFeedIdAndUserId(...)` |

### Entity 비즈니스 메서드

| 목적 | 패턴 | 예시 |
|------|------|------|
| 상태 변경 | `updateTo{Status}()` | `updateToComplete()` |
| 필드 업데이트 | `update{Entity}Info(...)` | `updateClubInfo(club)` |
| 조회수 증가 | `increment{Field}()` | `incrementViewCount()` |
| Boolean 판단 | `is{Condition}()` / `has{Condition}()` | `isImage()`, `hasInterview()` |
| 양방향 관계 편의 | `set{Entity}ForConvenience(...)` | `setClubForConvenience(club)` |
| 컬렉션 추가 | `add{Child}(...)` | `addClubMember(member)` |

### DTO 변환 메서드

| 방향 | 패턴 | 예시 |
|------|------|------|
| Request → Command | `toCommand(User user)` | `request.toCommand(user)` |
| Command → Entity | `toEntity(RelatedEntity)` | `command.toEntity(club)` |
| Query/Entity → Response | `Response.from(query)` | `MyFeedPageResponse.from(query)` |
| 빈 객체 생성 | `createEmpty()` | `MyFeedPageQuery.createEmpty()` |
| 팩토리 | `of(...)` | `PagingQuery.of(cursorId, list, hasNext)` |

---

## 3. 변수 및 필드 네이밍

| 대상 | 패턴 | 예시 |
|------|------|------|
| 엔티티 필드 | camelCase, 약어 없음 | `activityContent`, `viewCount` |
| FK 필드명 | `{entityName}Id` | `feedId`, `userId`, `clubId` |
| Boolean 필드 | `is{State}` / `has{Noun}` | `isCompleted`, `hasInterview` |
| 시간 필드 (DateTime) | `{action}At` | `createdAt`, `updatedAt`, `deletedAt` |
| 날짜 필드 (Date) | `{noun}Date` | `startDate`, `endDate` |
| 컬렉션 필드 | 복수형 | `clubMembers`, `formFields` |
| 상수 | `UPPER_SNAKE_CASE` | `DDINDONG_SERVICE_CLUB_URL` |

---

## 4. 패키지 구조

```
domain/{domain}/
├── api/
│   └── {Role}{Domain}Api.java          # Swagger 계약 인터페이스
├── controller/
│   ├── {Role}{Domain}Controller.java
│   └── dto/
│       ├── request/
│       │   └── {Action}{Entity}Request.java
│       └── response/
│           └── {Context}{Entity}Response.java
├── entity/
│   ├── {Entity}.java
│   └── {EntityEnum}.java
├── service/
│   ├── Facade{Role}{Domain}Service.java
│   ├── Facade{Role}{Domain}ServiceImpl.java
│   ├── {Domain}Service.java             # 도메인 서비스 인터페이스
│   ├── General{Domain}Service.java      # 도메인 서비스 구현체
│   └── dto/
│       ├── command/
│       │   └── {Action}{Entity}Command.java
│       └── query/
│           └── {Entity}{Context}Query.java
└── repository/
    ├── {Entity}Repository.java
    └── dto/
        └── {QueryPurpose}Dto.java       # native query projection (interface)
```

---

## 5. 어노테이션 사용 규칙

### Entity 클래스 (필수 어노테이션)

```java
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "update {table} set deleted_at = CURRENT_TIMESTAMP where id=?")
@SQLRestriction("deleted_at IS NULL")
public class Xxx extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String field;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_id", nullable = false)
    private Related related;

    @Column(name = "deleted_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime deletedAt;

    @Builder
    private Xxx(Long id, String field, Related related) { ... }
}
```

- `@Builder`는 **생성자**에 붙인다 (클래스에 붙이지 않음)
- 생성자 접근자는 `private`
- 모든 연관관계는 `FetchType.LAZY`

### Service 어노테이션

```java
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)      // 클래스 레벨: 읽기 전용 기본
public class GeneralXxxService implements XxxService {

    @Override
    @Transactional                    // 메서드 레벨: 쓰기 작업만 오버라이드
    public void create(...) { ... }

    @Override
    public Xxx getById(Long id) { ... }  // readOnly 상속
}
```

### API 인터페이스 어노테이션

```java
@Tag(name = "{Domain} - {Role}", description = "{설명}")
@RequestMapping("/server/{base-path}")
public interface XxxApi {

    @Operation(summary = "...")
    @ApiResponse(responseCode = "200", description = "...",
        content = @Content(schema = @Schema(implementation = XxxResponse.class)))
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{path}")
    XxxResponse getXxx(@PathVariable("id") Long id);
}
```

### HTTP 상태 코드 기준

| HTTP 메서드 | 상태 코드 |
|-----------|---------|
| POST (생성) | 201 CREATED |
| GET (조회) | 200 OK |
| PUT / PATCH (수정) | 204 NO_CONTENT |
| DELETE (삭제) | 204 NO_CONTENT |

---

## 6. DTO 작성 규칙

- 모든 DTO는 **Java record** 사용
- Request DTO: `@NotNull`, `@Min` 등 검증 어노테이션 포함, 메시지는 한국어
- Command / Query / Response DTO: `@Builder` 포함 (from/of 메서드 활용)
- 중첩 record는 outer record 내부에 정의 가능

```java
// Request DTO
public record CreateFeedRequest(
    @NotNull(message = "activityContent는 null이 될 수 없습니다.")
    String activityContent,

    @NotNull(message = "feedType는 null이 될 수 없습니다.")
    FeedType feedType
) {
    public CreateFeedCommand toCommand(User user) { ... }
}

// Command DTO
@Builder
public record CreateFeedCommand(User user, String activityContent, FeedType feedType) {
    public Feed toEntity(Club club) { ... }
}

// Query DTO
@Builder
public record FeedPageQuery(List<FeedListQuery> feeds, PagingQuery paging) {
    public static FeedPageQuery of(List<FeedListQuery> feeds, PagingQuery paging) { ... }
    public static FeedPageQuery createEmpty() { ... }
}

// Response DTO
@Builder
public record FeedPageResponse(List<FeedListResponse> feeds, PagingResponse paging) {
    public static FeedPageResponse from(FeedPageQuery query) { ... }
}
```

---

## 7. 예외 처리 규칙

```java
// 도메인별 예외 — 부모 클래스
public class FeedException extends CustomException {
    public FeedException(String message, int errorCode) {
        super(message, errorCode);
    }

    // 구체적 예외 — static final inner class
    public static final class DuplicatedFeedLikeException extends FeedException {
        private static final String MESSAGE = "이미 좋아요한 피드입니다.";
        public DuplicatedFeedLikeException() {
            super(MESSAGE, BAD_REQUEST.value());
        }
    }
}

// 사용
throw new FeedException.DuplicatedFeedLikeException();
```

| 예외 유형 | 패턴 | HTTP 코드 |
|---------|------|---------|
| 리소스 없음 | `PersistenceException.ResourceNotFound` | 404 |
| 중복 생성 | `{Domain}Exception.Duplicated{Entity}Exception` | 400 |
| 기간/유효성 | `{Domain}Exception.{Entity}PeriodException` | 400 |
| 권한 없음 | `{Domain}Exception.{Entity}AccessDeniedException` | 403 |

---

## 8. Repository 쿼리 규칙

- 단순 조건 조회: Spring Data JPA 메서드명 (`findByXxxAndYyy`)
- 복잡 조회 (JOIN, 집계, 서브쿼리): `@Query(nativeQuery = true)` 사용
- native query 결과가 여러 컬럼: **interface projection** (`repository/dto/` 하위에 정의)
- QueryDSL: `*RepositoryImpl.java` 에서 구현

---

## 9. 인증 패턴

```java
// API 인터페이스 — 인증 필요 표시
@SecurityRequirement(name = "AccessToken")
@PostMapping("/my/feeds")
void createFeed(
    @RequestBody @Valid CreateFeedRequest request,
    @AuthenticationPrincipal PrincipalDetails principalDetails
);

// Controller — 사용자 ID 추출
@Override
public void createFeed(CreateFeedRequest request, PrincipalDetails principalDetails) {
    Long userId = principalDetails.getId();
    User user = principalDetails.getUser();
    facadeService.create(request.toCommand(user));
}
```

---

## 10. 불일치 & 주의사항

| 항목 | 현황 | 신규 코드 권장 |
|------|------|-------------|
| Service Impl 네이밍 | Impl 유무 혼재 | **항상 Impl suffix 붙이기** |
| Exception 메시지 | 상수 vs 인라인 혼재 | **`private static final String MESSAGE`로 상수화** |
| Boolean 필드 네이밍 | `is` / `has` 혼재 | **엔티티 상태 → `is`, 보유 여부 → `has`** |
| FeedLike soft delete | unique constraint 충돌 | **FeedLike만 예외적으로 hard delete** |
