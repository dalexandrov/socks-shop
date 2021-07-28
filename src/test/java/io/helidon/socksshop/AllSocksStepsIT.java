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

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class AllSocksStepsIT extends AbstractIntegrationTest {

    private static final Logger LOG = LoggerFactory.getLogger(AllSocksStepsIT.class);

    private RequestSpecification requestSpecification;

    private io.restassured.response.Response response;

    @Before
    public void beforeScenario() {
        APPLICATION.withLogConsumer(new Slf4jLogConsumer(LOG));
        requestSpecification = new RequestSpecBuilder()
                .setPort(APPLICATION.getFirstMappedPort())
                .build();
    }

    @Given("a request to get all socks")
    public void a_request_to_get_all_socks() {
        RestAssured.given(requestSpecification)
                .accept(MediaType.APPLICATION_JSON)
                .when()
                .get("/api/shop/allSocks")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());

    }

    @When("a user wants to get all socks")
    public void a_user_wants_to_get_all_socks() {
        response = RestAssured.given(requestSpecification)
                .accept(MediaType.APPLICATION_JSON)
                .when()
                .get("/api/shop/allSocks");
    }

    @Then("the output is response with socks")
    public void the_output_is_response_with_socks() {
        response.then()
                .statusCode(Response.Status.OK.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Matchers
                        .equalTo("[{\"model\":\"Model1\",\"price\":10.0},{\"model\":\"Model2\",\"price\":20.0}]"));
    }
}
