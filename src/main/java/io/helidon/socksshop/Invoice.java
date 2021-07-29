package io.helidon.socksshop;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Invoice {

    @Id
    private Long id;

    private Long invoiceId;

    private double invoicingAmount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getInvoicingAmount() {
        return invoicingAmount;
    }

    public void setInvoicingAmount(double invoicingAmount) {
        this.invoicingAmount = invoicingAmount;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }
}
