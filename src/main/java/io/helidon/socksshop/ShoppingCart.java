package io.helidon.socksshop;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class ShoppingCart {

    @Id
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(
            cascade = CascadeType.ALL
    )
    @JoinColumn(name="socks_id")
    public List<Socks> cart = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Socks> getCart() {
        return cart;
    }

    public void setCart(List<Socks> cart) {
        this.cart = cart;
    }

    public double total(){
        return cart.stream().mapToDouble(Socks::getPrice).sum();
    }
}
