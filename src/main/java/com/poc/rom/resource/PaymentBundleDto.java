package com.poc.rom.resource;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonRootName("paymentBundle")
public class PaymentBundleDto implements Serializable {

    private String name;

    private Long cartId;

    private Long tableId;

    private List<PrePaymentCartItemDto> prePaymentCartItems;
}
