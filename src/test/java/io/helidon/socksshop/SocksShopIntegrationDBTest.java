package io.helidon.socksshop;


import io.helidon.config.Config;
import io.helidon.config.ConfigSources;
import io.helidon.config.mp.MpConfigSources;
import io.helidon.microprofile.tests.junit5.AddBean;
import io.helidon.microprofile.tests.junit5.Configuration;
import io.helidon.microprofile.tests.junit5.HelidonTest;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MariaDBContainer;

import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

@HelidonTest
@Configuration(useExisting = true)
public class SocksShopIntegrationDBTest {

    private static MariaDBContainer<?> db = new MariaDBContainer<>("mariadb:10.3.6")
            .withDatabaseName("mydb")
            .withUsername("test")
            .withPassword("test");

    @BeforeAll
    public static void setup() {
        db.start();

        Map<String, String> configValues = new HashMap<>();
        configValues.put("mp.initializer.allow", "true");
        configValues.put("javax.sql.DataSource.test.dataSourceClassName", "org.mariadb.jdbc.MariaDbDataSource");
        configValues.put("javax.sql.DataSource.test.dataSource.url", db.getJdbcUrl());
        configValues.put("javax.sql.DataSource.test.dataSource.user", db.getUsername());
        configValues.put("javax.sql.DataSource.test.dataSource.password", db.getPassword());


        org.eclipse.microprofile.config.Config mpConfig = ConfigProviderResolver.instance()
                .getBuilder()
                .withSources(MpConfigSources.create(configValues))
                .build();
        ConfigProviderResolver.instance().registerConfig(mpConfig, Thread.currentThread().getContextClassLoader());
    }

    @Inject
    private ShoppingService shoppingService;

    @Test
    public void smokeTest() {
        shoppingService.allSocks().stream().forEach(System.out::println);
        Assertions.assertTrue(true);
    }

    @AfterAll
    public static void stopAll() {
        System.out.println("Stopping AAALLLL!");
        db.stop();
    }
}
