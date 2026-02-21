# API 01: 피드 댓글 작성/삭제 (API 3~5)

## API 명세

| No | Method | URL | Auth | 상태코드 |
|----|--------|-----|------|---------|
| 3  | POST   | `/server/feeds/{feedId}/comments` | 비회원 (X-Anonymous-UUID) | 201 |
| 4  | DELETE | `/server/feeds/{feedId}/comments/{commentId}` | 비회원 (X-Anonymous-UUID) | 204 |
| 5  | DELETE | `/server/central/feeds/{feedId}/comments/{commentId}` | ROLE_CLUB | 204 |

### Request Header (API 3~4)
```
X-Anonymous-UUID: {uuid-v4}   // 필수, UUID v4 형식
```

### POST Request Body (API 3)
```json
{ "content": "댓글 내용" }
```

### POST Response Body (API 3)
```json
{
  "commentId": 1,
  "anonymousNumber": 3
}
```

### 비즈니스 규칙
- **익명 번호**: 피드별로 부여. 동일 피드에 동일 UUID 재방문 시 기존 번호 재사용.
  - 신규 UUID: 해당 피드 댓글의 MAX(anonymous_number) + 1
  - 기존 UUID: 기존 anonymous_number 반환
- **댓글 삭제 (API 4)**: UUID 검증 — 본인이 작성한 댓글만 삭제 가능
- **댓글 강제삭제 (API 5)**: ROLE_CLUB 권한으로 모든 댓글 삭제 가능 (UUID 불필요)
- content: NotBlank, 최대 500자

---

## DB 변경사항

### Flyway 마이그레이션 (신규)
`resources/db/migration/V{next}__alter_feed_comment_uuid.sql`

```sql
ALTER TABLE feed_comment
    DROP FOREIGN KEY fk_feed_comment_user,
    DROP COLUMN user_id,
    ADD COLUMN uuid VARCHAR(36) NOT NULL AFTER feed_id,
    ADD COLUMN anonymous_number INT NOT NULL AFTER uuid;
```

> **Note**: user_id 컬럼 제거 → uuid, anonymous_number 컬럼 추가. soft delete 유지.

---

## 구현할 파일 목록

### 0. `entity/FeedComment.java` (수정)
```java
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "update feed_comment set deleted_at = CURRENT_TIMESTAMP where id=?")
@SQLRestriction("deleted_at IS NULL")
public class FeedComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id", nullable = false)
    private Feed feed;

    @Column(nullable = false, length = 36)
    private String uuid;            // user_id → uuid 교체

    @Column(nullable = false)
    private int anonymousNumber;    // 피드별 익명 번호

    @Column(nullable = false, length = 500)
    private String content;

    @Column(name = "deleted_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime deletedAt;

    @Builder
    private FeedComment(Feed feed, String uuid, int anonymousNumber, String content) {
        this.feed = feed;
        this.uuid = uuid;
        this.anonymousNumber = anonymousNumber;
        this.content = content;
    }
}
```

### 1. `repository/FeedCommentRepository.java` (수정)
```java
// 피드별 UUID의 기존 anonymous_number 조회
Optional<Integer> findAnonymousNumberByFeedIdAndUuid(Long feedId, String uuid);

// 피드별 최대 anonymous_number 조회 (신규 번호 부여용)
@Query("SELECT COALESCE(MAX(fc.anonymousNumber), 0) FROM FeedComment fc WHERE fc.feed.id = :feedId")
int findMaxAnonymousNumberByFeedId(@Param("feedId") Long feedId);

long countByFeedId(Long feedId);
```

### 2. `service/FeedCommentService.java` (인터페이스)
```java
public interface FeedCommentService {
    CreateFeedCommentResult create(CreateFeedCommentCommand command);
    void delete(Long commentId, String uuid);           // 비회원 UUID 검증
    void forceDelete(Long feedId, Long commentId);      // ROLE_CLUB 강제삭제
    long countByFeedId(Long feedId);
    List<FeedCommentQuery> getAllByFeedId(Long feedId);
}
```

