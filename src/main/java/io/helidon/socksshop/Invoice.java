package io.helidon.socksshop;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Invoice {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "cart_id")
    private ShoppingCart shoppingCart;

    private double invoicingAmount;
}
