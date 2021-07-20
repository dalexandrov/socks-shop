package io.helidon.socksshop;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DiscountCalculationService {

    @RestClient
    DiscountClient discountClient;

    public double calculate(ShoppingCart shoppingCart) {
        double total = shoppingCart.total();
        double discountedPrice = Double.parseDouble(discountClient.getDiscount(Double.toString(total)));

        return discountedPrice;
    }
}
