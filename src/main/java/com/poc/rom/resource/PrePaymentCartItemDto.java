package com.poc.rom.resource;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.poc.rom.entity.MenuItem;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonRootName("paymentBundle")
public class PrePaymentCartItemDto implements Serializable {

    private Long id;

    private int quantity;

    private MenuItemDto menuItem;
}
