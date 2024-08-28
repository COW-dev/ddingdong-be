package ddingdong.ddingdongBE.common.support;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.BuilderArbitraryIntrospector;

public class FixtureMonkeyFactory {

    public static FixtureMonkey getBuilderIntrospectorMonkey() {
        return FixtureMonkey.builder()
                .objectIntrospector(BuilderArbitraryIntrospector.INSTANCE)
                .defaultNotNull(true)
                .build();
    }

    public static FixtureMonkey getBuilderIntrospectorNotNullMonkey() {
        return FixtureMonkey.builder()
                .objectIntrospector(BuilderArbitraryIntrospector.INSTANCE)
                .defaultNotNull(true)
                .build();
    }

}
