package io.helidon.socksshop;

import io.helidon.microprofile.tests.junit5.HelidonTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.json.JsonArray;
import javax.ws.rs.client.WebTarget;

import static org.junit.jupiter.api.Assertions.assertEquals;

@HelidonTest
public class TestSocksResource {

    @Inject
    WebTarget webTarget;

    @Test
    void testAllSocks(){

        JsonArray jsonObject = webTarget.path("/api/shop/allSocks")
                .request()
                .get(JsonArray.class);

        assertEquals("[{\"id\":1,\"model\":\"Model1\",\"price\":10.0},{\"id\":2,\"model\":\"Model2\",\"price\":20.0}]",jsonObject.toString());
    }
}
