package io.helidon.socksshop;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("/api")
public class DiscountResource {

    @GET
    @Path("/discount/{price}")
    public Response getDiscountedPrice(@PathParam("price") String price) {
        double priceBeforeDiscount = Double.parseDouble(price);
        return Response
                .ok()
                .entity(String.valueOf(priceBeforeDiscount * 0.9))
                .build();
    }
}
