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
    void init(){
        Socks model1 = new Socks(1L,"Model1",10.00);
        entityManager.persist(model1);
        Socks model2 = new Socks(2L,"Model2",20.00);
        entityManager.persist(model2);

        Client client1 = new Client(1L,"Joe","Doe","Somewhere","12345");
        entityManager.persist(client1);

        ShoppingCart cart = new ShoppingCart();
        cart.setId(1L);
        cart.setClient(client1);
        cart.setCart(List.of(model1,model2));
        entityManager.persist(cart);

        entityManager.flush();
    }

    @Transactional
    void printall(){
        Query query = entityManager.createQuery("SELECT c FROM Client c");
        List<Client> result = (List<Client>) query.getResultList();
        result.stream().map(Client::toString).forEach(System.out::println);
    }
}
