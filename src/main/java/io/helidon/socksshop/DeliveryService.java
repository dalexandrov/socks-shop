package io.helidon.socksshop;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class DeliveryService {

    @PersistenceContext(unitName = "test")
    private EntityManager entityManager;

    @Incoming("delivery")
    public void deliverToCustomer(String cartId){
        Delivery delivery = new Delivery();
        delivery.setShoppingCartId(Long.parseLong(cartId));
        entityManager.persist(delivery);
        System.out.println("Delivering cart id: "+cartId);
    }
}
