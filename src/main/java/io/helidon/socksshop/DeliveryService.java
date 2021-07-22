package io.helidon.socksshop;

import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class DeliveryService {

    @Incoming("delivery")
    public void deliverToCustomer(String cartId){
        System.out.println("Delivering cart id: "+cartId);
    }
}
