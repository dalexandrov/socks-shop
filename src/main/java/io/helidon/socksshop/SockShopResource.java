package io.helidon.socksshop;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

@ApplicationScoped
@Path("/api/shop")
public class SockShopResource {

    private ShoppingService shoppingService;

    @Inject
    public SockShopResource(ShoppingService shoppingService) {
        this.shoppingService = shoppingService;
    }


    @POST
    public Response checkout(ShoppingCart shoppingCart){
        long id = shoppingService.checkout(shoppingCart);

        UriBuilder responseUri = UriBuilder.fromResource(SockShopResource.class);
        responseUri.path(Long.toString(id));

        return Response.created(responseUri.build()).build();
    }


    @GET
    @Path("/status/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response status(@PathParam("id") String id){
        ShoppingCart cart = shoppingService.status(Long.parseLong(id));
        Jsonb jsonb = JsonbBuilder.create();
        String json = jsonb.toJson(cart, ShoppingCart.class);
        Response response = Response
                .status(Response.Status.OK)
                .entity(json)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
        return response;
    }


    @GET
    @Path("/empty")
    public Response empty(){
        shoppingService.emptyCheckout();

        return Response.ok().build();
    }

    @GET
    @Path("/allSocks")
    @Produces(MediaType.APPLICATION_JSON)
    public String allSocks(){
        List<Socks> socks = shoppingService.allSocks();
        Jsonb jsonb = JsonbBuilder.create();
        String json = jsonb.toJson(socks, Socks[].class);
        System.out.println(json);
        return json;
    }
}
