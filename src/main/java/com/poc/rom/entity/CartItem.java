package com.poc.rom.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.poc.rom.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="cart_item")
public class CartItem {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "menu_item_id", referencedColumnName = "id")
    private MenuItem menuItem;

    private int quantity;

    private int preSelected;
    private int confirmed;

    private int ready;

    private int payed;
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "cart_id")
    private Cart cart;

    public int getUnconfirmedItemsQuantity() {
        if (quantity > confirmed + ready + payed) {
            return quantity - confirmed + ready + payed;
        }
        return 0;
    }

    public int getConfirmedItemsQuantity() {
        if (quantity > confirmed + ready + payed) {
            return quantity - confirmed + ready + payed;
        }
        return 0;
    }

    private void updateQuantity() {
        this.setQuantity(confirmed + ready + payed);
    }

    public void setPreSelected(int preSelected) {
        this.preSelected = preSelected;
        updateQuantity();
    }

    public void setConfirmed(int confirmed) {
        this.confirmed = confirmed;
        updateQuantity();
    }

    public void setReady(int ready) {
        this.ready = ready;
        updateQuantity();
    }

    public void setPayed(int payed) {
        this.payed = payed;
        updateQuantity();
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", menuItem=" + menuItem +
                ", quantity=" + quantity +
                ", cart=" + cart.getId() +
                '}';
    }
}
