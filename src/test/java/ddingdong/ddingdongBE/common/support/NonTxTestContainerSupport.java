package ddingdong.ddingdongBE.common.support;


import static lombok.AccessLevel.PROTECTED;

import ddingdong.ddingdongBE.common.config.TestConfig;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;

@NoArgsConstructor(access = PROTECTED)
@ActiveProfiles("test")
@Import(TestConfig.class)
public abstract class NonTxTestContainerSupport {

    private static final String MYSQL_IMAGE = "mysql:8";
    private static final int MYSQL_PORT = 3306;
    private static final JdbcDatabaseContainer<?> MYSQL;

    @Autowired
    private DataInitializer dataInitializer;

    // 싱글톤
    static {
        MYSQL = new MySQLContainer<>(MYSQL_IMAGE)
                .withExposedPorts(MYSQL_PORT)
                .withReuse(true);

        MYSQL.start();
    }

    // 동적으로 DB 속성 할당
    @DynamicPropertySource
    public static void setUp(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.driver-class-name", MYSQL::getDriverClassName);
        registry.add("spring.datasource.url", MYSQL::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL::getUsername);
        registry.add("spring.datasource.password", MYSQL::getPassword);
    }

    @BeforeEach
    void delete() {
        dataInitializer.deleteAll();
    }
}
