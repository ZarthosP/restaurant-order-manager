package com.poc.rom.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="payment_bundle")
@EntityListeners(AuditingEntityListener.class)
public class PaymentBundle {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Long cartId;

    private Long tableId;

    @OneToMany(mappedBy = "paymentBundle", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PrePaymentCartItem> prePaymentCartItems;

    @CreatedDate
    private LocalDateTime createdDate;

    @Override
    public String toString() {
        return "PaymentBundle{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cartId=" + cartId +
                ", tableId=" + tableId +
                ", prePaymentCartItems=" + prePaymentCartItems +
                ", createdDate=" + createdDate +
                '}';
    }
}
