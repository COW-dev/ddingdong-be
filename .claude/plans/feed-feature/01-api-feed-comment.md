# API 01: 피드 댓글 작성/삭제

## API 명세

| Method | URL | Auth | 상태코드 |
|--------|-----|------|---------|
| POST | `/server/feeds/{feedId}/comments` | USER | 201 |
| DELETE | `/server/feeds/{feedId}/comments/{commentId}` | USER | 204 |

### POST Request Body
```json
{ "content": "댓글 내용" }
```

### 비즈니스 규칙
- 댓글 작성: 인증된 사용자 누구나 가능
- 댓글 삭제: 본인 댓글만 삭제 가능 (작성자 userId 검증)
- content: NotBlank, 최대 500자

---

## 구현할 파일 목록

### 0. `entity/FeedComment.java`
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(name = "deleted_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime deletedAt;

    @Builder
    private FeedComment(Feed feed, User user, String content) {
        this.feed = feed;
        this.user = user;
        this.content = content;
    }
}
```

### 1. `service/FeedCommentService.java` (인터페이스)
```java
public interface FeedCommentService {
    void create(CreateFeedCommentCommand command);
    void delete(Long commentId, Long userId);
}
```

### 2. `service/GeneralFeedCommentService.java`
```java
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GeneralFeedCommentService implements FeedCommentService {

    private final FeedCommentRepository feedCommentRepository;
    private final FeedService feedService;

    @Override
    @Transactional
    public void create(CreateFeedCommentCommand command) {
        Feed feed = feedService.getById(command.feedId());
        FeedComment comment = command.toEntity(feed);
        feedCommentRepository.save(comment);
    }

    @Override
    @Transactional
    public void delete(Long commentId, Long userId) {
        FeedComment comment = feedCommentRepository.findById(commentId)
                .orElseThrow(FeedException.CommentNotFoundException::new);
        if (!comment.getUser().getId().equals(userId)) {
            throw new FeedException.CommentAccessDeniedException();
        }
        feedCommentRepository.delete(comment);
    }
}
```

### 3. `service/dto/command/CreateFeedCommentCommand.java`
```java
@Builder
public record CreateFeedCommentCommand(User user, Long feedId, String content) {
    public FeedComment toEntity(Feed feed) {
        return FeedComment.builder()
                .feed(feed)
                .user(user)
                .content(content)
                .build();
    }
}
```

### 4. `controller/dto/request/CreateFeedCommentRequest.java`
```java
public record CreateFeedCommentRequest(
    @NotBlank(message = "댓글 내용은 필수입니다.")
    @Size(max = 500, message = "댓글은 500자 이내로 작성해주세요.")
    String content
) {
    public CreateFeedCommentCommand toCommand(User user, Long feedId) {
        return CreateFeedCommentCommand.builder()
                .user(user)
                .feedId(feedId)
                .content(content)
                .build();
    }
}
```

### 5. `api/FeedCommentApi.java`
```java
@Tag(name = "Feed Comment", description = "피드 댓글 API")
@RequestMapping("/server/feeds")
public interface FeedCommentApi {

    @Operation(summary = "피드 댓글 작성 API")
    @ApiResponse(responseCode = "201", description = "피드 댓글 작성 성공")
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "AccessToken")
    @PostMapping("/{feedId}/comments")
    void createComment(
        @PathVariable("feedId") Long feedId,
        @RequestBody @Valid CreateFeedCommentRequest request,
        @AuthenticationPrincipal PrincipalDetails principalDetails
    );

    @Operation(summary = "피드 댓글 삭제 API")
    @ApiResponse(responseCode = "204", description = "피드 댓글 삭제 성공")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "AccessToken")
    @DeleteMapping("/{feedId}/comments/{commentId}")
    void deleteComment(
        @PathVariable("feedId") Long feedId,
        @PathVariable("commentId") Long commentId,
        @AuthenticationPrincipal PrincipalDetails principalDetails
    );
}
```

### 6. `controller/FeedCommentController.java`
```java
@RestController
@RequiredArgsConstructor
public class FeedCommentController implements FeedCommentApi {

    private final FeedCommentService feedCommentService;

    @Override
    public void createComment(Long feedId, CreateFeedCommentRequest request,
            PrincipalDetails principalDetails) {
        feedCommentService.create(request.toCommand(principalDetails.getUser(), feedId));
    }

    @Override
    public void deleteComment(Long feedId, Long commentId,
            PrincipalDetails principalDetails) {
        feedCommentService.delete(commentId, principalDetails.getUser().getId());
    }
}
```

### 7. 예외 추가: `common/exception/FeedException.java`
```java
// 기존 FeedException에 추가
public static final class CommentNotFoundException extends FeedException {
    private static final String MESSAGE = "존재하지 않는 댓글입니다.";
    public CommentNotFoundException() { super(MESSAGE, NOT_FOUND.value()); }
}

public static final class CommentAccessDeniedException extends FeedException {
    private static final String MESSAGE = "본인이 작성한 댓글만 삭제할 수 있습니다.";
    public CommentAccessDeniedException() { super(MESSAGE, FORBIDDEN.value()); }
}
```

---

## 완료 기준
- [ ] POST `/server/feeds/{feedId}/comments` 201 응답
- [ ] DELETE `/server/feeds/{feedId}/comments/{commentId}` 204 응답
- [ ] 타인 댓글 삭제 시 403 응답
- [ ] 존재하지 않는 댓글 삭제 시 404 응답
- [ ] content 빈 값 시 400 응답
- [ ] Swagger UI 노출 확인

---

## 테스트 케이스

### `FeedCommentApiTest` (통합)

| 테스트 | 조건 | 기대 결과 |
|--------|------|---------|
| 댓글 작성 성공 | 인증 사용자, 유효한 content | 201 |
| 댓글 작성 — content 빈 값 | `content: ""` | 400 |
| 댓글 작성 — content null | `content: null` | 400 |
| 댓글 작성 — content 500자 초과 | 501자 content | 400 |
| 댓글 작성 — 미인증 | Authorization 헤더 없음 | 401 |
| 댓글 작성 — 존재하지 않는 피드 | feedId 없음 | 404 |
| 댓글 삭제 성공 | 본인 댓글 | 204 |
| 댓글 삭제 — 타인 댓글 | 작성자 != 요청자 | 403 |
| 댓글 삭제 — 존재하지 않는 댓글 | commentId 없음 | 404 |
| 댓글 삭제 — 미인증 | Authorization 헤더 없음 | 401 |

### `GeneralFeedCommentServiceTest` (단위)

| 테스트 | 검증 내용 |
|--------|---------|
| create — 정상 | FeedCommentRepository.save 호출 확인 |
| create — 피드 없음 | FeedService.getById 예외 전파 |
| delete — 본인 | repository.delete 호출 확인 |
| delete — 타인 | CommentAccessDeniedException 발생 |
| delete — 없는 댓글 | CommentNotFoundException 발생 |
| countByFeedId | 정확한 카운트 반환 |
