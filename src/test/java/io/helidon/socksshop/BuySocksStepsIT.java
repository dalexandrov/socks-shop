package io.helidon.socksshop;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.output.Slf4jLogConsumer;

import java.util.List;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class BuySocksStepsIT extends AbstractIntegrationTest {

    private static final Logger LOG = LoggerFactory.getLogger(BuySocksStepsIT.class);

    private RequestSpecification requestSpecification;

    private io.restassured.response.Response response;

    @Before
    public void beforeScenario() {
        APPLICATION.withLogConsumer(new Slf4jLogConsumer(LOG));
        requestSpecification = new RequestSpecBuilder()
                .setPort(APPLICATION.getFirstMappedPort())
                .build();
    }

    @Given("a shopping cart with one pair of socks")
    public void a_shopping_cart_with_one_pair_of_socks() {

        Socks socks = new Socks(100l, "Model1", 10.0);
        Client client1 = new Client(100L, "Joe", "Doe", "Somewhere", "12345");
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(100L);
        shoppingCart.setClient(client1);
        shoppingCart.setCart(List.of(socks));

        RestAssured.given(requestSpecification)
                .contentType(MediaType.APPLICATION_JSON)
                .body(shoppingCart)
                .when()
                .post("/api/shop/")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode());

    }

    @When("a user makes a checkout")
    public void a_user_makes_a_checkout() {
        response = RestAssured.given(requestSpecification)
                .accept(MediaType.APPLICATION_JSON)
                .when()
                .get("/api/shop/status/100");
    }

    @Then("checkout performed and submitted for delivery")
    public void the_output_is_response_with_socks() {
        response.then()
                .statusCode(Response.Status.OK.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Matchers
                        .equalTo("{\"cart\":[{\"id\":100,\"model\":\"Model1\",\"price\":10.0}],\"client\":{\"address\":\"Somewhere\",\"firstName\":\"Joe\",\"id\":100,\"lastName\":\"Doe\",\"postcode\":\"12345\"},\"id\":100}"));
    }
}
