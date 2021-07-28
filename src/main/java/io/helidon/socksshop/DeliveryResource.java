package io.helidon.socksshop;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("/api/delivery")
public class DeliveryResource {
    private DeliveryService deliveryService;

    @Inject
    public DeliveryResource(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GET
    @Path("/status/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response status(@PathParam("id") String id){
        Delivery delivery = deliveryService.statusByShoppingCart(Long.parseLong(id));
        Jsonb jsonb = JsonbBuilder.create();
        String json = jsonb.toJson(delivery, ShoppingCart.class);
        Response response = Response
                .status(Response.Status.OK)
                .entity(json)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
        return response;
    }
}
