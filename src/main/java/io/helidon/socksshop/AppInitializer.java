package io.helidon.socksshop;

import org.apache.activemq.ActiveMQConnectionFactory;

import java.util.List;
import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.jms.Connection;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import static javax.interceptor.Interceptor.Priority.PLATFORM_AFTER;

@ApplicationScoped
public class AppInitializer {

    @PersistenceContext(unitName = "test")
    private EntityManager entityManager;

    @Transactional
    void onStartup(@Observes @Initialized(ApplicationScoped.class) final Object event) {
        Socks model1 = new Socks(1L, "Model1", 10.00);
        entityManager.persist(model1);
        Socks model2 = new Socks(2L, "Model2", 20.00);
        entityManager.persist(model2);

        Client client1 = new Client(1L, "Joe", "Doe", "Somewhere", "12345");
        entityManager.persist(client1);

        ShoppingCart cart = new ShoppingCart();
        cart.setId(1L);
        cart.setClient(client1);
        cart.setCart(List.of(model1, model2));
        entityManager.persist(cart);

        entityManager.flush();
    }

    private void makeMessagingConnections(@Observes @Priority(PLATFORM_AFTER + 1) @Initialized(ApplicationScoped.class) Object event) throws Throwable{
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
        Connection connection = connectionFactory.createConnection();
        connection.createSession(false, 1);

    }
}
