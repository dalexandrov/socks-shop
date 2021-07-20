package io.helidon.socksshop;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/api")
@RegisterRestClient
public interface DiscountClient {
    @POST
    @Path("/discount")
    String getDiscount(String price);
}