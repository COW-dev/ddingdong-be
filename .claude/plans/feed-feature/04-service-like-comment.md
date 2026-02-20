# Task 04: 좋아요/댓글 Service 레이어

## Overview
피드 좋아요 생성/취소, 피드 댓글 작성/삭제를 위한 Service 레이어를 구현한다.

## Dependencies
- Task 03 (Repository)

## Files to Create/Modify

### 1. 생성: `domain/feed/service/FeedLikeService.java` (인터페이스)

```java
public interface FeedLikeService {

    void create(Long feedId, Long userId);

    void delete(Long feedId, Long userId);

    long countByFeedId(Long feedId);

    boolean existsByFeedIdAndUserId(Long feedId, Long userId);
}
```

### 2. 생성: `domain/feed/service/GeneralFeedLikeService.java`

```java
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GeneralFeedLikeService implements FeedLikeService {

    private final FeedLikeRepository feedLikeRepository;
    private final FeedService feedService;

    @Override
    @Transactional
    public void create(Long feedId, Long userId) {
        // 중복 체크
        if (feedLikeRepository.existsByFeedIdAndUserId(feedId, userId)) {
            throw new InvalidParamException("이미 좋아요한 피드입니다.");
        }
        Feed feed = feedService.getById(feedId);
        FeedLike feedLike = FeedLike.builder()
                .feed(feed)
                .user(User.builder().id(userId).build())  // 또는 UserService로 조회
                .build();
        feedLikeRepository.save(feedLike);
    }

    @Override
    @Transactional
    public void delete(Long feedId, Long userId) {
        feedLikeRepository.deleteByFeedIdAndUserId(feedId, userId);
    }

    @Override
    public long countByFeedId(Long feedId) {
        return feedLikeRepository.countByFeedId(feedId);
    }

    @Override
    public boolean existsByFeedIdAndUserId(Long feedId, Long userId) {
        return feedLikeRepository.existsByFeedIdAndUserId(feedId, userId);
    }
}
```

### 3. 생성: `domain/feed/service/FeedCommentService.java` (인터페이스)

```java
public interface FeedCommentService {

    Long create(FeedComment feedComment);

    void delete(Long commentId);

    FeedComment getById(Long commentId);

    List<FeedComment> getAllByFeedId(Long feedId);

    long countByFeedId(Long feedId);
}
```

### 4. 생성: `domain/feed/service/GeneralFeedCommentService.java`

```java
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GeneralFeedCommentService implements FeedCommentService {

    private final FeedCommentRepository feedCommentRepository;

    @Override
    @Transactional
    public Long create(FeedComment feedComment) {
        FeedComment saved = feedCommentRepository.save(feedComment);
        return saved.getId();
    }

    @Override
    @Transactional
    public void delete(Long commentId) {
        FeedComment comment = getById(commentId);
        feedCommentRepository.delete(comment);  // soft delete
    }

    @Override
    public FeedComment getById(Long commentId) {
        return feedCommentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFound("FeedComment(id: " + commentId + ")를 찾을 수 없습니다."));
    }

    @Override
    public List<FeedComment> getAllByFeedId(Long feedId) {
        return feedCommentRepository.findAllByFeedIdOrderByCreatedAtAsc(feedId);
    }

    @Override
    public long countByFeedId(Long feedId) {
        return feedCommentRepository.countByFeedId(feedId);
    }
}
```

### 5. DTO 생성

- `domain/feed/service/dto/command/CreateFeedCommentCommand.java`

```java
public record CreateFeedCommentCommand(
    User user,
    Long feedId,
    String content
) {
    public FeedComment toEntity(Feed feed) {
        return FeedComment.builder()
                .feed(feed)
                .user(user)
                .content(content)
                .build();
    }
}
```

## Acceptance Criteria
- [ ] FeedLikeService: create(중복 방지), delete(hard delete), countByFeedId 구현
- [ ] FeedCommentService: create, delete(soft delete), getById, getAllByFeedId, countByFeedId 구현
- [ ] 좋아요 중복 생성 시 적절한 예외 발생
- [ ] FixZoneComment 서비스 패턴 일관성 유지

## Notes
- FeedLike의 delete는 hard delete (물리 삭제) - unique constraint 때문
- FeedComment의 delete는 soft delete (@SQLDelete 활용)
- 예외 클래스는 기존 프로젝트의 `PersistenceException.ResourceNotFound`와 유사한 커스텀 예외 활용
