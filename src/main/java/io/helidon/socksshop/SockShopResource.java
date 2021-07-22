package io.helidon.socksshop;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkout(ShoppingCart shoppingCart){
        long id = shoppingService.checkout(shoppingCart);

        UriBuilder responseUri = UriBuilder.fromResource(SockShopResource.class);
        responseUri.path(Long.toString(id));

        return Response.created(responseUri.build()).build();
    }


    @GET
    @Path("/empty")
    public Response empty(){
        shoppingService.emptyCheckout();

        return Response.ok().build();
    }


    @GET
    @Path("/init")
    public Response init(){
        shoppingService.init();

        return Response.ok().build();
    }

    @GET
    @Path("/all")
    public Response all(){
        shoppingService.printall();

        return Response.ok().build();
    }
}
