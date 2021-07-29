package io.helidon.socksshop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.cucumber.java.Before;
import io.helidon.config.mp.MpConfigSources;
import io.helidon.messaging.connectors.kafka.KafkaConnector;
import io.helidon.microprofile.tests.junit5.Configuration;
import io.helidon.microprofile.tests.junit5.HelidonTest;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;

import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static io.helidon.socksshop.AbstractIntegrationTest.APPLICATION;
import static org.junit.jupiter.api.Assertions.assertEquals;

@HelidonTest
@Configuration(useExisting = true)
public class MessagingIT {

    @Inject
    WebTarget webTarget;

    static KafkaContainer kafka = new KafkaContainer();

    @BeforeAll
    public static void setup() {
        kafka.start();

        Map<String, String> configValues = new HashMap<>();
        configValues.put("mp.initializer.allow", "true");
        configValues.put("mp.messaging.incoming.from-kafka.connector", "helidon-kafka");
        configValues.put("mp.messaging.incoming.from-kafka.topic", "delivery");
        configValues.put("mp.messaging.incoming.from-kafka.auto.offset.reset", "latest");
        configValues.put("mp.messaging.incoming.from-kafka.enable.auto.commit", "true");
        configValues.put("mp.messaging.incoming.from-kafka.group.id", "helidon-group-1");

        configValues.put("mp.messaging.outgoing.to-kafka.connector", "helidon-kafka");
        configValues.put("mp.messaging.outgoing.to-kafka.topic", "delivery");

        configValues.put("mp.messaging.outgoing.test-delivery.connector", "helidon-kafka");
        configValues.put("mp.messaging.outgoing.test-delivery.topic", "delivery");

        configValues.put(KafkaConnector.CONNECTOR_PREFIX + "helidon-kafka.bootstrap.servers", kafka.getBootstrapServers());
        configValues.put(KafkaConnector.CONNECTOR_PREFIX + "helidon-kafka.key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        configValues.put(KafkaConnector.CONNECTOR_PREFIX + "helidon-kafka.value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        configValues.put(KafkaConnector.CONNECTOR_PREFIX + "helidon-kafka.key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        configValues.put(KafkaConnector.CONNECTOR_PREFIX + "helidon-kafka.value.deserializer", "org.apache.kafka.common.s" +
                "erialization.StringDeserializer");

        configValues.put("javax.sql.DataSource.test.dataSourceClassName", "org.h2.jdbcx.JdbcDataSource");
        configValues.put("javax.sql.DataSource.test.dataSource.url", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false");
        configValues.put("javax.sql.DataSource.test.dataSource.user", "sa");
        configValues.put("javax.sql.DataSource.test.dataSource.password", "");

        org.eclipse.microprofile.config.Config mpConfig = ConfigProviderResolver.instance()
                .getBuilder()
                .withSources(MpConfigSources.create(configValues))
                .build();
        ConfigProviderResolver.instance().registerConfig(mpConfig, Thread.currentThread().getContextClassLoader());
    }

    @Inject
    private ShoppingService shoppingService;


    @Test
    public void smokeTest() throws Exception {
        Thread.sleep(10000);

        JsonObject jsonObject = webTarget.path("/api/delivery/status/200")
                .request()
                .get(JsonObject.class);

        assertEquals("{\"id\":1,\"shoppingCartId\":200}",jsonObject.toString());
    }

    @Outgoing("delivery")
    public Publisher<String> preparePublisher() {
        return ReactiveStreams.of("200")
                .buildRs();
    }

    @AfterAll
    public static void killEmAll() {
        kafka.stop();
    }
}
