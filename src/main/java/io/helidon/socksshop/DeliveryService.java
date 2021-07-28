package io.helidon.socksshop;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class DeliveryService {

    @PersistenceContext(unitName = "test")
    private EntityManager entityManager;

    @Incoming("delivery")
    @Transactional
    public void deliverToCustomer(String cartId){
        Delivery delivery = new Delivery();
        delivery.setShoppingCartId(Long.parseLong(cartId));
        entityManager.persist(delivery);
        System.out.println("Delivering cart id: "+cartId);
    }


    @Transactional
    public Delivery statusByShoppingCart(long shoppingCartId){
        Query query = entityManager.createQuery("SELECT delivery FROM Delivery delivery WHERE delivery.shoppingCartId = " + shoppingCartId);
        Delivery result = (Delivery) query.getSingleResult();
        return result;
    }
}
