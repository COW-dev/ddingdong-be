package ddingdong.ddingdongBE.common.cache.controller;

import ddingdong.ddingdongBE.common.cache.api.AdminCacheApi;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminCacheController implements AdminCacheApi {

    private final CacheManager cacheManager;

    @Override
    public void evictClubsCache() {
        Cache clubsCache = cacheManager.getCache("clubsCache");
        if (Objects.nonNull(clubsCache)) {
            clubsCache.clear();
        }
    }

    @Override
    public void evictFormsCache() {
        Cache formsCache = cacheManager.getCache("formsCache");
        if (Objects.nonNull(formsCache)) {
            formsCache.clear();
        }
        Cache formSectionsCache = cacheManager.getCache("formSectionsCache");
        if (Objects.nonNull(formSectionsCache)) {
            formSectionsCache.clear();
        }
    }
}
