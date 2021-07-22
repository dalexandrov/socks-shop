package io.helidon.socksshop;

import io.helidon.microprofile.tests.junit5.AddBean;
import io.helidon.microprofile.tests.junit5.HelidonTest;
import org.junit.jupiter.api.Test;


import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.WebTarget;

import static org.junit.jupiter.api.Assertions.assertEquals;

@HelidonTest
@AddBean(SockShopResource.class)
public class TestSocksResource {

    @Inject
    WebTarget webTarget;

    @Test
    void testAllSocks(){

        JsonArray jsonObject = webTarget.path("/api/shop/allSocks")
                .request()
                .get(JsonArray.class);

        assertEquals("[{\"model\":\"Model1\",\"price\":10.0},{\"model\":\"Model2\",\"price\":20.0}]",jsonObject.toString());
    }
}
