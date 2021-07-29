package io.helidon.socksshop;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("/api/invoicing")
public class InvoicingResource {

    private InvoicingService invoicingService;

    @Inject
    public InvoicingResource(InvoicingService invoicingService) {
        this.invoicingService = invoicingService;
    }

    @POST
    @Transactional
    public void handleInvoice(ShoppingCart shoppingCart){
        invoicingService.saveInvoice(shoppingCart);
        Response.status(Response.Status.CREATED).build();
    }
}
