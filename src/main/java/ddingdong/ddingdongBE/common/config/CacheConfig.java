package ddingdong.ddingdongBE.common.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("clubsCache");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.DAYS)
                .maximumSize(100)
                .recordStats());
        return cacheManager;
    }

}
