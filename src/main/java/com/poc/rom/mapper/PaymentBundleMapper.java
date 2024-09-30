package com.poc.rom.mapper;

import com.poc.rom.entity.PaymentBundle;
import com.poc.rom.entity.TableR;
import com.poc.rom.resource.PaymentBundleDto;
import com.poc.rom.resource.TableRDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PaymentBundleMapper {

    private ModelMapper mapper;

    public PaymentBundleMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public PaymentBundle map(PaymentBundleDto resource) {
        return mapper.map(resource, PaymentBundle.class);
    }

    public PaymentBundleDto map(PaymentBundle entity) {
        return mapper.map(entity, PaymentBundleDto.class);
    }
}
