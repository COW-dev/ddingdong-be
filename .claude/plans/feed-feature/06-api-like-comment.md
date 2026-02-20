# Task 06: 좋아요/댓글 API (Controller + Api interface)

## Overview
피드 좋아요 생성/취소, 댓글 작성/삭제 API를 구현한다. 인증된 사용자만 접근 가능하다.

## Dependencies
- Task 04 (Like/Comment Service)

## Files to Create/Modify

### 1. 생성: `domain/feed/api/FeedLikeApi.java`

```java
@Tag(name = "Feed Like", description = "피드 좋아요 API")
@RequestMapping("/server/feeds")
public interface FeedLikeApi {

    @Operation(summary = "피드 좋아요 API")
    @ApiResponse(responseCode = "201", description = "피드 좋아요 성공")
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "AccessToken")
    @PostMapping("/{feedId}/likes")
    void createLike(
        @PathVariable("feedId") Long feedId,
        @AuthenticationPrincipal PrincipalDetails principalDetails
    );

    @Operation(summary = "피드 좋아요 취소 API")
    @ApiResponse(responseCode = "204", description = "피드 좋아요 취소 성공")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "AccessToken")
    @DeleteMapping("/{feedId}/likes")
    void deleteLike(
        @PathVariable("feedId") Long feedId,
        @AuthenticationPrincipal PrincipalDetails principalDetails
    );
}
```

### 2. 생성: `domain/feed/controller/FeedLikeController.java`

```java
@RestController
@RequiredArgsConstructor
public class FeedLikeController implements FeedLikeApi {

    private final FeedLikeService feedLikeService;

    @Override
    public void createLike(Long feedId, PrincipalDetails principalDetails) {
        feedLikeService.create(feedId, principalDetails.getUser().getId());
    }

    @Override
    public void deleteLike(Long feedId, PrincipalDetails principalDetails) {
        feedLikeService.delete(feedId, principalDetails.getUser().getId());
    }
}
```

### 3. 생성: `domain/feed/api/FeedCommentApi.java`

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

### 4. 생성: `domain/feed/controller/FeedCommentController.java`

### 5. 생성: `domain/feed/controller/dto/request/CreateFeedCommentRequest.java`

```java
public record CreateFeedCommentRequest(
    @NotBlank(message = "댓글 내용은 필수입니다.")
    @Size(max = 500, message = "댓글은 500자 이내로 작성해주세요.")
    String content
) {
    public CreateFeedCommentCommand toCommand(User user, Long feedId) {
        return new CreateFeedCommentCommand(user, feedId, content);
    }
}
```

## URL Routing

| Method | URL | Description | Auth |
|--------|-----|-------------|------|
| POST | `/server/feeds/{feedId}/likes` | 좋아요 | 인증 필요 |
| DELETE | `/server/feeds/{feedId}/likes` | 좋아요 취소 | 인증 필요 |
| POST | `/server/feeds/{feedId}/comments` | 댓글 작성 | 인증 필요 |
| DELETE | `/server/feeds/{feedId}/comments/{commentId}` | 댓글 삭제 | 인증 필요 |

## Acceptance Criteria
- [ ] 4개 API 엔드포인트 정상 동작
- [ ] 인증되지 않은 사용자 접근 시 401 응답
- [ ] 좋아요 중복 시 적절한 에러 응답 (400 or 409)
- [ ] 댓글 content validation (@NotBlank, @Size)
- [ ] Swagger UI에 모든 API 노출

## Notes
- `/server/feeds/**` 경로의 POST, DELETE는 현재 SecurityConfig에서 인증 필요 (anyRequest().authenticated())
- SecurityConfig에서 별도 설정 불필요 (기존 GET만 permitAll)
- 댓글 삭제 시 본인 댓글인지 검증 로직 필요 (또는 ADMIN도 삭제 가능하게 할지 결정)