### 3. `service/GeneralFeedCommentService.java`
```java
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GeneralFeedCommentService implements FeedCommentService {

    private final FeedCommentRepository feedCommentRepository;
    private final FeedService feedService;

    @Override
    @Transactional
    public CreateFeedCommentResult create(CreateFeedCommentCommand command) {
        Feed feed = feedService.getById(command.feedId());

        // 익명 번호 결정
        int anonymousNumber = feedCommentRepository
                .findAnonymousNumberByFeedIdAndUuid(command.feedId(), command.uuid())
                .orElseGet(() ->
                    feedCommentRepository.findMaxAnonymousNumberByFeedId(command.feedId()) + 1
                );

        FeedComment comment = command.toEntity(feed, anonymousNumber);
        FeedComment saved = feedCommentRepository.save(comment);
        return new CreateFeedCommentResult(saved.getId(), anonymousNumber);
    }

    @Override
    @Transactional
    public void delete(Long commentId, String uuid) {
        FeedComment comment = feedCommentRepository.findById(commentId)
                .orElseThrow(FeedException.CommentNotFoundException::new);
        if (!comment.getUuid().equals(uuid)) {
            throw new FeedException.CommentAccessDeniedException();
        }
        feedCommentRepository.delete(comment);
    }

    @Override
    @Transactional
    public void forceDelete(Long feedId, Long commentId) {
        FeedComment comment = feedCommentRepository.findById(commentId)
                .orElseThrow(FeedException.CommentNotFoundException::new);
        if (!comment.getFeed().getId().equals(feedId)) {
            throw new FeedException.CommentAccessDeniedException();
        }
        feedCommentRepository.delete(comment);
    }
}
```

### 4. `service/dto/command/CreateFeedCommentCommand.java`
```java
@Builder
public record CreateFeedCommentCommand(String uuid, Long feedId, String content) {
    public FeedComment toEntity(Feed feed, int anonymousNumber) {
        return FeedComment.builder()
                .feed(feed)
                .uuid(uuid)
                .anonymousNumber(anonymousNumber)
                .content(content)
                .build();
    }
}
```

### 5. `service/dto/result/CreateFeedCommentResult.java` (신규)
```java
public record CreateFeedCommentResult(Long commentId, int anonymousNumber) {}
```

### 6. `service/dto/query/FeedCommentQuery.java` (신규)
```java
@Builder
public record FeedCommentQuery(
    Long id,
    String content,
    String anonymousName,   // "익명{anonymousNumber}" 형식
    LocalDateTime createdAt
) {
    public static FeedCommentQuery from(FeedComment comment) {
        return FeedCommentQuery.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .anonymousName("익명" + comment.getAnonymousNumber())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
```

### 7. `controller/dto/request/CreateFeedCommentRequest.java`
```java
public record CreateFeedCommentRequest(
    @NotBlank(message = "댓글 내용은 필수입니다.")
    @Size(max = 500, message = "댓글은 500자 이내로 작성해주세요.")
    String content
) {
    public CreateFeedCommentCommand toCommand(String uuid, Long feedId) {
        return CreateFeedCommentCommand.builder()
                .uuid(uuid)
                .feedId(feedId)
                .content(content)
                .build();
    }
}
```

### 8. `controller/dto/response/CreateFeedCommentResponse.java`
```java
@Builder
public record CreateFeedCommentResponse(Long commentId, int anonymousNumber) {
    public static CreateFeedCommentResponse from(CreateFeedCommentResult result) {
        return CreateFeedCommentResponse.builder()
                .commentId(result.commentId())
                .anonymousNumber(result.anonymousNumber())
                .build();
    }
}
```

### 9. `api/FeedCommentApi.java`
```java
@Tag(name = "Feed Comment", description = "피드 댓글 API (비회원)")
@RequestMapping("/server/feeds")
public interface FeedCommentApi {

    @Operation(summary = "피드 댓글 작성 API")
    @ApiResponse(responseCode = "201", description = "댓글 작성 성공",
        content = @Content(schema = @Schema(implementation = CreateFeedCommentResponse.class)))
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{feedId}/comments")
    CreateFeedCommentResponse createComment(
        @PathVariable("feedId") Long feedId,
        @RequestHeader("X-Anonymous-UUID") String uuid,
        @RequestBody @Valid CreateFeedCommentRequest request
    );

    @Operation(summary = "피드 댓글 삭제 API (비회원)")
    @ApiResponse(responseCode = "204", description = "댓글 삭제 성공")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{feedId}/comments/{commentId}")
    void deleteComment(
        @PathVariable("feedId") Long feedId,
        @PathVariable("commentId") Long commentId,
        @RequestHeader("X-Anonymous-UUID") String uuid
    );
}
```

