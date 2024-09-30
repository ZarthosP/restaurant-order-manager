package com.poc.rom.repository;

import com.poc.rom.entity.CartItem;
import com.poc.rom.entity.PaymentBundle;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PaymentBundleRepository extends CrudRepository<PaymentBundle, Long> {

    List<PaymentBundle> findByTableId(Long tableId);
}
