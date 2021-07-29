package io.helidon.socksshop;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@ApplicationScoped
public class InvoicingService {

    @PersistenceContext(unitName = "test")
    private EntityManager entityManager;

    public void saveInvoice(ShoppingCart shoppingCart) {
        Invoice invoice = new Invoice();
        invoice.setId(System.currentTimeMillis());
        invoice.setInvoiceId(shoppingCart.getId());
        invoice.setInvoicingAmount(shoppingCart.total());
        entityManager.persist(invoice);
    }
}
