package io.helidon.socksshop;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Socks {

    @Id
    @GeneratedValue
    private Long id;

    private String model;
    private double price;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Socks{" +
                "model='" + model + '\'' +
                ", price=" + price +
                '}';
    }
}