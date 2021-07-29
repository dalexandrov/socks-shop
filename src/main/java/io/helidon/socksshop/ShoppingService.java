package io.helidon.socksshop;

import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.reactivestreams.FlowAdapters;
import org.reactivestreams.Publisher;

import java.util.List;
import java.util.concurrent.SubmissionPublisher;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

@ApplicationScoped
public class ShoppingService {

    private final SubmissionPublisher<String> emitter = new SubmissionPublisher<>();

    @PersistenceContext(unitName = "test")
    private EntityManager entityManager;

    @Inject
    @RestClient
    private InvoicingClient invoicingClient;


    @Transactional
    public long checkout(ShoppingCart shoppingCart){
        entityManager.persist(shoppingCart);
        Long id = shoppingCart.getId();

        emitter.submit(String.valueOf(id));

        invoicingClient.handleInvoice(shoppingCart);

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
    ShoppingCart status(long id){
        Query query = entityManager.createQuery("SELECT cart FROM ShoppingCart cart WHERE cart.id = " + id);
        ShoppingCart result = (ShoppingCart) query.getSingleResult();
        return result;
    }

    @Transactional
    List<Socks> allSocks(){
        Query query = entityManager.createQuery("SELECT s FROM Socks s");
        List<Socks> result = (List<Socks>) query.getResultList();
        return result;
    }
}
