package ddingdong.ddingdongBE.common.support;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.generator.ArbitraryGeneratorContext;
import com.navercorp.fixturemonkey.api.introspector.BuilderArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.jqwik.JavaArbitraryResolver;
import com.navercorp.fixturemonkey.api.jqwik.JqwikPlugin;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.arbitraries.StringArbitrary;

public class FixtureMonkeyFactory {

    public static FixtureMonkey getBuilderIntrospectorMonkey() {
        return FixtureMonkey.builder()
                .objectIntrospector(BuilderArbitraryIntrospector.INSTANCE)
                .build();
    }

    public static FixtureMonkey getNotNullBuilderIntrospectorMonkey() {
        return FixtureMonkey.builder()
                .objectIntrospector(BuilderArbitraryIntrospector.INSTANCE)
                .defaultNotNull(true)
                .plugin(
                        new JqwikPlugin()
                                .javaArbitraryResolver(new JavaArbitraryResolver() {
                                    @Override
                                    public Arbitrary<String> strings(StringArbitrary stringArbitrary, ArbitraryGeneratorContext context) {
//                                        if (context.findAnnotation(MaxOfLength.class).isPresent()) {
//                                            return stringArbitrary.ofMaxLength(10);
//                                        }
                                        return stringArbitrary;
                                    }
                                })
                )
                .build();
    }

}
