package ddingdong.ddingdongBE.domain.feed.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Service;

@Service
public class FeedLikeCacheService {

    private final Cache<String, Set<Long>> likeCache = Caffeine.newBuilder()
            .expireAfterWrite(14, TimeUnit.DAYS)
            .maximumSize(100_000)
            .build();

    public boolean isLiked(String uuid, Long feedId) {
        Set<Long> likedFeeds = likeCache.getIfPresent(uuid);
        return likedFeeds != null && likedFeeds.contains(feedId);
    }

    public void addLike(String uuid, Long feedId) {
        likeCache.asMap().compute(uuid, (k, existing) -> {
            Set<Long> set = existing != null ? existing : ConcurrentHashMap.newKeySet();
            set.add(feedId);
            return set;
        });
    }

    public void removeLike(String uuid, Long feedId) {
        likeCache.asMap().computeIfPresent(uuid, (k, existing) -> {
            existing.remove(feedId);
            return existing.isEmpty() ? null : existing;
        });
    }

    public void clearAll() {
        likeCache.invalidateAll();
    }
}
