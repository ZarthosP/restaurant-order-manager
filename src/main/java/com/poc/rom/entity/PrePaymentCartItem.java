package com.poc.rom.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="pre_payment_cart_item")
public class PrePaymentCartItem {

    @Id
    @GeneratedValue
    private Long id;

    private int quantity;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "menu_item_id", referencedColumnName = "id")
    private MenuItem menuItem;

    @ManyToOne
    @JoinColumn(name = "payment_bundle_id")
    @JsonBackReference
    private PaymentBundle paymentBundle;

    @Override
    public String toString() {
        return "PrePaymentCartItem{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", menuItem=" + menuItem +
                ", paymentBundle=" + paymentBundle +
                '}';
    }
}
