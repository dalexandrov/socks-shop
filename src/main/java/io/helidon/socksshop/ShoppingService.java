package io.helidon.socksshop;

import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;
import org.reactivestreams.FlowAdapters;
import org.reactivestreams.Publisher;

import java.util.List;
import java.util.concurrent.SubmissionPublisher;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

@ApplicationScoped
public class ShoppingService {

    private final SubmissionPublisher<String> emitter = new SubmissionPublisher<>();

    @PersistenceContext(unitName = "test")
    private EntityManager entityManager;


    @Transactional
    public long checkout(ShoppingCart shoppingCart){
        entityManager.persist(shoppingCart);
        Long id = shoppingCart.getId();
        emitter.submit(String.valueOf(id));
        return id;
    }

    public long emptyCheckout(){
        long id = 42L;
        emitter.submit(String.valueOf(id));
        return id;
    }

    @Outgoing("delivery")
    public Publisher<String> preparePublisher() {
        // Create new publisher for emitting to by this::process
        return ReactiveStreams
                .fromPublisher(FlowAdapters.toPublisher(emitter))
                .buildRs();
    }


    @Transactional
    void printall(){
        Query query = entityManager.createQuery("SELECT c FROM Client c");
        List<Client> result = (List<Client>) query.getResultList();
        result.stream().map(Client::toString).forEach(System.out::println);
    }
}
