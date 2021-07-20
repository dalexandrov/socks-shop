package io.helidon.socksshop;

import org.eclipse.microprofile.reactive.messaging.Outgoing;

import java.util.concurrent.SubmissionPublisher;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@ApplicationScoped
public class ShoppingService {

    private SubmissionPublisher<String> emitter = new SubmissionPublisher<>();

    @PersistenceContext
    private EntityManager entityManager;

    @Outgoing("delivery")
    public long checkout(ShoppingCart shoppingCart){
        entityManager.persist(shoppingCart);
        Long id = shoppingCart.getId();
        emitter.submit(String.valueOf(id));
        return id;
    }
}