### 10. `api/ClubFeedCommentApi.java` (신규 — CLUB 강제삭제)
```java
@Tag(name = "Feed Comment - Club", description = "동아리 피드 댓글 관리 API")
@RequestMapping("/server/central/feeds")
public interface ClubFeedCommentApi {

    @Operation(summary = "피드 댓글 강제삭제 API (동아리 회장)")
    @ApiResponse(responseCode = "204", description = "댓글 강제삭제 성공")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "AccessToken")
    @DeleteMapping("/{feedId}/comments/{commentId}")
    void forceDeleteComment(
        @PathVariable("feedId") Long feedId,
        @PathVariable("commentId") Long commentId,
        @AuthenticationPrincipal PrincipalDetails principalDetails
    );
}
```

### 11. `controller/FeedCommentController.java`
```java
@RestController
@RequiredArgsConstructor
public class FeedCommentController implements FeedCommentApi {

    private final FeedCommentService feedCommentService;

    @Override
    public CreateFeedCommentResponse createComment(Long feedId, String uuid,
            CreateFeedCommentRequest request) {
        return CreateFeedCommentResponse.from(
                feedCommentService.create(request.toCommand(uuid, feedId)));
    }

    @Override
    public void deleteComment(Long feedId, Long commentId, String uuid) {
        feedCommentService.delete(commentId, uuid);
    }
}
```

### 12. `controller/ClubFeedCommentController.java` (신규)
```java
@RestController
@RequiredArgsConstructor
public class ClubFeedCommentController implements ClubFeedCommentApi {

    private final FeedCommentService feedCommentService;

    @Override
    public void forceDeleteComment(Long feedId, Long commentId,
            PrincipalDetails principalDetails) {
        feedCommentService.forceDelete(feedId, commentId);
    }
}
```

### 13. 예외 추가: `common/exception/FeedException.java`
```java
public static final class CommentNotFoundException extends FeedException {
    private static final String MESSAGE = "존재하지 않는 댓글입니다.";
    public CommentNotFoundException() { super(MESSAGE, NOT_FOUND.value()); }
}

public static final class CommentAccessDeniedException extends FeedException {
    private static final String MESSAGE = "댓글을 삭제할 권한이 없습니다.";
    public CommentAccessDeniedException() { super(MESSAGE, FORBIDDEN.value()); }
}
```

---

## 완료 기준
- [ ] POST `/server/feeds/{feedId}/comments` 201 응답 + `{commentId, anonymousNumber}`
- [ ] 동일 UUID 재방문 시 기존 anonymousNumber 재사용
- [ ] DELETE `/server/feeds/{feedId}/comments/{commentId}` 204 응답 (UUID 일치)
- [ ] 타인 UUID로 삭제 시 403 응답
- [ ] DELETE `/server/central/feeds/{feedId}/comments/{commentId}` 204 응답 (ROLE_CLUB)
- [ ] 존재하지 않는 댓글 삭제 시 404 응답
- [ ] content 빈 값 시 400 응답
- [ ] UUID 형식 오류 시 400 응답
- [ ] Swagger UI 노출

---

## 테스트 케이스

### `FeedCommentApiTest` (통합)

| 테스트 | 조건 | 기대 결과 |
|--------|------|---------|
| 댓글 작성 성공 | 유효 UUID, 유효한 content | 201 + commentId, anonymousNumber |
| 익명 번호 신규 부여 | 피드 첫 번째 댓글 | anonymousNumber=1 |
| 익명 번호 순차 부여 | 다른 UUID 3번 댓글 | 각각 1, 2, 3 |
| 익명 번호 재사용 | 동일 UUID 재방문 | 기존 anonymousNumber 반환 |
| 댓글 작성 — content 빈 값 | `content: ""` | 400 |
| 댓글 작성 — UUID 없음 | X-Anonymous-UUID 헤더 없음 | 400 |
| 댓글 작성 — 존재하지 않는 피드 | feedId 없음 | 404 |
| 댓글 삭제 성공 | 본인 UUID | 204 |
| 댓글 삭제 — 타인 UUID | 다른 UUID | 403 |
| 댓글 삭제 — 없는 댓글 | commentId 없음 | 404 |
| 댓글 강제삭제 성공 | ROLE_CLUB 인증 | 204 |
| 댓글 강제삭제 — 미인증 | Authorization 없음 | 401 |
| 댓글 강제삭제 — CLUB 아닌 접근 | ROLE_ADMIN 토큰 | 403 |
