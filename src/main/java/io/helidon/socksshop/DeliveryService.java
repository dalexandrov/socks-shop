package io.helidon.socksshop;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

@ApplicationScoped
public class DeliveryService {

    @PersistenceContext(unitName = "test")
    private EntityManager entityManager;


    @Incoming("incoming-delivery")
    @Transactional
    public void deliverToCustomer(String cartId){
        Delivery delivery = new Delivery();
        delivery.setShoppingCartId(Long.parseLong(cartId));
        entityManager.persist(delivery);
    }


    @Transactional
    public Delivery statusByShoppingCart(long shoppingCartId){
        Query query = entityManager.createQuery("SELECT delivery FROM Delivery delivery WHERE delivery.shoppingCartId = " + shoppingCartId);
        Delivery result = (Delivery) query.getSingleResult();
        return result;
    }
}
