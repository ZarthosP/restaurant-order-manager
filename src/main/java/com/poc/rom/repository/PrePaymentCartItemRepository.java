package com.poc.rom.repository;

import com.poc.rom.entity.PaymentBundle;
import org.springframework.data.repository.CrudRepository;

public interface PrePaymentCartItemRepository extends CrudRepository<PaymentBundle, Long> {
}
