package io.helidon.socksshop;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@RegisterRestClient(baseUri = "http://localhost:8080/api")
public interface InvoicingClient {
    @POST
    @Path("/invoicing")
    void handleInvoice(ShoppingCart shoppingCart);
}
