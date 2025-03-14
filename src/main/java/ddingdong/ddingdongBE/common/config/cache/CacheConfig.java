package ddingdong.ddingdongBE.common.config.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.service.FormService;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableCaching
@RequiredArgsConstructor
public class CacheConfig {

    private final FormService formService;

    private static final Pattern FORM_CACHE_PATTERN = Pattern.compile("^form_\\d+.*$");
    private static final Pattern FORM_SECTION_CACHE_PATTERN = Pattern.compile("^formSection_\\d+$");

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        List<CaffeineCache> caches = new ArrayList<>();

        caches.add(new CaffeineCache("clubsCache",
                Caffeine.newBuilder()
                        .expireAfterWrite(1, TimeUnit.DAYS)
                        .maximumSize(100)
                        .recordStats()
                        .build()));
        caches.add(new CaffeineCache("formsCache",
                Caffeine.newBuilder()
                        .expireAfter(new FormDynamicExpiry())
                        .maximumSize(200)
                        .recordStats()
                        .build()));
        caches.add(new CaffeineCache("formSectionsCache",
                Caffeine.newBuilder()
                        .expireAfter(new FormDynamicExpiry())
                        .maximumSize(100)
                        .recordStats()
                        .build()));

        cacheManager.setCaches(caches);
        return cacheManager;
    }

    public class FormDynamicExpiry implements Expiry<Object, Object> {

        @Override
        public long expireAfterCreate(Object key, Object value, long currentTime) {
            return calculateExpiryNanos(key);
        }

        @Override
        public long expireAfterUpdate(Object key, Object value, long currentTime, long currentDuration) {
            return calculateExpiryNanos(key);
        }

        @Override
        public long expireAfterRead(Object key, Object value, long currentTime, long currentDuration) {
            return currentDuration;
        }

        private long calculateExpiryNanos(Object key) {
            Long formId = extracFormId(key);
            if (formId == null) {
                return TimeUnit.HOURS.toNanos(1); // 기본값: 1시간
            }
            try {
                Form form = formService.getById(formId);
                LocalDate endDate = form.getEndDate();

                LocalDateTime now = LocalDateTime.now();
                LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

                Duration duration = Duration.between(now, endDateTime);
                return Math.min(duration.toNanos(), TimeUnit.DAYS.toNanos(1));

            } catch (Exception e) {
                return TimeUnit.HOURS.toNanos(1);
            }
        }

        /**
         * 캐시 키에서 폼 ID 추출
         */
        private Long extracFormId(Object key) {
            if (key instanceof String cacheKey) {
                if (FORM_CACHE_PATTERN.matcher(cacheKey).matches() ||
                        FORM_SECTION_CACHE_PATTERN.matcher(cacheKey).matches()) {
                    return extract(cacheKey);
                }
            }
            return null;
        }

        private Long extract(String key) {
            if (FORM_CACHE_PATTERN.matcher(key).matches()) {
                String[] parts = key.split("_");
                if (parts.length >= 2) {
                    try {
                        return Long.parseLong(parts[1]);
                    } catch (NumberFormatException e) {
                        return null;
                    }
                }
            }
            return null;
        }
    }
}
